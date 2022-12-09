package com.wriesnig.expertise;

import com.wriesnig.githubapi.GitUser;
import com.wriesnig.stackoverflow.api.StackUser;

public class User {
    private int stackId;
    private String stackDisplayName;
    private String gitLogin;
    private Expertise expertise;

    public User(StackUser so_user, GitUser gh_user){
        this.stackDisplayName = so_user.getDisplayName();
        this.stackId = so_user.getId();
        this.gitLogin = gh_user.getLogin();
        this.expertise = new Expertise();
    }

    public int getStackId() {
        return stackId;
    }

    public void setStackId(int stackId) {
        this.stackId = stackId;
    }

    public String getStackDisplayName() {
        return stackDisplayName;
    }

    public void setStackDisplayName(String stackDisplayName) {
        this.stackDisplayName = stackDisplayName;
    }

    public String getGitLogin() {
        return gitLogin;
    }

    public void setGitLogin(String gitLogin) {
        this.gitLogin = gitLogin;
    }

    public Expertise getExpertise() {
        return expertise;
    }

    public void setExpertise(Expertise expertise) {
        this.expertise = expertise;
    }
}
