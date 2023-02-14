package com.wriesnig.api.git;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


public class GitApiResponseParserTest {
    private GitApiResponseParser gitApiResponseParser;

    @BeforeEach
    public void setUp() {
       gitApiResponseParser = new GitApiResponseParser();
    }

    @Test
    public void parseUserLoginResponse() throws IOException {
        String content = Files.readString(Paths.get("src/main/resources/test/apiResponses/git/loginResponse1.txt"));
        JSONObject json = new JSONObject(content);
        GitUser gitUser = gitApiResponseParser.parseUserByLoginResponse(json);

        assertEquals("octocat", gitUser.getLogin());
        assertEquals("monalisa octocat", gitUser.getName());
        assertEquals("https://github.com/octocat", gitUser.getHtmlUrl());
        assertEquals("https://github.com/images/error/octocat_happy.gif", gitUser.getProfileImageUrl());
        assertEquals("https://github.com/blog", gitUser.getWebsiteUrl());
    }

    @Test
    public void parseFullNameResponse() throws IOException {
        String content = Files.readString(Paths.get("src/main/resources/test/apiResponses/git/fullNameResponse.txt"));
        JSONObject json = new JSONObject(content);
        ArrayList<String> logins = gitApiResponseParser.parseUsersByFullName(json);

        assertEquals("jonhoo",logins.get(0));
        assertEquals("jskeet",logins.get(1));
    }

    @Test
    public void parseFullNameResponseNoUserFound() throws IOException {
        String content = Files.readString(Paths.get("src/main/resources/test/apiResponses/git/fullNameResponseNoMatch.txt"));
        JSONObject json = new JSONObject(content);
        ArrayList<String> logins = gitApiResponseParser.parseUsersByFullName(json);

        assertEquals(0, logins.size());
    }

    @Test
    public void parseReposResponse() throws IOException {
        String content = Files.readString(Paths.get("src/main/resources/test/apiResponses/git/reposResponse.txt"));
        JSONArray json = new JSONArray(content);
        ArrayList<Repo> repos = gitApiResponseParser.parseReposByLogin(json);

        Repo runner = repos.get(0);
        assertEquals("johannwriesnig/Runner", runner.getName());
        assertEquals("assembly", runner.getMainLanguage());
        assertEquals(0, runner.getStars());

        Repo se2_Einzelbeispiel = repos.get(1);
        assertEquals("johannwriesnig/SE2_Einzelbeispiel", se2_Einzelbeispiel.getName());
        assertEquals("", se2_Einzelbeispiel.getMainLanguage());
        assertEquals(0, se2_Einzelbeispiel.getStars());
    }

    @Test
    public void parseLoginResponseWhenNoUserFound() throws IOException {
        String content = Files.readString(Paths.get("src/main/resources/test/apiResponses/git/loginResponseUserNotFound.txt"));
        JSONObject json = new JSONObject(content);
        GitUser gitUser = gitApiResponseParser.parseUserByLoginResponse(json);

        assertNull(gitUser);
    }

    @Test
    public void parseLoginResponseWithNullableFieldName() throws IOException {
        String content = Files.readString(Paths.get("src/main/resources/test/apiResponses/git/loginResponse2.txt"));
        JSONObject json = new JSONObject(content);
        GitUser gitUser = gitApiResponseParser.parseUserByLoginResponse(json);

        assertEquals("johannwriesnig",gitUser.getLogin());
        assertEquals("", gitUser.getName());
    }

    @AfterEach
    public void tearDown() {
       gitApiResponseParser = null;
    }
}
