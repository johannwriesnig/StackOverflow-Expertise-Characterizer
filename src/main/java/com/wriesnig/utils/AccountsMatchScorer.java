package com.wriesnig.utils;

import com.wriesnig.api.git.GitUser;
import com.wriesnig.api.stack.StackUser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class AccountsMatchScorer {
    public static final double MATCHING_NAMES_SCORE = 0.4;
    public static final double MATCHING_IMAGES_SCORE = 0.4;
    public static final double MATCHING_LINKED_WEBSITES_SCORE = 0.2;
    public static final double NO_MATCH_SCORE = 0;

    private StackUser stackUser;
    private GitUser gitUser;

    public AccountsMatchScorer(StackUser stackUser, GitUser gitUser){
        this.gitUser = gitUser;
        this.stackUser = stackUser;
    }

    public double getMatchingScore() {
        double score = 0;
        score += getNameMatchingScore();
        score += getImageMatchingScore(stackUser, gitUser);
        score += getLinkedWebsiteMatchingScore();

        return (double)((int)(score*100))/100.0;
    }

    public double getNameMatchingScore() {
        return isNamesMatching() ? MATCHING_NAMES_SCORE : NO_MATCH_SCORE;
    }

    public boolean isNamesMatching() {
        return stackUser.getDisplayName().equals(gitUser.getLogin()) || stackUser.getDisplayName().equals(gitUser.getName());
    }

    public double getImageMatchingScore(StackUser stackUser, GitUser gitUser) {
        BufferedImage stackUserImage = getImageFromUrl(stackUser.getProfileImageUrl());
        BufferedImage gitUserImage = getImageFromUrl(gitUser.getProfileImageUrl());

        double images_difference = getImagesDissimilarity(stackUserImage, gitUserImage);

        return images_difference < 8 ? MATCHING_IMAGES_SCORE : NO_MATCH_SCORE;
    }

    public static BufferedImage getImageFromUrl(String imageUrl) {
        imageUrl = adaptUrlForStandardImageSize(imageUrl);
        return readImageFromUrl(imageUrl);
    }

    public static BufferedImage readImageFromUrl(String imageUrl){
        BufferedImage image = null;
        try {
            URL url = new URL(imageUrl);
            image = ImageIO.read(url);
        } catch (MalformedURLException e) {
            Logger.error("Profile image url is malformed -> " + imageUrl, e);
        } catch (IOException e) {
            Logger.error("I/O error while reading profile image from url.", e);
        }
        return image;
    }

    public static String adaptUrlForStandardImageSize(String imageUrl){
        if (imageUrl.contains("google"))
            imageUrl = imageUrl.replaceAll("=k-s\\d*", "=k-s256");
        else {
            imageUrl = imageUrl.replaceAll("&*s=\\d*", "");
            imageUrl = imageUrl + "&s=256";
        }
        return imageUrl;
    }

    public double getImagesDissimilarity(BufferedImage img1, BufferedImage img2) {
        if (img1 == null || img2 == null || img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight())
            return 100;
        int width1 = img1.getWidth();
        int height1 = img1.getHeight();

        long difference = 0;

        // treating images likely 2D matrix

        // Outer loop for rows(height)
        for (int y = 0; y < height1; y++) {

            // Inner loop for columns(width)
            for (int x = 0; x < width1; x++) {

                int rgbA = img1.getRGB(x, y);
                int rgbB = img2.getRGB(x, y);
                int redA = (rgbA >> 16) & 0xff;
                int greenA = (rgbA >> 8) & 0xff;
                int blueA = (rgbA) & 0xff;
                int redB = (rgbB >> 16) & 0xff;
                int greenB = (rgbB >> 8) & 0xff;
                int blueB = (rgbB) & 0xff;

                difference += Math.abs(redA - redB);
                difference += Math.abs(greenA - greenB);
                difference += Math.abs(blueA - blueB);
            }
        }

        // Total number of red pixels = width * height
        // Total number of blue pixels = width * height
        // Total number of green pixels = width * height
        // So total number of pixels = width * height *
        // 3
        double total_pixels = width1 * height1 * 3;

        // Normalizing the value of different pixels
        // for accuracy

        // Note: Average pixels per color component
        double avg_different_pixels
                = difference / total_pixels;

        // There are 255 values of pixels in total
        double percentage
                = (avg_different_pixels / 255) * 100;
        return percentage;
    }

    public double getLinkedWebsiteMatchingScore() {
        return isWebsitesMatching() ? MATCHING_LINKED_WEBSITES_SCORE : NO_MATCH_SCORE;
    }

    public boolean isWebsitesMatching() {
        return !stackUser.getWebsiteUrl().isEmpty() && !gitUser.getWebsiteUrl().isEmpty() && stackUser.getWebsiteUrl().equals(gitUser.getWebsiteUrl());
    }

}
