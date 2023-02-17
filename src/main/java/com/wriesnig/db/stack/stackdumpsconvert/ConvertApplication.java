package com.wriesnig.db.stack.stackdumpsconvert;


import com.wriesnig.db.stack.stackdumpsconvert.datainfo.PostsInfo;
import com.wriesnig.db.stack.stackdumpsconvert.datainfo.UsersInfo;
import com.wriesnig.db.stack.stackdumpsconvert.datainfo.VotesInfo;
import com.wriesnig.utils.Logger;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class ConvertApplication {
    public static String dataPath = "db/dumpsDb";

    public void run() {
        System.setProperty("jdk.xml.totalEntitySizeLimit", String.valueOf(Integer.MAX_VALUE));
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            ConvertJob usersConvertJob = new ConvertJob(new UsersInfo(), dataPath + "/" + "xml/Users.xml", factory.newSAXParser());
            ConvertJob postsConvertJob = new ConvertJob(new PostsInfo(), dataPath + "/" + "xml/Posts.xml", factory.newSAXParser());
            ConvertJob votesConvertJob = new ConvertJob(new VotesInfo(), dataPath + "/" + "xml/Votes.xml", factory.newSAXParser());

            usersConvertJob.convert();
            votesConvertJob.convert();
            postsConvertJob.convert();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            Logger.error("Converting xml to csv failed.", e);
        }
    }
}
