package com.wriesnig.api.stack;

import java.util.ArrayList;

public class StackUser {
    private final int id;
    private final int reputation;
    private final String displayName;
    private final String websiteUrl;
    private final String link;
    private final String profileImageUrl;
    private final int accountId;
    private ArrayList<String> mainTags;

    public StackUser(int id, int reputation, String displayName, String websiteUrl, String link, String profileImageUrl, int accountId) {
        this.id = id;
        this.reputation = reputation;
        this.displayName = displayName;
        this.websiteUrl = websiteUrl;
        this.link = link;
        this.profileImageUrl = profileImageUrl;
        this.accountId = accountId;
        mainTags = new ArrayList<>();
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

    public void setMainTags(ArrayList<String> mainTags) {
        this.mainTags = mainTags;
    }

    public ArrayList<String> getMainTags() {
        return mainTags;
    }
}
