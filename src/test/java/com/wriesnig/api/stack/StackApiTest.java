package com.wriesnig.api.stack;

import com.wriesnig.api.git.GitApi;
import com.wriesnig.utils.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    public void nullStreamToString(){
        assertEquals("", StackApi.getStringFromStream(null));
    }

    @Test
    public void retrieveTags() throws IOException {
        String fileContent = Files.readString(Paths.get("src/main/resources/test/apiResponses/stack/tagsResponse.txt"));
        try(MockedStatic<StackApi> mocked = mockStatic(StackApi.class)){
            mocked.when(()->StackApi.getStreamFromAPICall(anyString())).thenReturn(getGzipInputStreamFromString(fileContent));
            mocked.when(()->StackApi.getMainTags(anyInt())).thenCallRealMethod();
            mocked.when(()->StackApi.getStringFromStream(any())).thenCallRealMethod();
            assertEquals("wcf", StackApi.getMainTags(0).get(1));
        }
    }

    @Test
    public void retrieveUsers() throws IOException {
        String fileContent = Files.readString(Paths.get("src/main/resources/test/apiResponses/stack/usersResponse.txt"));
        try(MockedStatic<StackApi> mocked = mockStatic(StackApi.class)){
            mocked.when(()->StackApi.getStreamFromAPICall(anyString())).thenReturn(getGzipInputStreamFromString(fileContent));
            mocked.when(()->StackApi.getUsers(any())).thenCallRealMethod();
            mocked.when(()->StackApi.getStringFromStream(any())).thenCallRealMethod();
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

    @Test
    public void getStreamFromApiCallThrowsIOException() {
        try (MockedStatic<Logger> mockedLogger = mockStatic(Logger.class);
             MockedStatic<StackApi> mockedStackApi = mockStatic(StackApi.class)){
            mockedStackApi.when(() -> StackApi.getGzipInputStream(any())).thenThrow(IOException.class);
            mockedStackApi.when(() -> StackApi.getStreamFromAPICall(any())).thenCallRealMethod();
            assertNull(StackApi.getStreamFromAPICall("path"));
            mockedLogger.verify(() -> Logger.error(any(), any()), times(1));
        }
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
