package com.wriesnig;

import com.wriesnig.githubapi.GHUser;
import com.wriesnig.stackoverflowapi.SOUser;

import java.awt.image.BufferedImage;

/**
 * Scores the matching between Stackoverflow- and GithubUser
 */
public class MatchScorer {
    private MatchScorer(){}

    public static double getMatchingScore(SOUser so_user, GHUser gh_user){
        double score=0;
        score+= getLocationMatchingScore(so_user.getLocation(), gh_user.getLocation());
        score+= getWebsiteMatchingScore(so_user.getWebsite_url(), gh_user.getWebsite_url());
        score+= getNameMatchingScore(so_user.getDisplay_name(), gh_user);
        score+= getImageMatchingScore(so_user.getProfile_image_url(), gh_user.getProfile_image_url());
        return score;
    }

    private static double getLocationMatchingScore(String so_user_location, String gh_user_location){
        double score=0;
        if(so_user_location.toLowerCase().contains(gh_user_location.toLowerCase()) || gh_user_location.toLowerCase().contains(so_user_location.toLowerCase()))
            score +=0.1;

        return score;
    }

    private static double getWebsiteMatchingScore(String so_user_website_url, String gh_user_website_url){
        double score=0;
        return score;
    }

    private static double getNameMatchingScore(String so_user_name, GHUser gh_user){
        double score=0;
        if(so_user_name.equals(gh_user.getLogin()) || so_user_name.equals(gh_user.getName()))
            score+=0.25;
        return score;
    }

    private static double getImageMatchingScore(String so_user_image_url, String gh_user_image_url){
        double score=0;
        BufferedImage so_user_image = ImageFetcher.getImageFromUrl(so_user_image_url);
        BufferedImage gh_user_image = ImageFetcher.getImageFromUrl(gh_user_image_url);

        double similarity_dif = compareImages(so_user_image, gh_user_image);
        if(similarity_dif<8) score+=0.25;

        return score;
    }

    private static double compareImages(BufferedImage img1, BufferedImage img2){
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


}
