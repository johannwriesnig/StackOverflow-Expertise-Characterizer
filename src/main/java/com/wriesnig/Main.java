package com.wriesnig;

import com.wriesnig.api.stack.StackApi;
import com.wriesnig.db.expertise.ExpertiseDatabase;
import com.wriesnig.db.stack.stackdumpsconvert.ConvertApplication;
import com.wriesnig.expertise.Tags;
import com.wriesnig.api.git.GitApi;
import com.wriesnig.db.stack.StackDatabase;
import com.wriesnig.gui.CharacterizerApplicationGui;
import com.wriesnig.utils.Logger;
import java.io.*;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        if(args.length!=1){
            Logger.error("Wrong arguments.");
            return;
        }

        if(args[0].equals("c")){
            ConvertApplication convertApplication = new ConvertApplication();
            convertApplication.run();
            return;
        }

        Logger.info("Setting properties.");
        Properties properties = getPropertiesFromConfigFile(args[0]);
        setGitApiToken(properties);
        setStackApiKey(properties);
        setTags(properties);
        setDbCredentials(properties);
        startApp();
        //closeDbConnections();
    }

    public static void setTags(Properties properties) {
        String tags = properties.getProperty("tags");
        Tags.tagsToCharacterize = tags.split(",");
    }

    public static void setGitApiToken(Properties properties) {
        String gitToken = properties.getProperty("git.token");
        GitApi.setToken(gitToken);
    }

    public static void setStackApiKey(Properties properties){
        String stackKey = properties.getProperty("stack.key");
        StackApi.setKey(stackKey);
    }

    public static void setDbCredentials(Properties properties) {
        setStackDbCredentials(properties);
        setExpertiseDbCredentials(properties);
    }

    public static void setStackDbCredentials(Properties properties) {
        String url = properties.getProperty("dumpsDB.url");
        String user = properties.getProperty("dumpsDB.user");
        String password = properties.getProperty("dumpsDB.password");
        StackDatabase.setCredentials(user, password, url);
    }

    public static void setExpertiseDbCredentials(Properties properties) {
        String url = properties.getProperty("expertiseDB.url");
        String user = properties.getProperty("expertiseDB.user");
        String password = properties.getProperty("expertiseDB.password");
        ExpertiseDatabase.setCredentials(user, password, url);
    }

    public static Properties getPropertiesFromConfigFile(String configFile) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream(configFile);
            properties.load(inputStream);
            inputStream.close();
        } catch (FileNotFoundException e) {
            Logger.error("Properties file not found.", e);
            throw new RuntimeException();
        } catch (IOException e) {
            Logger.error("I/O error while processing properties file.", e);
            throw new RuntimeException();
        }

        return properties;
    }

    public static void startApp(){
        CharacterizerApplicationGui characterizerApplicationGui = new CharacterizerApplicationGui();
    }

    public static void closeDbConnections(){
        StackDatabase.closeConnection();
        ExpertiseDatabase.closeConnection();
    }
}