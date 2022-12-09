package com.wriesnig;

import com.wriesnig.expertise.ExpertiseDatabase;
import com.wriesnig.stackoverflow.db.StackDatabase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class Main {

    public static void main(String[] args){
        //Wie UI aussieht wird noch entschieden
        //Deshalb erstmal mit ausgewählten Usern, hier sind es die Top SO-User
        //Still todo: api rates, vllt auf spring verzichten
        initDatabases();

        CharacterizerApplication application = new CharacterizerApplication();
        application.run();
    }

    public static void initDatabases(){
        Properties properties = getPropertiesFromConfigFile();

        initDumpsDatabase(properties);
        initExpertiseDatabase(properties);

    }

    public static void initDumpsDatabase(Properties properties){
        String url = properties.getProperty("dumpsDB.url");
        String user = properties.getProperty("dumpsDB.user");
        String password = properties.getProperty("dumpsDB.password");
        StackDatabase.initDB(user, password, url);
    }
    public static void initExpertiseDatabase(Properties properties){
        String url = properties.getProperty("expertiseDB.url");
        String user = properties.getProperty("expertiseDB.user");
        String password = properties.getProperty("expertiseDB.password");
        ExpertiseDatabase.initDB(url, user, password);
    }

    public static Properties getPropertiesFromConfigFile(){
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream("config.properties");
            properties.load(inputStream);
            inputStream.close();
        } catch(FileNotFoundException e){
            throw new RuntimeException("Issues with .properties file",e);
        }
        catch (IOException e) {
            throw new RuntimeException("Issues with IO operations concerning .properties file",e);
        }

        return properties;
    }
}