package com.wriesnig.api.stack;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class StackApiResponseParser {

    public ArrayList<StackUser> parseUsersResponse(JSONObject response) {
        if(!response.has("items")) return new ArrayList<>();
        JSONArray usersAsJson = response.getJSONArray("items");

        ArrayList<StackUser> users = new ArrayList<>();
        for (int i = 0; i < usersAsJson.length(); i++) {
            JSONObject current_user = usersAsJson.getJSONObject(i);
            int id = current_user.getInt("user_id");
            int reputation = current_user.getInt("reputation");
            String displayName = current_user.getString("display_name");
            String websiteUrl = current_user.has("website_url")?current_user.getString("website_url"):"";
            String link = current_user.getString("link");
            String profileImageUrl = current_user.getString("profile_image");
            int accountId = current_user.getInt("account_id");
            StackUser user = new StackUser(id, reputation, displayName, websiteUrl, link, profileImageUrl, accountId);
            users.add(user);
        }

        return users;
    }

    public ArrayList<String> parseTagsResponse(JSONObject response) {
        if(!response.has("items")) return new ArrayList<>();
        JSONArray tags = response.getJSONArray("items");
        ArrayList<String> mainTags = new ArrayList<>();

        for (int i = 0; i < tags.length(); i++) {
            JSONObject currentTag = tags.getJSONObject(i);
            String tag = currentTag.getString("tag_name");
            mainTags.add(tag);
        }

        return mainTags;
    }
}
