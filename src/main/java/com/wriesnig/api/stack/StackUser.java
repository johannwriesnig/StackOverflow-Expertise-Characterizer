package com.wriesnig.api.stack;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class StackUser {
    private final int id;
    private final int reputation;
    private final int accountId;
    private String displayName;
    private String websiteUrl;
    private String link;
    private String profileImageUrl;
    private ArrayList<String> mainTags;
    private BufferedImage profileImage;

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

    public BufferedImage getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(BufferedImage profileImage) {
        this.profileImage = profileImage;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
