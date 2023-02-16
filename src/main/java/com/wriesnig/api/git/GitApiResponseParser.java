package com.wriesnig.api.git;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class GitApiResponseParser {

    public GitUser parseUserByLoginResponse(JSONObject user) {
        if (user.has("message")) return null;

        String login = user.getString("login");
        String profileImageUrl = user.getString("avatar_url");
        String name = user.isNull("name") ? "" : user.getString("name");
        String websiteUrl = user.getString("blog");
        String htmlUrl = user.getString("html_url");

        return new GitUser(login, profileImageUrl, name, htmlUrl, websiteUrl);
    }

    public ArrayList<String> parseUsersByFullName(JSONObject response) {
        if (response.getInt("total_count") == 0) return new ArrayList<>();
        ArrayList<String> usersLogin = new ArrayList<>();

        JSONArray users = response.getJSONArray("items");
        for (int i = 0; i < users.length(); i++) {
            JSONObject current_user = users.getJSONObject(i);
            usersLogin.add(current_user.getString("login"));
        }

        return usersLogin;
    }

    public ArrayList<Repo> parseReposByLogin(JSONArray response) {
        ArrayList<Repo> repos = new ArrayList<>();

        for (int i = 0; i < response.length(); i++) {
            JSONObject currentRepo = response.getJSONObject(i);
            boolean isFork = currentRepo.getBoolean("fork");
            if(isFork)continue;
            String repoName = currentRepo.getString("full_name");
            String repoMainLanguage = currentRepo.isNull("language") ? "" : currentRepo.getString("language").toLowerCase();
            int stars = currentRepo.getInt("stargazers_count");
            repos.add(new Repo(repoName, repoMainLanguage,stars));
        }

        return repos;
    }
}
