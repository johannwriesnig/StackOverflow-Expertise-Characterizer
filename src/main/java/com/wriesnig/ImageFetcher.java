package com.wriesnig;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


public class ImageFetcher {
    private static int image_counter = 0;
    private static final String images_directory = "src/main/images/";

    private ImageFetcher(){}

    public static BufferedImage getImageFromUrl(String image_url){
        if(image_url.contains("google")) image_url = image_url.replaceAll("=k-s\\d*", "=k-s256");
        else {
            image_url = image_url.replaceAll("&*s=\\d*", "");
            image_url = image_url + "&s=256";
        }

        System.out.println("New Url: " + image_url);
        BufferedImage image=null;
        try {
            URL url = new URL(image_url);
            image = ImageIO.read(url);
        } catch (MalformedURLException e){
            System.out.println("Malformed Url...\n" + e);
        } catch(IOException e){
            System.out.println("File operation failed...\n" + e);
        }

        return image;
    }
}
