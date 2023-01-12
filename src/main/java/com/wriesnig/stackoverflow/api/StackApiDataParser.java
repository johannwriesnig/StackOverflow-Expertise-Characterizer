package com.wriesnig.stackoverflow.api;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Parser for Stackoverflow Rest-Api responses
 */
public class StackApiDataParser {
    private StackApiDataParser(){}

    /**
     * Returns StackoverflowUsers based on response
     * @param response
     * @return
     */
    public static ArrayList<StackUser> parseUsersResponse(JSONObject response){
        JSONArray usersAsJson = response.getJSONArray("items");

        ArrayList<StackUser> users = new ArrayList<>();
        for(int i=0; i<usersAsJson.length(); i++){
            JSONObject current_user = usersAsJson.getJSONObject(i);
            int id = current_user.getInt("user_id");
            int reputation = current_user.getInt("reputation");
            String displayName = current_user.getString("display_name");
            String websiteUrl = current_user.getString("website_url");
            String link = current_user.getString("link");
            String profileImageUrl = current_user.getString("profile_image");
            int accountId = current_user.getInt("account_id");
            StackUser user = new StackUser(id, reputation, displayName, websiteUrl, link, profileImageUrl, accountId);
            users.add(user);
        }

        return users;
    }
}
