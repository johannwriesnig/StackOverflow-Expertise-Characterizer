package com.wriesnig.stackoverflow.db.sodumpsconvert;


import com.wriesnig.stackoverflow.db.sodumpsconvert.datainfo.PostsInfo;
import com.wriesnig.stackoverflow.db.sodumpsconvert.datainfo.UsersInfo;
import com.wriesnig.stackoverflow.db.sodumpsconvert.datainfo.VotesInfo;
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

            ConvertJob usersConvertJob = new ConvertJob(new UsersInfo(), "", factory.newSAXParser());
            ConvertJob postsConvertJob = new ConvertJob(new PostsInfo(), "", factory.newSAXParser());
            ConvertJob votesConvertJob = new ConvertJob(new VotesInfo(), "", factory.newSAXParser());

            usersConvertJob.convert();
            votesConvertJob.convert();
            postsConvertJob.convert();
        } catch (SAXException | ParserConfigurationException e) {
            System.out.println("Issues with ConverterApplication...");
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
