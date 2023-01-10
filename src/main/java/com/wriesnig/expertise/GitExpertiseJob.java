package com.wriesnig.expertise;

import com.wriesnig.githubapi.GitApi;

import java.io.File;
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
        String usersReposPath = "repos/"+user.getGitLogin()+"/";
        File currentUser = new File(usersReposPath);
        currentUser.mkdirs();
        ArrayList<String> repos = gitApi.getReposByLogin(user.getGitLogin());
        gitApi.downloadRepos(user.getGitLogin(), repos, usersReposPath);

        File[] reposDirs = currentUser.listFiles();

        for(File repo : reposDirs){
            System.out.println(repo.getName());
        }
        //System.out.println(user.getStackDisplayName() + " has following repose: " + repos);
        //deleteDirectory(userRepos);
    }

    void deleteDirectory(File file){
        if (file.isDirectory()) {
            File[] entries = file.listFiles();
            if (entries != null) {
                for (File entry : entries) {
                    deleteDirectory(entry);
                }
            }
        }
        file.delete();
    }
}
