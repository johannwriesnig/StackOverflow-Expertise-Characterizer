package com.wriesnig.api.git;

import com.wriesnig.utils.Logger;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.io.*;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GitApiTest {
    private GitApi gitApi;
    private HttpURLConnection connection;

    @BeforeEach
    public void setUp() throws IOException {
        gitApi = mock(GitApi.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
        connection = mock(HttpURLConnection.class);
        doReturn(connection).when(gitApi).getConnectionFromUrl(any());
    }


    @Test
    public void shouldReturnInputStream() throws IOException {
        doReturn(GitApiResponses.getLoginResponse()).when(connection).getInputStream();
        assertNotNull(gitApi.getStreamFromAPICall(""));
    }

    @Test
    public void shouldThrowRuntimeException() throws IOException {
        Logger.deactivatePrinting();
        doReturn(GitApiResponses.getLoginResponse()).when(connection).getInputStream();
        doReturn(GitApi.CODE_BAD_CREDENTIALS).when(connection).getResponseCode();

        assertThrows(RuntimeException.class, () -> gitApi.getStreamFromAPICall(""));
    }

    @Test
    public void shouldSetTokenInHeaderWhenSet() throws IOException {
        Logger.deactivatePrinting();
        doReturn(GitApiResponses.getLoginResponse()).when(connection).getInputStream();
        String token = "token";
        GitApi.setToken(token);
        gitApi.getStreamFromAPICall("");
        verify(connection, times(1)).setRequestProperty("Authorization", "Bearer " + token);

    }

    @Test
    public void retrieveUserByLogin() throws IOException {
        doReturn(GitApiResponses.getLoginResponse()).when(gitApi).getStreamFromAPICall(anyString());
        GitUser user = gitApi.getUserByLogin("octocat");
        assertNotNull(user);
        assertEquals("octocat", user.getLogin());
        verify(gitApi, times(1)).getStreamFromAPICall(anyString());
        verify(gitApi, times(1)).getStringFromStream(any());

    }

    @Test
    public void retrieveUsersByFullName() throws IOException {
        doReturn(GitApiResponses.getFullNamesResponse()).when(gitApi).getStreamFromAPICall(anyString());

        ArrayList<String> logins = gitApi.getUsersByFullName("Max Mustermann");
        assertEquals(2, logins.size());
        verify(gitApi, times(1)).getStreamFromAPICall(anyString());
        verify(gitApi, times(1)).getStringFromStream(any());

    }

    @Test
    public void retrieveRepos() throws IOException {
        doReturn(GitApiResponses.getReposResponse()).when(gitApi).getStreamFromAPICall(anyString());

        ArrayList<Repo> repos = gitApi.getReposByLogin("johannwriesnig");
        assertEquals(3, repos.size());
        assertEquals("johannwriesnig/Runner", repos.get(0).getName());
        assertEquals("johannwriesnig/SE2_Einzelbeispiel", repos.get(1).getName());
        assertEquals("johannwriesnig/ControlNet", repos.get(2).getName());
        verify(gitApi, times(1)).getStreamFromAPICall(anyString());
        verify(gitApi, times(1)).getStringFromStream(any());

    }

    @Test
    public void getStringFromInputStreamThrowsIOException() throws IOException {
        try (MockedStatic<Logger> mockedLogger = mockStatic(Logger.class);
             MockedConstruction<BufferedReader> mockedBufferedReader = mockConstruction(BufferedReader.class,
                     (mock, context) -> {
                         doThrow(IOException.class).when(mock).read();
                     })) {
            InputStream inputStream = InputStream.nullInputStream();
            gitApi.getStringFromStream(inputStream);
            mockedLogger.verify(() -> Logger.error(any(), any()), times(1));
            assertEquals(1, ((MockedConstruction<?>) mockedBufferedReader).constructed().size());
            BufferedReader bufferedReader = mockedBufferedReader.constructed().get(0);
            verify(bufferedReader, times(1)).read();
        }
    }


    @Test
    public void downloadingReposThrowsIOException() throws IOException, InterruptedException {
        try (MockedStatic<Logger> mockedLogger = mockStatic(Logger.class)) {
            doThrow(IOException.class).when(gitApi).tryDownloadRepos(any(), anyString(), any());
            gitApi.downloadRepos(new ArrayList<Repo>(), "", new LinkedBlockingQueue<>());
            mockedLogger.verify(() -> Logger.error(any(), any()), times(1));
        }
    }

    @Test
    public void downloadingReposThrowsInterruptedException() throws IOException, InterruptedException {
        try (MockedStatic<Logger> mockedLogger = mockStatic(Logger.class)) {
            doThrow(InterruptedException.class).when(gitApi).tryDownloadRepos(any(), anyString(), any());
            gitApi.downloadRepos(new ArrayList<Repo>(), "", new LinkedBlockingQueue<>());
            mockedLogger.verify(() -> Logger.error(any(), any()), times(1));
        }
    }

    @Test
    public void downloadReposAndUnZip() throws IOException, InterruptedException {
        String fileName = "src/main/resources/test/apiResponses/git/zip/repo.zip";

        doReturn(new FileInputStream(fileName)).when(gitApi).getStreamFromAPICall(anyString());

        String path = "src/main/resources/test/apiResponses/git/zip/";
        String repoName = "Runner";
        Repo repo = new Repo(repoName, "java", false, 0);
        ArrayList<Repo> repos = new ArrayList<>();
        repos.add(repo);
        BlockingQueue<Repo> downloadedRepos = new LinkedBlockingQueue<>();
        gitApi.downloadRepos(repos, path, downloadedRepos);

        Repo downloadedRepo = downloadedRepos.take();
        Repo finished = downloadedRepos.take();

        assertEquals("", finished.getName());
        assertEquals(repo, downloadedRepo);

        File repoDir = new File(path + downloadedRepo.getFileName());
        assertTrue(repoDir.isDirectory());
        FileUtils.deleteDirectory(repoDir);
    }

    @Test
    public void emptyStreamToString() throws IOException {
        String expected = "";
        assertEquals(expected, gitApi.getStringFromStream(InputStream.nullInputStream()));
    }

}
