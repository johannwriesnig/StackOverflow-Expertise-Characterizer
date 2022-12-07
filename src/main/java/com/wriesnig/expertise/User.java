package com.wriesnig.expertise;

import com.wriesnig.expertise.Expertise;
import com.wriesnig.githubapi.GHUser;
import com.wriesnig.stackoverflow.api.SOUser;

public class User {
    private int so_id;
    private String so_display_name;
    private String gh_login;
    private Expertise expertise;

    public User(SOUser so_user, GHUser gh_user){
        this.so_display_name = so_user.getDisplayName();
        this.so_id = so_user.getId();
        this.gh_login = gh_user.getLogin();
    }

    public int getSo_id() {
        return so_id;
    }

    public void setSo_id(int so_id) {
        this.so_id = so_id;
    }

    public String getSo_display_name() {
        return so_display_name;
    }

    public void setSo_display_name(String so_display_name) {
        this.so_display_name = so_display_name;
    }

    public String getGh_login() {
        return gh_login;
    }

    public void setGh_login(String gh_login) {
        this.gh_login = gh_login;
    }

    public Expertise getExpertise() {
        return expertise;
    }

    public void setExpertise(Expertise expertise) {
        this.expertise = expertise;
    }
}
