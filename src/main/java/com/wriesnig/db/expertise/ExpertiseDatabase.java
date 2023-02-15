package com.wriesnig.db.expertise;

import com.wriesnig.expertise.Expertise;
import com.wriesnig.expertise.Tags;
import com.wriesnig.expertise.User;
import com.wriesnig.utils.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ExpertiseDatabase {
    private static PreparedStatement insertUser;
    private static PreparedStatement deleteUser;
    private static PreparedStatement getUsers;
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
            getUsers = connection.prepareStatement("SELECT * FROM Users");
            deleteUser = connection.prepareStatement("DELETE FROM Users where id=?");
        } catch (SQLException e) {
            Logger.error("Connection issues with ExpertiseDatabase in initDB(...)", e);
            throw new RuntimeException();
        }
    }

    public static String getInsertUserStatement() {
        StringBuilder tagsStringBuilder = new StringBuilder();

        for (String tag : Tags.tagsToCharacterize)
            tagsStringBuilder.append(",").append(tag);

        String tags = tagsStringBuilder.toString();
        StringBuilder statementStringBuilder = new StringBuilder();

        statementStringBuilder.append("INSERT INTO Users (id, stackId, gitId, profileImageUrl").append(tags).append(") VALUES (?,?,?,?");
        for(int i=1; i<=Tags.tagsToCharacterize.length;i++){
            statementStringBuilder.append(",").append("?");
        }
        statementStringBuilder.append(");");

        return statementStringBuilder.toString();
    }

    public static void closeConnection() {
        if(connection==null)return;
        try {
            connection.close();
        } catch (SQLException e) {
            Logger.error("Issues with closing ExpertiseDbConnection...", e);
        }
    }

    public static void insertUser(User user) {
        try {
            deleteUser.setInt(1, user.getStackId());
            deleteUser.executeUpdate();

            insertUser.setInt(1, user.getStackId());
            insertUser.setString(2, user.getStackDisplayName());
            insertUser.setString(3, user.getGitLogin());
            insertUser.setString(4, user.getProfileImageUrl());

            HashMap<String, Double> expertise = user.getExpertise().getOverAllExpertise();
            int counter=5;
            for(String tag: Tags.tagsToCharacterize)
                insertUser.setDouble(counter++, expertise.get(tag));

            Logger.info(insertUser.toString());
            insertUser.executeUpdate();
        } catch (SQLException e) {
            Logger.error("Inserting into users failed", e);
        }

    }

    public static boolean isCredentialsSet(){
        return isCredentialsSet;
    }
}
