package com.wriesnig.expertise;

import com.wriesnig.api.git.GitUser;
import com.wriesnig.api.stack.StackUser;

import java.util.ArrayList;

public class User {
    private final int stackId;
    private final String stackDisplayName;
    private final String gitLogin;
    private final int isEstablishedOnStack;
    private final String profileImageUrl;
    private final ArrayList<String> mainTags;

    private Expertise expertise;

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


    public String getStackDisplayName() {
        return stackDisplayName;
    }

    public String getGitLogin() {
        return gitLogin;
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
