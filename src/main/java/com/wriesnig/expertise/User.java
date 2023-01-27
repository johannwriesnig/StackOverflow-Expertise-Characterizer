package com.wriesnig.expertise;

import com.wriesnig.api.git.GitUser;
import com.wriesnig.api.stack.StackUser;

import java.util.ArrayList;

public class User {
    private int stackId;
    private String stackDisplayName;
    private String gitLogin;
    private Expertise expertise;
    private int isEstablishedOnStack;
    private String profileImageUrl;
    private ArrayList<String> mainTags;

    public User(StackUser stackUser, GitUser gitUser) {
        this.stackDisplayName = stackUser.getDisplayName();
        this.stackId = stackUser.getId();
        this.gitLogin = gitUser.getLogin();
        this.expertise = new Expertise();
        this.isEstablishedOnStack = stackUser.getReputation() >= 3000 ? 1 : 0;
        this.mainTags = stackUser.getMainTags();
        this.profileImageUrl = stackUser.getProfileImageUrl();
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

    public int getIsEstablishedOnStack() {
        return isEstablishedOnStack;
    }

    public ArrayList<String> getMainTags() {
        return mainTags;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
