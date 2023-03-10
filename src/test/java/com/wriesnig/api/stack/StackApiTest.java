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
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StackApiTest {
    private HttpURLConnection connection;
    private StackApi stackApi;

    @BeforeEach
    public void setUp() throws IOException {
        connection = mock(HttpURLConnection.class);
        stackApi = mock(StackApi.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        doReturn(connection).when(stackApi).getConnectionFromUrl(any());
    }


    @Test
    public void shouldReturnUsers() {
        doReturn(StackApiResponses.RESPONSE_USERS).when(stackApi).getResponse(any());
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(1);
        ArrayList<StackUser> stackUsers = stackApi.getUsers(ids);
        assertEquals("Jon Skeet", stackUsers.get(0).getDisplayName());
        assertEquals("johannwriesnig", stackUsers.get(1).getDisplayName());
        verify(stackApi, times(1)).getResponse(any());

    }

    @Test
    public void shouldMake2RequestSinceIdsListIsGreater100() {
        doReturn(StackApiResponses.RESPONSE_USERS).when(stackApi).getResponse(any());
        ArrayList<Integer> ids = new ArrayList<>();
        for (int i = 0; i <= 150; i++)
            ids.add(i);
        stackApi.getUsers(ids);
        verify(stackApi, times(2)).getResponse(anyString());

    }

    @Test
    public void shouldReturnMainTags(){
        doReturn(StackApiResponses.RESPONSE_TAGS).when(stackApi).getResponse(any());
        assertEquals("wcf", stackApi.getMainTags(0).get(1));
    }

    @Test
    public void shouldReturnResponseCorrect() throws IOException {
        doReturn(StackApi.CODE_OK).when(connection).getResponseCode();
        doReturn(getInputStreamInGzipFormat(String.valueOf(StackApiResponses.RESPONSE_TAGS))).when(connection).getInputStream();
        JSONObject response = stackApi.getResponse("");
        assertTrue(response.has("items"));

    }


    @Test
    public void shouldReturnEmptyString() throws IOException {
        String expected = "";
        assertEquals(expected, stackApi.getStringFromStream(getGzipInputStreamFromString(expected)));
    }

    @Test
    public void shouldConvertGZIPInputStreamToString() throws IOException {
        String testSentence = "This represents the content of a gzip stream!";

        String streamContent = stackApi.getStringFromStream(getGzipInputStreamFromString(testSentence));
        assertEquals(testSentence, streamContent);
    }

    @Test
    public void shouldReturnEmptyJsonWhenGivenNullStream() {
        assertEquals("{}", stackApi.getStringFromStream(null));
    }

    @Test
    public void shouldThrowRuntimeExceptionWhenBadRequest() throws IOException {
        doReturn(StackApi.CODE_BAD_REQUEST).when(connection).getResponseCode();
        doReturn(getInputStreamInGzipFormat(String.valueOf(StackApiResponses.RESPONSE_502))).when(connection).getErrorStream();
        try (MockedStatic<Logger> loggerMockedStatic = mockStatic(Logger.class)) {
            assertThrows(RuntimeException.class, () -> stackApi.getStreamFromAPICall(""));
            loggerMockedStatic.verify(() -> Logger.error(anyString()), times(1));
        }
    }

    @Test
    public void shouldReturnGZIPStream() throws IOException {
        doReturn(StackApi.CODE_OK).when(connection).getResponseCode();
        doReturn(getInputStreamInGzipFormat(String.valueOf(StackApiResponses.RESPONSE_USERS))).when(connection).getInputStream();
        assertNotNull(stackApi.getStreamFromAPICall(""));
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
            stackApi.getStringFromStream(getGzipInputStreamFromString("test"));
            assertEquals(1, mockedReader.constructed().size());
            mockedLogger.verify(() -> Logger.error(any(), any()), times(1));
        }
    }

    @Test
    public void shouldLogWhenGZIPStreamCannotBeCreated() throws IOException {
        doReturn(StackApi.CODE_OK).when(connection).getResponseCode();
        InputStream inputStreamInNoZIPFormat = new ByteArrayInputStream(String.valueOf(StackApiResponses.RESPONSE_USERS).getBytes(StandardCharsets.UTF_8));
        doReturn(inputStreamInNoZIPFormat).when(connection).getInputStream();
        try (MockedStatic<Logger> mockedLogger = mockStatic(Logger.class)) {
            stackApi.getStreamFromAPICall(" broken url ");
            mockedLogger.verify(() -> Logger.error(any(), any()), times(1));
        }
    }


    @AfterEach
    public void tearDown() {
        connection = null;
    }
}
