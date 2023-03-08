package com.wriesnig.db.expertise;

import com.wriesnig.expertise.Tags;
import com.wriesnig.expertise.User;
import com.wriesnig.utils.Logger;

import java.sql.*;
import java.util.HashMap;

public class ExpertiseDatabase {
    private static PreparedStatement insertUser;
    private static Connection connection;

    private static String user;
    private static String password;
    private static String url;
    private static boolean isCredentialsSet;

    public static void setCredentials(String user, String password, String url){
        ExpertiseDatabase.user = user;
        ExpertiseDatabase.password = password;
        ExpertiseDatabase.url = url;
        isCredentialsSet = true;
    }

    public static void initDB() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            String insertStmt = getInsertUserStatement();
            insertUser = connection.prepareStatement(insertStmt);
        } catch (SQLException e) {
            Logger.error("Accessing expertise-database failed.", e);
            throw new RuntimeException();
        }
    }

    public static String getInsertUserStatement() {
        StringBuilder tagsStringBuilder = new StringBuilder();

        for (String tag : Tags.tagsToCharacterize)
            tagsStringBuilder.append(",").append(tag);

        String tags = tagsStringBuilder.toString();
        StringBuilder statementStringBuilder = new StringBuilder();

        statementStringBuilder.append("INSERT INTO Users (stackId, stackDisplayName, gitLogin, profileImageUrl").append(tags).append(", time) VALUES (?,?,?,?");
        for(int i=1; i<=Tags.tagsToCharacterize.length;i++){
            statementStringBuilder.append(",").append("?");
        }
        statementStringBuilder.append(",?);");

        return statementStringBuilder.toString();
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            Logger.error("Closing expertise-database connection failed.", e);
        } catch (NullPointerException e){
            Logger.error("Expertise-database connection is null thus cannot be closed.", e);
        }
    }

    public static void insertUser(User user) {
        try {
            int index=1;
            insertUser.setInt(index++, user.getStackId());
            insertUser.setString(index++, user.getStackDisplayName());
            insertUser.setString(index++, user.getGitLogin());
            insertUser.setString(index++, user.getProfileImageUrl());

            HashMap<String, Double> expertise = user.getExpertise().getOverAllExpertise();
            for(String tag: Tags.tagsToCharacterize)
                insertUser.setDouble(index++, expertise.get(tag));

            insertUser.setTimestamp(index, new Timestamp(System.currentTimeMillis()));
            Logger.info(insertUser.toString());
            insertUser.executeUpdate();
        } catch (SQLException e) {
            Logger.error("Inserting user into expertise-database failed.", e);
        }

    }

    public static boolean isCredentialsSet(){
        return isCredentialsSet;
    }
}
