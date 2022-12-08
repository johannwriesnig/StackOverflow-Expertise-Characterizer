package com.wriesnig.expertise;

import com.wriesnig.githubapi.GitApi;

import java.util.ArrayList;

public class GitExpertiseJob implements Runnable{
    private User user;
    private GitApi gitApi;

    public GitExpertiseJob(User user, GitApi gitApi){
        this.user = user;
        this.gitApi = gitApi;
    }
    @Override
    public void run() {
        ArrayList<String> repos = gitApi.getReposByLogin(user.getGhLogin());
        System.out.println(user.getSoDisplayName() + " has following repose: " + repos);
    }
}
