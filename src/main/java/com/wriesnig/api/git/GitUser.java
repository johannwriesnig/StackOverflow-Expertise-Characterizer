package com.wriesnig.api.git;

public class GitUser {
    private final String login;
    private final String profileImageUrl;
    private final String name;
    private final String htmlUrl;
    private final String websiteUrl;

    public GitUser(String login, String profileImageUrl, String name, String htmlUrl, String websiteUrl) {
        this.login = login;
        this.profileImageUrl = profileImageUrl;
        this.name = name;
        this.htmlUrl = htmlUrl;
        this.websiteUrl = websiteUrl;
    }

    @Override
    public String toString() {
        return "GHUser: " + login;
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
}
