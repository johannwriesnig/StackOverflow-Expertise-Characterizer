package com.wriesnig;

import com.wriesnig.db.expertise.ExpertiseDatabase;
import com.wriesnig.db.stack.sodumpsconvert.ConvertApplication;
import com.wriesnig.expertise.Tags;
import com.wriesnig.api.git.GitApi;
import com.wriesnig.db.stack.StackDatabase;
import com.wriesnig.utils.Logger;

import java.io.*;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        if(args.length>1){
            Logger.error("Too many arguments");
            return;
        }

        if(args.length == 1 && args[0].equals("c")){
            Logger.info("Converting xml...");
            ConvertApplication convertApplication = new ConvertApplication();
            convertApplication.run();
            return;
        }

        Logger.info("Application initialization...");
        Properties properties = getPropertiesFromConfigFile();
        setGitApiToken(properties);
        setTags(properties);
        initDatabasesConnection(properties);


        CharacterizerApplication application = new CharacterizerApplication();
        application.run();
        closeDbConnections();
    }

    public static void setTags(Properties properties) {
        String tags = properties.getProperty("tags");
        Tags.tagsToCharacterize = tags.split(",");
    }

    public static void setGitApiToken(Properties properties) {
        String gitToken = properties.getProperty("git.token");
        GitApi.setToken(gitToken);
    }

    public static void closeDbConnections() {
        StackDatabase.closeConnections();
        ExpertiseDatabase.closeConnection();
    }

    public static void initDatabasesConnection(Properties properties) {
        initDumpsDatabase(properties);
        initExpertiseDatabase(properties);
    }

    public static void initDumpsDatabase(Properties properties) {
        String url = properties.getProperty("dumpsDB.url");
        String user = properties.getProperty("dumpsDB.user");
        String password = properties.getProperty("dumpsDB.password");
        StackDatabase.initDB(user, password, url);
        Logger.info("Created connection to stack-database");
    }

    public static void initExpertiseDatabase(Properties properties) {
        String url = properties.getProperty("expertiseDB.url");
        String user = properties.getProperty("expertiseDB.user");
        String password = properties.getProperty("expertiseDB.password");
        ExpertiseDatabase.initDB(url, user, password);
        Logger.info("Created connection to expertise-database");
    }

    public static Properties getPropertiesFromConfigFile() {
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream("config.properties");
            properties.load(inputStream);
            inputStream.close();
        } catch (FileNotFoundException e) {
            Logger.error("Issues with .properties file", e);
            throw new RuntimeException();
        } catch (IOException e) {
            Logger.error("Issues with IO operations concerning .properties file", e);
            throw new RuntimeException();
        }

        return properties;
    }
}