package com.wriesnig.githubapi;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Class parses the JSON-Responses from the Api.
 */
public class GitApiDataParser {
    private GitApiDataParser(){}

    /**
     * Creates an GHUser based on the Rest Api response.
     * @param user
     * @return
     */
    public static GitUser parseUserByLoginResponse(JSONObject user){
        if(user.has("message") && user.get("message").equals("Not Found")) return null;

        String login = user.getString("login");
        String profileImageUrl = user.getString("avatar_url");
        String name = user.getString("name");
        String websiteUrl = user.getString("blog");
        String htmlUrl = user.getString("html_url");

        return new GitUser(login, profileImageUrl, name, htmlUrl, websiteUrl);

    }

    /**
     * Returns the unique logins from the full_name search.
     * @param response
     * @return
     */
    public static ArrayList<String> parseUsersByFullName(JSONObject response){
        if(response.getInt("total_count") == 0) return new ArrayList<>();
        ArrayList<String> usersLogin = new ArrayList<>();

        JSONArray users = response.getJSONArray("items");
        for(int i=0; i<users.length(); i++){
            JSONObject current_user = users.getJSONObject(i);
            usersLogin.add(current_user.getString("login"));
        }

        return usersLogin;
    }

    public static ArrayList<Repo> parseReposByLogin(JSONArray response){
        ArrayList<Repo> repos = new ArrayList<>();

        for(int i=0; i<response.length();i++){
            JSONObject currentRepo = response.getJSONObject(i);
            String repoName = currentRepo.getString("full_name");
            String repoMainLanguage = currentRepo.isNull("language")?"":currentRepo.getString("language").toLowerCase();
            repos.add(new Repo(repoName, repoMainLanguage));
        }

        return repos;
    }
}
