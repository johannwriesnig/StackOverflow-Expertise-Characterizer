package com.wriesnig.githubapi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class GHUserDataParser {
    private GHUserDataParser(){}

    public static GHUser parseUserByLoginResponse(JSONObject user){
        if(user.get("message").equals("Not Found")) return null;

        String login = user.getString("login");
        String profile_image_url = user.getString("avatar_url");
        String name = user.getString("name");
        String website_url = user.getString("blog");
        String location = user.getString("Location");

        return new GHUser(login, profile_image_url, name, location, website_url);

    }

    public static ArrayList<String> parseUsersByFullName(JSONObject response){
        if(response.getInt("total_count") == 0) return null;
        ArrayList<String> users_login = new ArrayList<>();

        JSONArray users = response.getJSONArray("items");
        for(int i=0; i<users.length(); i++){
            JSONObject current_user = users.getJSONObject(i);
            users_login.add(current_user.getString("login"));
        }

        return users_login;
    }
}
