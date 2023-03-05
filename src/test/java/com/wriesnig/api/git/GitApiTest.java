package com.wriesnig.api.git;

import com.wriesnig.utils.Logger;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class GitApiTest {

    @Test
    public void retrieveUserByLogin() throws IOException {
        String fileName = "src/main/resources/test/apiResponses/git/loginResponse1.txt";
        try (MockedStatic<GitApi> mockedGitApi = mockStatic(GitApi.class)) {

            mockedGitApi.when(() -> GitApi.getStreamFromAPICall(anyString())).thenReturn(new FileInputStream(fileName));
            mockedGitApi.when(() -> GitApi.getUserByLogin(any())).thenCallRealMethod();
            mockedGitApi.when(() -> GitApi.getStringFromStream(any())).thenCallRealMethod();
            GitUser user = GitApi.getUserByLogin("octocat");
            assertNotNull(user);
            assertEquals("octocat", user.getLogin());
            mockedGitApi.verify(()->GitApi.getStreamFromAPICall(anyString()), times(1));
            mockedGitApi.verify(()->GitApi.getStringFromStream(any()), times(1));
        }
    }

    @Test
    public void retrieveUsersByFullName() throws IOException {
        String fileName = "src/main/resources/test/apiResponses/git/fullNameResponse.txt";
        try (MockedStatic<GitApi> mockedGitApi = mockStatic(GitApi.class)) {
            mockedGitApi.when(() -> GitApi.getStreamFromAPICall(anyString())).thenReturn(new FileInputStream(fileName));
            mockedGitApi.when(() -> GitApi.getUsersByFullName(any())).thenCallRealMethod();
            mockedGitApi.when(() -> GitApi.getStringFromStream(any())).thenCallRealMethod();
            ArrayList<String> logins = GitApi.getUsersByFullName("Max Mustermann");
            assertEquals(2, logins.size());
            mockedGitApi.verify(()->GitApi.getStreamFromAPICall(anyString()), times(1));
            mockedGitApi.verify(()->GitApi.getStringFromStream(any()), times(1));
        }
    }

    @Test
    public void retrieveRepos() throws IOException {
        String fileName = "src/main/resources/test/apiResponses/git/reposResponse.txt";
        try (MockedStatic<GitApi> mockedGitApi = mockStatic(GitApi.class)) {
            mockedGitApi.when(() -> GitApi.getStreamFromAPICall(anyString())).thenReturn(new FileInputStream(fileName));
            mockedGitApi.when(() -> GitApi.getReposByLogin(any())).thenCallRealMethod();
            mockedGitApi.when(() -> GitApi.getStringFromStream(any())).thenCallRealMethod();
            ArrayList<Repo> repos = GitApi.getReposByLogin("johannwriesnig");
            assertEquals(3, repos.size());
            assertEquals("johannwriesnig/Runner", repos.get(0).getName());
            assertEquals("johannwriesnig/SE2_Einzelbeispiel", repos.get(1).getName());
            assertEquals("johannwriesnig/ControlNet", repos.get(2).getName());
            mockedGitApi.verify(()->GitApi.getStreamFromAPICall(anyString()), times(1));
            mockedGitApi.verify(()->GitApi.getStringFromStream(any()), times(1));
        }
    }

    @Test
    public void getStringFromInputStreamThrowsIOException() throws IOException {
        try (MockedStatic<Logger> mockedLogger = mockStatic(Logger.class);
             MockedConstruction<BufferedReader> mockedBufferedReader = mockConstruction(BufferedReader.class,
                     (mock, context) -> {
                         doThrow(IOException.class).when(mock).read();
                     })) {
            InputStream inputStream = InputStream.nullInputStream();
            GitApi.getStringFromStream(inputStream);
            mockedLogger.verify(() -> Logger.error(any(), any()), times(1));
            assertEquals(1, mockedBufferedReader.constructed().size());
            BufferedReader bufferedReader = mockedBufferedReader.constructed().get(0);
            verify(bufferedReader, times(1)).read();
        }
    }



    @Test
    public void downloadingReposThrowsIOException() {
        try (MockedStatic<GitApi> mockedGitApi = mockStatic(GitApi.class);
             MockedStatic<Logger> mockedLogger = mockStatic(Logger.class)) {
            mockedGitApi.when(() -> GitApi.tryDownloadRepos(any(), anyString(), any())).thenThrow(IOException.class);
            mockedGitApi.when(() -> GitApi.downloadRepos(any(), anyString(), any())).thenCallRealMethod();
            GitApi.downloadRepos(new ArrayList<Repo>(), "", new LinkedBlockingQueue<>());
            mockedLogger.verify(() -> Logger.error(any(), any()), times(1));
        }
    }

    @Test
    public void downloadingReposThrowsInterruptedException() {
        try (MockedStatic<GitApi> mockedGitApi = mockStatic(GitApi.class);
             MockedStatic<Logger> mockedLogger = mockStatic(Logger.class)) {
            mockedGitApi.when(() -> GitApi.tryDownloadRepos(any(), anyString(), any())).thenThrow(InterruptedException.class);
            mockedGitApi.when(() -> GitApi.downloadRepos(any(), anyString(), any())).thenCallRealMethod();
            GitApi.downloadRepos(new ArrayList<Repo>(), "", new LinkedBlockingQueue<>());
            mockedLogger.verify(() -> Logger.error(any(), any()), times(1));
        }
    }

    @Test
    public void downloadReposAndUnZip() throws IOException, InterruptedException {
        String fileName = "src/main/resources/test/apiResponses/git/zip/repo.zip";
        try (MockedStatic<GitApi> mocked = mockStatic(GitApi.class)) {
            mocked.when(() -> GitApi.getStreamFromAPICall(anyString())).thenReturn(new FileInputStream(fileName));
            mocked.when(() -> GitApi.downloadRepos(any(), anyString(), any())).thenCallRealMethod();
            mocked.when(() -> GitApi.tryDownloadRepos(any(), anyString(), any())).thenCallRealMethod();
            mocked.when(() -> GitApi.processZipEntry(any(), any(), any())).thenCallRealMethod();
            mocked.when(() -> GitApi.getZipStreamFromRepo(anyString())).thenCallRealMethod();

            String path = "src/main/resources/test/apiResponses/git/zip/";
            String repoName = "Runner";
            Repo repo = new Repo(repoName, "java", false, 0);
            ArrayList<Repo> repos = new ArrayList<>();
            repos.add(repo);
            BlockingQueue<Repo> downloadedRepos = new LinkedBlockingQueue<>();
            GitApi.downloadRepos(repos, path, downloadedRepos);

            Repo downloadedRepo = downloadedRepos.take();
            Repo finished = downloadedRepos.take();

            assertEquals("", finished.getName());
            assertEquals(repo, downloadedRepo);

            File repoDir = new File(path + downloadedRepo.getFileName());
            assertTrue(repoDir.isDirectory());
            FileUtils.deleteDirectory(repoDir);
        }
    }

    @Test
    public void emptyStreamToString() throws IOException {
        String expected = "";
        assertEquals(expected, GitApi.getStringFromStream(InputStream.nullInputStream()));
    }
}
