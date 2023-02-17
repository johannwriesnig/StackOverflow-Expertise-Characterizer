package com.wriesnig.db.stack.stackdumpsconvert;

import com.wriesnig.Main;
import com.wriesnig.db.stack.stackdumpsconvert.datainfo.DataInfo;
import com.wriesnig.db.stack.stackdumpsconvert.datainfo.PostsInfo;
import com.wriesnig.db.stack.stackdumpsconvert.datainfo.UsersInfo;
import com.wriesnig.expertise.User;
import com.wriesnig.utils.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.xml.sax.HandlerBase;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ConvertJobTest {
    private String dataPath = "src/main/resources/test/convertApp";
    private ConvertJob convertJob;
    private SAXParserFactory factory;

    @BeforeEach
    public void setUp() {
        ConvertApplication.dataPath = dataPath;
        System.setProperty("jdk.xml.totalEntitySizeLimit", String.valueOf(Integer.MAX_VALUE));
        factory = SAXParserFactory.newInstance();
    }

    @Test
    public void correctConvert() throws IOException, SAXException, ParserConfigurationException {
        convertJob = new ConvertJob(new PostsInfo(), dataPath + "/" + "xml/Posts.xml", factory.newSAXParser());
        convertJob.convert();
        String actualFileContent = Files.readString(Path.of(dataPath + "/csv/Posts.csv")).replaceAll("\\R+", ",");
        String expectedFileContent = Files.readString(Path.of(dataPath + "/csv/PostsToCompare.csv")).replaceAll("\\R+", ",");
        assertEquals(expectedFileContent, actualFileContent);
    }

    @Test
    public void convertButXmlDoesNotExist() throws IOException, SAXException {
        convertJob = new ConvertJob(null, "noSuchFile.txt", null);
        try (MockedStatic<Logger> mockedLogger = mockStatic(Logger.class)) {
            convertJob.convert();
            mockedLogger.verify(() -> Logger.error(any()), times(1));
        }
    }

    @Test
    public void writeAttributesFromInfo() throws ParserConfigurationException, SAXException, IOException {
        SAXParser saxParser = mock(SAXParser.class);
        UsersInfo dataInfo = new UsersInfo();
        convertJob = new ConvertJob(new UsersInfo(), dataPath + "/" + "xml/Users.xml", saxParser);
        convertJob.convert();
        verify(saxParser, times(1)).parse((File) any(), (XMLHandler) any());
        String fileContent = Files.readString(Path.of(dataPath + "/csv/Users.csv"));
        StringBuilder expectedAttributes = new StringBuilder();
        for (int i = 0; i < dataInfo.getDataAttributes().size(); i++) {
            expectedAttributes.append((dataInfo.getDataAttributes().get(i).getKey()));
            if (i != dataInfo.getDataAttributes().size() - 1) expectedAttributes.append(",");
        }
        expectedAttributes.append("\n");
        assertEquals(expectedAttributes.toString(), fileContent);
    }

    @Test
    public void measureTime() throws IOException, SAXException {
        try (MockedStatic<Logger> mockedLogger = mockStatic(Logger.class)){
            convertJob = new ConvertJob(new UsersInfo(), null, null);
            for(int i=0; i<=1000000; i++)
                convertJob.measureTime();
            mockedLogger.verify(() -> Logger.info(any()), times(1));
        }
    }

    @Test
    public void processRowThrowsRuntimeException(){
        try(MockedConstruction<ConvertJob> mockedConvertJob = mockConstruction(ConvertJob.class,
                (mock,context)-> {
                    doCallRealMethod().when(mock).processRow(any());
                    doThrow(IOException.class).when(mock).writeToCSV(any());
                })){
            convertJob = new ConvertJob(null, null, null);
            assertThrows(RuntimeException.class, ()-> convertJob.processRow(any()));
        }
    }


    @AfterEach
    public void tearDown() throws IOException {
        convertJob = null;
        factory = null;
        new FileWriter(dataPath + "/csv/Users.csv", false).close();
        new FileWriter(dataPath + "/csv/Posts.csv", false).close();
        new FileWriter(dataPath + "/csv/Votes.csv", false).close();
    }


}
