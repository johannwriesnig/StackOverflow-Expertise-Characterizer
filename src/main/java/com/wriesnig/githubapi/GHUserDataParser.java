package com.wriesnig.githubapi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Class parses the JSON-Responses from the Api.
 */
public class GHUserDataParser {
    private GHUserDataParser(){}

    /**
     * Creates an GHUser based on the Rest Api response.
     * @param user
     * @return
     */
    public static GHUser parseUserByLoginResponse(JSONObject user){
        if(user.has("message") && user.get("message").equals("Not Found")) return null;

        String login = user.getString("login");
        String profile_image_url = user.getString("avatar_url");
        String name = user.getString("name");
        String website_url = user.getString("blog");
        String html_url = user.getString("html_url");

        return new GHUser(login, profile_image_url, name, html_url, website_url);

    }

    /**
     * Returns the unique logins from the full_name search.
     * @param response
     * @return
     */
    public static ArrayList<String> parseUsersByFullName(JSONObject response){
        if(response.getInt("total_count") == 0) return new ArrayList<>();
        ArrayList<String> users_login = new ArrayList<>();

        JSONArray users = response.getJSONArray("items");
        for(int i=0; i<users.length(); i++){
            JSONObject current_user = users.getJSONObject(i);
            users_login.add(current_user.getString("login"));
        }

        return users_login;
    }
}
