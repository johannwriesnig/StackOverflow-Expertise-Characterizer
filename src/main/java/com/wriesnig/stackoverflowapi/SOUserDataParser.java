package com.wriesnig.stackoverflowapi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SOUserDataParser {
    private SOUserDataParser(){}

    public static ArrayList<SOUser> parseUsersResponse(JSONObject response){
        JSONArray users_as_json = response.getJSONArray("items");

        ArrayList<SOUser> users = new ArrayList<>();
        for(int i=0; i<users_as_json.length(); i++){
            JSONObject current_user = users_as_json.getJSONObject(i);
            int id = current_user.getInt("user_id");
            int reputation = current_user.getInt("reputation");
            String display_name = current_user.getString("display_name");
            String website_url = current_user.getString("website_url");
            String location = current_user.getString("location");
            String profile_image_url = current_user.getString("profile_image");
            int account_id = current_user.getInt("account_id");
            SOUser user = new SOUser(id, reputation, display_name, website_url, location, profile_image_url, account_id);
            users.add(user);
        }

        return users;
    }
}
