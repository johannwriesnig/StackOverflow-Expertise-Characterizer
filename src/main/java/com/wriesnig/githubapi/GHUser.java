package com.wriesnig.githubapi;

public class GHUser {
    private String login;
    private String profile_image_url;
    private String name;
    private String html_url;
    private String website_url;

    public GHUser(String login, String profile_image_url, String name, String html_url, String website_url) {
        this.login = login;
        this.profile_image_url = profile_image_url;
        this.name = name;
        this.html_url = html_url;
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

    public String getHtml_url() {
        return html_url;
    }

    public String getWebsite_url() {
        return website_url;
    }
}
