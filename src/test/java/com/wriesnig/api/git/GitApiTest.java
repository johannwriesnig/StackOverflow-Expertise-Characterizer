package com.wriesnig.api.git;

import com.wriesnig.api.stack.StackApi;
import org.apache.commons.io.FileUtils;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

public class GitApiTest {

    @Test
    public void emptyStreamToString() throws IOException {
        String expected = "";

        assertEquals(expected, GitApi.getStringFromStream(InputStream.nullInputStream()));
    }

    @Test
    public void retrieveUserByLogin() throws IOException {
        String fileName = "src/main/resources/test/apiResponses/git/loginResponse1.txt";
        try(MockedStatic<GitApi> mocked = mockStatic(GitApi.class)){
            mocked.when(()->GitApi.getStreamFromAPICall(anyString())).thenReturn(new FileInputStream(fileName));
            mocked.when(()->GitApi.getUserByLogin(any())).thenCallRealMethod();
            mocked.when(()->GitApi.getStringFromStream(any())).thenCallRealMethod();
            GitUser user = GitApi.getUserByLogin("octocat");
            assertNotNull(user);
            assertEquals("octocat", user.getLogin());
        }
    }

    @Test
    public void retrieveUsersByFullName() throws IOException {
        String fileName = "src/main/resources/test/apiResponses/git/fullNameResponse.txt";
        try(MockedStatic<GitApi> mocked = mockStatic(GitApi.class)){
            mocked.when(()->GitApi.getStreamFromAPICall(anyString())).thenReturn(new FileInputStream(fileName));
            mocked.when(()->GitApi.getUsersByFullName(any())).thenCallRealMethod();
            mocked.when(()->GitApi.getStringFromStream(any())).thenCallRealMethod();
            ArrayList<String> logins = GitApi.getUsersByFullName("Max Mustermann");
            assertEquals(2, logins.size());
        }
    }

    @Test
    public void retrieveRepos() throws IOException {
        String fileName = "src/main/resources/test/apiResponses/git/reposResponse.txt";
        try(MockedStatic<GitApi> mocked = mockStatic(GitApi.class)){
            mocked.when(()->GitApi.getStreamFromAPICall(anyString())).thenReturn(new FileInputStream(fileName));
            mocked.when(()->GitApi.getReposByLogin(any())).thenCallRealMethod();
            mocked.when(()->GitApi.getStringFromStream(any())).thenCallRealMethod();
            ArrayList<Repo> repos = GitApi.getReposByLogin("johannwriesnig");
            assertEquals(2, repos.size());
            assertEquals("johannwriesnig/Runner", repos.get(0).getName());
        }
    }

    @Test
    public void downloadReposAndUnZip() throws IOException, InterruptedException {
        String fileName = "src/main/resources/test/apiResponses/git/zip/repo.zip";
        try(MockedStatic<GitApi> mocked = mockStatic(GitApi.class)){
            mocked.when(()->GitApi.getStreamFromAPICall(anyString())).thenReturn(new FileInputStream(fileName));
            mocked.when(()->GitApi.downloadRepos(any(), anyString(), any())).thenCallRealMethod();
            mocked.when(()->GitApi.tryDownloadRepos(any(), anyString(), any())).thenCallRealMethod();
            mocked.when(()->GitApi.processZipEntry(any(), any(), any())).thenCallRealMethod();
            mocked.when(()->GitApi.getZipStreamFromRepo(anyString())).thenCallRealMethod();

            String path = "src/main/resources/test/apiResponses/git/zip/";
            String repoName = "Runner";
            Repo repo = new Repo(repoName, "java", 0);
            ArrayList<Repo> repos = new ArrayList<>();
            repos.add(repo);
            BlockingQueue<Repo> downloadedRepos = new LinkedBlockingQueue<>();
            GitApi.downloadRepos(repos, path, downloadedRepos);

            Repo downloadedRepo = downloadedRepos.take();
            Repo finished = downloadedRepos.take();

            assertEquals("",finished.getName());
            assertEquals(repo, downloadedRepo);

            File repoDir = new File(path+downloadedRepo.getFileName());
            assertTrue(repoDir.isDirectory());
            FileUtils.deleteDirectory(repoDir);
        }
    }
}
