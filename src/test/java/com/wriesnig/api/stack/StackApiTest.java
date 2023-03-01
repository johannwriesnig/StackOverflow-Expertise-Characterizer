package com.wriesnig.api.stack;

import com.wriesnig.utils.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StackApiTest {

    @Test
    public void emptyStreamToString() throws IOException {
        String expected = "";

        assertEquals(expected, StackApi.getStringFromStream(getGzipInputStreamFromString(expected)));
    }

    @Test
    public void streamToString() throws IOException {
        String testSentence = "This represents the content of a gzip stream!";

        String streamContent = StackApi.getStringFromStream(getGzipInputStreamFromString(testSentence));
        assertEquals(testSentence, streamContent);
    }

    @Test
    public void nullStreamToString() {
        assertEquals("", StackApi.getStringFromStream(null));
    }

    @Test
    public void retrieveTags() throws IOException {
        String fileContent = Files.readString(Paths.get("src/main/resources/test/apiResponses/stack/tagsResponse.txt"));
        try (MockedStatic<StackApi> mocked = mockStatic(StackApi.class)) {
            mocked.when(() -> StackApi.getStreamFromAPICall(anyString())).thenReturn(getGzipInputStreamFromString(fileContent));
            mocked.when(() -> StackApi.getMainTags(anyInt())).thenCallRealMethod();
            mocked.when(() -> StackApi.getStringFromStream(any())).thenCallRealMethod();
            assertEquals("wcf", StackApi.getMainTags(0).get(1));
        }
    }

    @Test
    void getStreamFromApi() throws IOException {
        URL url = mock(URL.class);
        HttpURLConnection httpURLConnection = mock(HttpURLConnection.class);
        when(httpURLConnection.getResponseCode()).thenReturn(StackApi.CODE_OK);
        when(httpURLConnection.getInputStream()).thenReturn(getInputStreamInGzipFormat(""));
        when(url.openConnection()).thenReturn(httpURLConnection);

        try (MockedStatic<Logger> mockedLogger = mockStatic(Logger.class);
             MockedStatic<StackApi> mockedStackApi = mockStatic(StackApi.class);) {

            mockedStackApi.when(() -> StackApi.getStreamFromAPICall(anyString())).thenCallRealMethod();
            mockedStackApi.when(() -> StackApi.getUrl(anyString())).thenReturn(url);

            GZIPInputStream gzipInputStream = StackApi.getStreamFromAPICall("");
            assertNotNull(gzipInputStream);
        }
    }

    @Test
    void getStreamFromApiHasStatus400() throws IOException {
        URL url = mock(URL.class);
        HttpURLConnection httpURLConnection = mock(HttpURLConnection.class);
        when(httpURLConnection.getResponseCode()).thenReturn(StackApi.CODE_BAD_REQUEST);
        InputStream stream = getInputStreamInGzipFormat("{\"error_id\": "+StackApi.CODE_THROTTLE_VIOLATION+"}");
        when(httpURLConnection.getErrorStream()).thenReturn(stream);
        when(url.openConnection()).thenReturn(httpURLConnection);

        try (MockedStatic<Logger> mockedLogger = mockStatic(Logger.class, withSettings().defaultAnswer(Answers.CALLS_REAL_METHODS));
             MockedStatic<StackApi> mockedStackApi = mockStatic(StackApi.class)){

            mockedStackApi.when(() -> StackApi.getStringFromStream(any())).thenCallRealMethod();
            mockedStackApi.when(() -> StackApi.getStreamFromAPICall(anyString())).thenCallRealMethod();
            mockedStackApi.when(() -> StackApi.getUrl(anyString())).thenReturn(url);
            assertThrows(RuntimeException.class, ()->StackApi.getStreamFromAPICall(""));
            mockedLogger.verify(() -> Logger.error(anyString()), times(1));
        }
    }

    @Test
    public void retrieveUsers() throws IOException {
        String fileContent = Files.readString(Paths.get("src/main/resources/test/apiResponses/stack/usersResponse.txt"));
        try (MockedStatic<StackApi> mocked = mockStatic(StackApi.class)) {
            mocked.when(() -> StackApi.getStreamFromAPICall(anyString())).thenReturn(getGzipInputStreamFromString(fileContent));
            mocked.when(() -> StackApi.getUsers(any())).thenCallRealMethod();
            mocked.when(() -> StackApi.getStringFromStream(any())).thenCallRealMethod();
            ArrayList<Integer> ids = new ArrayList<>();
            ids.add(1);
            ids.add(2);
            assertEquals(2, StackApi.getUsers(ids).size());
        }
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
    public void getStringFromInputStreamThrowsIOException() throws IOException {
        try (MockedStatic<Logger> mockedLogger = mockStatic(Logger.class);
             MockedConstruction<InputStreamReader> mockedReader = mockConstruction(InputStreamReader.class,
                     (mock, context) -> {
                         doThrow(IOException.class).when(mock).read();
                     })) {
            StackApi.getStringFromStream(getGzipInputStreamFromString("test"));
            mockedLogger.verify(() -> Logger.error(any(), any()), times(1));
        }
    }
}
