package com.wriesnig.sodumpsconvert;

import datainfo.PostsInfo;
import datainfo.UsersInfo;
import datainfo.VotesInfo;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class ConverterApplication {
    private SAXParserFactory factory;

    public ConverterApplication() {

    }

    public void run() {
        System.setProperty("jdk.xml.totalEntitySizeLimit", String.valueOf(Integer.MAX_VALUE));
        factory = SAXParserFactory.newInstance();

        try {
            new File("output").mkdirs();

            ConvertJob users_convert_job = new ConvertJob(new UsersInfo(), "C:\\Users\\43664\\Desktop\\dumps\\Users.xml", factory.newSAXParser());
            ConvertJob posts_convert_job = new ConvertJob(new PostsInfo(), "C:\\Users\\43664\\Desktop\\dumps\\Posts.xml", factory.newSAXParser());
            ConvertJob votes_convert_job = new ConvertJob(new VotesInfo(), "C:\\Users\\43664\\Desktop\\dumps\\Votes.xml", factory.newSAXParser());

            users_convert_job.convert();
            votes_convert_job.convert();
            posts_convert_job.convert();

        } catch (SAXException | ParserConfigurationException e) {
            System.out.println("Issues with ConverterApplication...");
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
