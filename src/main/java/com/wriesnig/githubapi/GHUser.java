package com.wriesnig.githubapi;

public class GHUser {
    private String login;
    private String profile_image_url;
    private String name;
    private String location;
    private String website_url;

    public GHUser(String login, String profile_image_url, String name, String location, String website_url) {
        this.login = login;
        this.profile_image_url = profile_image_url;
        this.name = name;
        this.location = location;
        this.website_url = website_url;
    }

    @Override
    public String toString() {
        return "GHUser: " + login;
    }

    public String getLogin() {
        return login;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getWebsite_url() {
        return website_url;
    }
}
