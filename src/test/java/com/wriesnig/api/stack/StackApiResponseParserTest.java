package com.wriesnig.api.stack;

import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StackApiResponseParserTest {
    private StackApiResponseParser responseParser;

    @BeforeEach
    public void setUp(){
        responseParser = new StackApiResponseParser();
    }

    @Test
    public void shouldParseAndReturnTagsFromApiResponse() {
        JSONObject tagsResponse = StackApiResponses.RESPONSE_TAGS;
        ArrayList<String> tags = responseParser.parseTagsResponse(tagsResponse);

        assertEquals(".net", tags.get(0));
        assertEquals("wcf", tags.get(1));
        assertEquals("c#", tags.get(2));
    }

    @Test
    public void shouldParseAndReturnUsersFromApiResponse() {
        JSONObject usersResponse = StackApiResponses.RESPONSE_USERS;
        ArrayList<StackUser> users = responseParser.parseUsersResponse(usersResponse);

        assertEquals(2, users.size());

        StackUser user = users.get(1);
        assertEquals(21153013, user.getId());
        assertEquals(1, user.getReputation());
        assertEquals("johannwriesnig", user.getDisplayName());
        assertEquals("https://github.com/johannwriesnig", user.getWebsiteUrl());
        assertEquals("https://stackoverflow.com/users/21153013/johannwriesnig", user.getLink());
        assertEquals("https://lh3.googleusercontent.com/a/AEdFTp5-samj1gA8JHKWrvcVLEySLoR5yKqEjawBcHs=k-s256", user.getProfileImageUrl());
        assertEquals(27710156, user.getAccountId());
    }

    @AfterEach
    public void tearDown(){
        responseParser = null;
    }
}
