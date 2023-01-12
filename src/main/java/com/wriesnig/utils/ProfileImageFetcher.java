package com.wriesnig.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ProfileImageFetcher {
    private ProfileImageFetcher(){}

    public static BufferedImage getImageFromUrl(String imageUrl){
        if(imageUrl.contains("google")) imageUrl = imageUrl.replaceAll("=k-s\\d*", "=k-s256");
        else {
            imageUrl = imageUrl.replaceAll("&*s=\\d*", "");
            imageUrl = imageUrl + "&s=256";
        }

        BufferedImage image=null;

        try {
            URL url = new URL(imageUrl);
            image = ImageIO.read(url);
        } catch (MalformedURLException e){
            Logger.error("Malformed url when fetching profile images -> " + imageUrl);
        } catch(IOException e){
            Logger.error("Issues reading image from " + imageUrl);
        }

        return image;
    }
}
