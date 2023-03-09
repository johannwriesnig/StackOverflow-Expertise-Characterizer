package com.wriesnig.api.stack;

import com.wriesnig.utils.Logger;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StackApiTest {
    private final String users_response = "src/main/resources/test/apiResponses/stack/usersResponse.txt";

    private HttpURLConnection connection;

    @BeforeEach
    public void setUp() {
        connection = mock(HttpURLConnection.class);
    }

    @Test
    public void shouldReturnUsers() throws IOException {
        try (MockedStatic<StackApi> stackApiMockedStatic = mockStatic(StackApi.class)) {
            stackApiMockedStatic.when(() -> StackApi.getUsers(any())).thenCallRealMethod();
            stackApiMockedStatic.when(() -> StackApi.getResponse(any())).thenReturn(new JSONObject(Files.readString(Path.of(users_response))));
            ArrayList<Integer> ids = new ArrayList<>();
            ids.add(1);
            ArrayList<StackUser> stackUsers = StackApi.getUsers(ids);
            assertEquals("Jon Skeet", stackUsers.get(0).getDisplayName());
            assertEquals("johannwriesnig", stackUsers.get(1).getDisplayName());
            stackApiMockedStatic.verify(() -> StackApi.getResponse(any()), times(1));
        }
    }

    @Test
    public void shouldMake2RequestSinceIdsListIsGreater100() throws IOException {
        try (MockedStatic<StackApi> stackApiMockedStatic = mockStatic(StackApi.class)) {
            stackApiMockedStatic.when(() -> StackApi.getUsers(any())).thenCallRealMethod();
            stackApiMockedStatic.when(() -> StackApi.getResponse(any())).thenReturn(new JSONObject(Files.readString(Path.of(users_response))));
            ArrayList<Integer> ids = new ArrayList<>();
            for (int i = 0; i <= 150; i++)
                ids.add(i);
            StackApi.getUsers(ids);
            stackApiMockedStatic.verify(() -> StackApi.getResponse(anyString()), times(2));
        }
    }

    @Test
    public void shouldReturnMainTags() throws IOException {
        String tagsResponse = Files.readString(Paths.get("src/main/resources/test/apiResponses/stack/tagsResponse.txt"));
        try (MockedStatic<StackApi> stackApiMockedStatic = mockStatic(StackApi.class)) {
            stackApiMockedStatic.when(() -> StackApi.getMainTags(anyInt())).thenCallRealMethod();
            stackApiMockedStatic.when(() -> StackApi.getResponse(any())).thenReturn(new JSONObject(tagsResponse));
            assertEquals("wcf", StackApi.getMainTags(0).get(1));
        }
    }

    @Test
    public void shouldReturnResponseCorrect() throws IOException {
        String tagsResponse = Files.readString(Paths.get("src/main/resources/test/apiResponses/stack/tagsResponse.txt"));
        doReturn(StackApi.CODE_OK).when(connection).getResponseCode();
        doReturn(getInputStreamInGzipFormat(tagsResponse)).when(connection).getInputStream();
        try (MockedStatic<StackApi> stackApiMockedStatic = mockStatic(StackApi.class)) {
            setupMockedStackApi(stackApiMockedStatic);
            JSONObject response = StackApi.getResponse("");
            assertTrue(response.has("items"));
        }
    }


    @Test
    public void shouldReturnEmptyString() throws IOException {
        String expected = "";
        assertEquals(expected, StackApi.getStringFromStream(getGzipInputStreamFromString(expected)));
    }

    @Test
    public void shouldConvertGZIPInputStreamToString() throws IOException {
        String testSentence = "This represents the content of a gzip stream!";

        String streamContent = StackApi.getStringFromStream(getGzipInputStreamFromString(testSentence));
        assertEquals(testSentence, streamContent);
    }

    @Test
    public void shouldReturnEmptyJsonWhenGivenNullStream() {
        assertEquals("{}", StackApi.getStringFromStream(null));
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenBadRequest() throws IOException {
        String throttleViolation = Files.readString(Paths.get("src/main/resources/test/apiResponses/stack/502Response.txt"));
        doReturn(StackApi.CODE_BAD_REQUEST).when(connection).getResponseCode();
        doReturn(getInputStreamInGzipFormat(throttleViolation)).when(connection).getErrorStream();
        try (MockedStatic<StackApi> stackApiMockedStatic = mockStatic(StackApi.class);
             MockedStatic<Logger> loggerMockedStatic = mockStatic(Logger.class)) {

            setupMockedStackApi(stackApiMockedStatic);
            assertThrows(RuntimeException.class, () -> StackApi.getStreamFromAPICall(""));
            loggerMockedStatic.verify(()->Logger.error(anyString()), times(1));
        }
    }

    @Test
    public void shouldReturnGZIPStream() throws IOException {
        try (MockedStatic<StackApi> stackApiMockedStatic = mockStatic(StackApi.class)) {
            setupMockedStackApi(stackApiMockedStatic);
            doReturn(StackApi.CODE_OK).when(connection).getResponseCode();
            doReturn(getInputStreamInGzipFormat(users_response)).when(connection).getInputStream();
            assertNotNull(StackApi.getStreamFromAPICall(""));
        }
    }


    public InputStream getInputStreamInGZIPFormat(String fileName) throws IOException {
        String fileContent = Files.readString(Path.of(fileName));
        return getInputStreamInGzipFormat(fileContent);
    }

    public void setupMockedStackApi(MockedStatic<StackApi> stackApiMockedStatic) {
        stackApiMockedStatic.when(() -> StackApi.getUsers(any())).thenCallRealMethod();
        stackApiMockedStatic.when(() -> StackApi.getMainTags(anyInt())).thenCallRealMethod();
        stackApiMockedStatic.when(() -> StackApi.getStreamFromAPICall(any())).thenCallRealMethod();
        stackApiMockedStatic.when(() -> StackApi.getStringFromStream(any())).thenCallRealMethod();
        stackApiMockedStatic.when(() -> StackApi.setKey(any())).thenCallRealMethod();
        stackApiMockedStatic.when(() -> StackApi.getResponse(anyString())).thenCallRealMethod();
        stackApiMockedStatic.when(() -> StackApi.getConnectionFromUrl(any())).thenReturn(connection);
    }


    public GZIPInputStream getGzipInputStreamFromString(String s) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        gzipOutputStream.write(s.getBytes(StandardCharsets.UTF_8));
        gzipOutputStream.finish();
        gzipOutputStream.close();
        return new GZIPInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
    }

    public InputStream getInputStreamInGzipFormat(String s) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        gzipOutputStream.write(s.getBytes(StandardCharsets.UTF_8));
        gzipOutputStream.finish();
        gzipOutputStream.close();
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }


    @Test
    public void shouldLogWhenReaderThrowsIOException() throws IOException {
        try (MockedStatic<Logger> mockedLogger = mockStatic(Logger.class);
             MockedConstruction<InputStreamReader> mockedReader = mockConstruction(InputStreamReader.class,
                     (mock, context) -> {
                         doThrow(IOException.class).when(mock).read();
                     })) {
            StackApi.getStringFromStream(getGzipInputStreamFromString("test"));
            assertEquals(1, mockedReader.constructed().size());
            mockedLogger.verify(() -> Logger.error(any(), any()), times(1));
        }
    }

    @Test
    public void shouldLogWhenGZIPStreamCannotBeCreated() throws IOException {
        doReturn(StackApi.CODE_OK).when(connection).getResponseCode();
        InputStream inputStreamInNoZIPFormat = new ByteArrayInputStream(Files.readString(Path.of(users_response)).getBytes(StandardCharsets.UTF_8));
        doReturn(inputStreamInNoZIPFormat).when(connection).getInputStream();
        try (MockedStatic<Logger> mockedLogger = mockStatic(Logger.class);
             MockedStatic<StackApi> stackApiMockedStatic = mockStatic(StackApi.class)) {
            setupMockedStackApi(stackApiMockedStatic);
            StackApi.getStreamFromAPICall(" broken url ");
            mockedLogger.verify(() -> Logger.error(any(), any()), times(1));
        }
    }

    @AfterEach
    public void tearDown() {
        connection = null;
    }
}
