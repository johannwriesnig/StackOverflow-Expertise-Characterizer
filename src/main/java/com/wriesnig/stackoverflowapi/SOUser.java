package com.wriesnig.stackoverflowapi;

public class SOUser {
    private int id;
    private int reputation;
    private String display_name;
    private String website_url;
    private String location;
    private String profile_image_url;
    private int account_id;

    public SOUser(int id, int reputation, String display_name, String website_url, String location, String profile_image_url, int account_id) {
        this.id = id;
        this.reputation = reputation;
        this.display_name = display_name;
        this.website_url = website_url;
        this.location = location;
        this.profile_image_url = profile_image_url;
        this.account_id = account_id;
    }

    @Override
    public String toString() {
        return "User: " + display_name;
    }
}
