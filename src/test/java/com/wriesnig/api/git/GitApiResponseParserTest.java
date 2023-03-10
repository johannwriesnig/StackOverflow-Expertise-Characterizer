package com.wriesnig.api.git;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class GitApiResponseParserTest {
    private GitApiResponseParser gitApiResponseParser;

    @BeforeEach
    public void setUp() {
       gitApiResponseParser = new GitApiResponseParser();
    }

    @Test
    public void shouldReturnUserByGitLogin() throws IOException {
        JSONObject loginResponse = new JSONObject(GitApiResponses.RESPONSE_LOGIN);
        GitUser gitUser = gitApiResponseParser.parseUserByLoginResponse(loginResponse);

        assertEquals("octocat", gitUser.getLogin());
        assertEquals("monalisa octocat", gitUser.getName());
        assertEquals("https://github.com/octocat", gitUser.getHtmlUrl());
        assertEquals("https://github.com/images/error/octocat_happy.gif", gitUser.getProfileImageUrl());
        assertEquals("https://github.com/blog", gitUser.getWebsiteUrl());
    }

    @Test
    public void shouldReturnFullNames(){
        JSONObject fullNamesResponse = new JSONObject(GitApiResponses.RESPONSE_FULL_NAMES);
        ArrayList<String> logins = gitApiResponseParser.parseUsersByFullName(fullNamesResponse);

        assertEquals("jonhoo",logins.get(0));
        assertEquals("jskeet",logins.get(1));
    }

    @Test
    public void shouldReturnEmptyList() throws IOException {
        JSONObject fullNamesNoMatchResponse = new JSONObject(GitApiResponses.RESPONSE_FULL_NAMES_NO_MATCH);
        ArrayList<String> logins = gitApiResponseParser.parseUsersByFullName(fullNamesNoMatchResponse);

        assertEquals(0, logins.size());
    }

    @Test
    public void shouldReturnRepos() throws IOException {
        JSONArray reposResponse = new JSONArray(GitApiResponses.RESPONSE_REPOS);
        ArrayList<Repo> repos = gitApiResponseParser.parseReposByLogin(reposResponse);

        Repo runner = repos.get(0);
        assertEquals("johannwriesnig/Runner", runner.getName());
        assertEquals("assembly", runner.getMainLanguage());

        Repo se2_Einzelbeispiel = repos.get(1);
        assertEquals("johannwriesnig/SE2_Einzelbeispiel", se2_Einzelbeispiel.getName());
        assertEquals("", se2_Einzelbeispiel.getMainLanguage());
    }

    @Test
    public void shouldReturnDefaultUserWhenNoUserFound() throws IOException {
        JSONObject loginUserNotFoundResponse = new JSONObject(GitApiResponses.RESPONSE_LOGIN_USER_NOT_FOUND);
        GitUser gitUser = gitApiResponseParser.parseUserByLoginResponse(loginUserNotFoundResponse);

        assertTrue(gitUser instanceof DefaultGitUser);
    }

    @Test
    public void shouldPassParseWithNullableFields() throws IOException {
        JSONObject loginResponseWithNullableFields = new JSONObject(GitApiResponses.RESPONSE_LOGIN_WITH_NULLABLE_FIELDS);
        GitUser gitUser = gitApiResponseParser.parseUserByLoginResponse(loginResponseWithNullableFields);

        assertEquals("johannwriesnig",gitUser.getLogin());
        assertEquals("", gitUser.getName());
    }

    @AfterEach
    public void tearDown() {
       gitApiResponseParser = null;
    }
}
