package com.wriesnig.db.stack.sodumpsconvert;


import com.wriesnig.db.stack.sodumpsconvert.datainfo.PostsInfo;
import com.wriesnig.db.stack.sodumpsconvert.datainfo.UsersInfo;
import com.wriesnig.db.stack.sodumpsconvert.datainfo.VotesInfo;
import com.wriesnig.utils.Logger;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class ConvertApplication {
    public static final String dataPath = "db/dumpsDb/";

    public ConvertApplication() {
    }

    public void run() {
        System.setProperty("jdk.xml.totalEntitySizeLimit", String.valueOf(Integer.MAX_VALUE));
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            ConvertJob usersConvertJob = new ConvertJob(new UsersInfo(), dataPath + "xml/Users.xml", factory.newSAXParser());
            ConvertJob postsConvertJob = new ConvertJob(new PostsInfo(), dataPath + "xml/Posts.xml", factory.newSAXParser());
            ConvertJob votesConvertJob = new ConvertJob(new VotesInfo(), dataPath + "xml/Votes.xml", factory.newSAXParser());

            usersConvertJob.convert();
            votesConvertJob.convert();
            postsConvertJob.convert();
        } catch (SAXException | ParserConfigurationException e) {
            Logger.error("Error while converting xml", e);
        } catch (IOException e) {
            Logger.error("IO Error while converting xml", e);
            throw new RuntimeException();
        }
    }
}