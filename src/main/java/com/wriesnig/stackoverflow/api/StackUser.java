package com.wriesnig.stackoverflow.api;

public class StackUser {
    private int id;
    private int reputation;
    private String displayName;
    private String websiteUrl;
    private String link;
    private String profileImageUrl;
    private int accountId;

    public StackUser(int id, int reputation, String displayName, String websiteUrl, String link, String profileImageUrl, int accountId) {
        this.id = id;
        this.reputation = reputation;
        this.displayName = displayName;
        this.websiteUrl = websiteUrl;
        this.link = link;
        this.profileImageUrl = profileImageUrl;
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "User: " + displayName;
    }

    public int getId() {
        return id;
    }

    public int getReputation() {
        return reputation;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getLink() {
        return link;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public int getAccountId() {
        return accountId;
    }
}
