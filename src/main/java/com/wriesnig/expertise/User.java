package com.wriesnig.expertise;

import com.wriesnig.githubapi.GitUser;
import com.wriesnig.stackoverflow.api.StackUser;

import java.util.ArrayList;

public class User {
    private int stackId;
    private String stackDisplayName;
    private String gitLogin;
    private Expertise expertise;
    private int isEstablishedOnStack;
    private ArrayList<String> mainTags;

    public User(StackUser so_user, GitUser gh_user){
        this.stackDisplayName = so_user.getDisplayName();
        this.stackId = so_user.getId();
        this.gitLogin = gh_user.getLogin();
        this.expertise = new Expertise();
        this.isEstablishedOnStack = so_user.getReputation()>=3000?1:0;
        this.mainTags = so_user.getMainTags();
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

    public int getIsEstablishedOnStack(){ return isEstablishedOnStack;}

    public ArrayList<String> getMainTags() {
        return mainTags;
    }
}
