package com.wriesnig;

import com.wriesnig.expertise.ExpertiseDatabase;
import com.wriesnig.stackoverflow.db.SODatabase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        //Wie UI aussieht wird noch entschieden
        //Deshalb erstmal mit ausgewählten Usern, hier sind es die Top SO-User
        //Still todo: error handling, code cleanen, api rates, vllt auf spring verzichten
        initDatabases();

        CharacterizerApplication application = new CharacterizerApplication();
        application.run();
    }

    public static void initDatabases() throws IOException, SQLException {
        Properties properties = new Properties();
        InputStream inputStream = new FileInputStream("config.properties");
        properties.load(inputStream);
        inputStream.close();

        initDumpsDatabase(properties);
        initExpertiseDatabase(properties);

    }

    public static void initDumpsDatabase(Properties properties) throws SQLException {
        String url = properties.getProperty("dumpsDB.url");
        String user = properties.getProperty("dumpsDB.user");
        String password = properties.getProperty("dumpsDB.password");
        SODatabase.initDB(user, password, url);
    }
    public static void initExpertiseDatabase(Properties properties) throws SQLException {
        String url = properties.getProperty("expertiseDB.url");
        String user = properties.getProperty("expertiseDB.user");
        String password = properties.getProperty("expertiseDB.password");
        ExpertiseDatabase.initDB(url, user, password);
    }
}