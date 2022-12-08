package com.wriesnig.expertise;

import com.wriesnig.githubapi.GitUser;
import com.wriesnig.stackoverflow.api.StackUser;

public class User {
    private int soId;
    private String soDisplayName;
    private String ghLogin;
    private Expertise expertise;

    public User(StackUser so_user, GitUser gh_user){
        this.soDisplayName = so_user.getDisplayName();
        this.soId = so_user.getId();
        this.ghLogin = gh_user.getLogin();
    }

    public int getSoId() {
        return soId;
    }

    public void setSoId(int soId) {
        this.soId = soId;
    }

    public String getSoDisplayName() {
        return soDisplayName;
    }

    public void setSoDisplayName(String soDisplayName) {
        this.soDisplayName = soDisplayName;
    }

    public String getGhLogin() {
        return ghLogin;
    }

    public void setGhLogin(String ghLogin) {
        this.ghLogin = ghLogin;
    }

    public Expertise getExpertise() {
        return expertise;
    }

    public void setExpertise(Expertise expertise) {
        this.expertise = expertise;
    }
}
