package com.wriesnig;

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

        Properties properties = new Properties();
        InputStream inputStream = new FileInputStream("config.properties");
        properties.load(inputStream);
        inputStream.close();

        String soDbUrl = properties.getProperty("dumpsDB.url");
        String soDbUser = properties.getProperty("dumpsDB.user");
        String soDbPassword = properties.getProperty("dumpsDB.password");
        SODatabase.initDB(soDbUser, soDbPassword, soDbUrl);

        CharacterizerApplication application = new CharacterizerApplication();
        application.run();

    }
}