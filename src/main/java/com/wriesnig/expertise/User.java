package com.wriesnig.expertise;

import com.wriesnig.api.git.DefaultGitUser;
import com.wriesnig.api.git.GitUser;
import com.wriesnig.api.stack.StackUser;

import java.util.ArrayList;

public class User {
    private final GitUser gitUser;
    private final StackUser stackUser;

    private Expertise expertise;

    public User(StackUser stackUser, GitUser gitUser) {
        this.gitUser = gitUser;
        this.stackUser =stackUser;
        this.expertise = new Expertise();
    }

    public int getStackId() {
        return stackUser.getId();
    }

    public String getStackDisplayName() {
        return stackUser.getDisplayName();
    }

    public String getGitLogin() {
        return gitUser.getLogin();
    }

    public Expertise getExpertise() {
        return expertise;
    }

    public void setExpertise(Expertise expertise) {
        this.expertise = expertise;
    }

    public int getIsEstablishedOnStack() {
        return stackUser.getReputation() >= 3000 ? 1 : 0;
    }

    public ArrayList<String> getMainTags() {
        return stackUser.getMainTags();
    }

    public String getProfileImageUrl() {
        return stackUser.getProfileImageUrl();
    }

    public GitUser getGitUser(){
        return gitUser;
    }
}
