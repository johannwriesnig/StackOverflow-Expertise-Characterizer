package com.wriesnig.expertise;

import com.wriesnig.githubapi.GitHubApi;

import java.util.ArrayList;

public class GHExpertiseJob implements Runnable{
    private User user;
    private GitHubApi gitHubApi;

    public GHExpertiseJob(User user, GitHubApi gitHubApi){
        this.user = user;
        this.gitHubApi = gitHubApi;
    }
    @Override
    public void run() {
        ArrayList<String> repos = gitHubApi.getReposByLogin(user.getGhLogin());
        System.out.println(user.getSoDisplayName() + " has following repose: " + repos);
    }
}
