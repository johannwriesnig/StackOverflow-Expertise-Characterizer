package com.wriesnig.utils;

import com.wriesnig.githubapi.GHUser;
import com.wriesnig.stackoverflow.api.SOUser;

import java.awt.image.BufferedImage;

/**
 * Scores the matching between Stackoverflow- and GithubUser
 */
public class MatchingScorer {
    private static final double MATCHING_NAMES_SCORE = 0.4;
    private static final double MATCHING_IMAGES_SCORE = 0.4;
    private static final double MATCHING_LINKED_WEBSITES_SCORE = 0.2;
    private static final double NO_MATCH_SCORE = 0;
    private MatchingScorer(){}

    public static double getMatchingScore(SOUser soUser, GHUser ghUser){
        double score=0;
        score+= getNameMatchingScore(soUser.getDisplayName(), ghUser);
        score+= getImageMatchingScore(soUser.getProfileImageUrl(), ghUser.getProfileImageUrl());
        score+= getLinkedWebsiteMatchingScore(soUser, ghUser);
        return score;
    }

    private static double getNameMatchingScore(String soUserName, GHUser ghUser){
        return isNamesMatching(soUserName, ghUser)? MATCHING_NAMES_SCORE : NO_MATCH_SCORE;
    }

    private static boolean isNamesMatching(String soUserName, GHUser ghUser){
        return soUserName.equals(ghUser.getLogin()) || soUserName.equals(ghUser.getName());
    }

    private static double getImageMatchingScore(String so_user_image_url, String gh_user_image_url){
        BufferedImage so_user_image = ProfileImageFetcher.getImageFromUrl(so_user_image_url);
        BufferedImage gh_user_image = ProfileImageFetcher.getImageFromUrl(gh_user_image_url);

        double images_difference = getImagesDissimilarity(so_user_image, gh_user_image);

        return images_difference<8? MATCHING_IMAGES_SCORE : NO_MATCH_SCORE;
    }

    private static double getImagesDissimilarity(BufferedImage img1, BufferedImage img2){
        if(img1 == null || img2 == null)return -1;
        int width1 = img1.getWidth();
        int width2 = img2.getWidth();
        int height1 = img1.getHeight();
        int height2 = img2.getHeight();

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
                int blueA = (rgbA)&0xff;
                int redB = (rgbB >> 16) & 0xff;
                int greenB = (rgbB >> 8) & 0xff;
                int blueB = (rgbB)&0xff;

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

    private static double getLinkedWebsiteMatchingScore(SOUser soUser, GHUser ghUser){
        return isWebsitesMatching(soUser, ghUser)? MATCHING_LINKED_WEBSITES_SCORE : NO_MATCH_SCORE;
    }

    private static boolean isWebsitesMatching(SOUser soUser, GHUser ghUser){
        return soUser.getLink().equals(ghUser.getWebsiteUrl()) || soUser.getWebsiteUrl().equals(ghUser.getHtmlUrl());
    }

}
