package com.wriesnig.api.git;

public class GitUser {
    private String login;
    private String profileImageUrl;
    private String name;
    private String htmlUrl;
    private String websiteUrl;

    public GitUser(String login, String profileImageUrl, String name, String htmlUrl, String websiteUrl) {
        this.login = login;
        this.profileImageUrl = profileImageUrl;
        this.name = name;
        this.htmlUrl = htmlUrl;
        this.websiteUrl = websiteUrl;
    }

    public String getLogin() {
        return login;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }
}
