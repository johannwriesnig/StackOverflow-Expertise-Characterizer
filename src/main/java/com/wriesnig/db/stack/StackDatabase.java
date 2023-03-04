package com.wriesnig.db.stack;

import com.wriesnig.utils.Logger;

import java.sql.*;

public class StackDatabase {
    private static String user;
    private static String password;
    private static String url;
    private static boolean isCredentialsSet=false;

    private static Connection connection;
    private static PreparedStatement selectPosts;


    private StackDatabase(){}

    public static void initDB() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            selectPosts = connection.prepareStatement("""
                    SELECT p.Id,
                      SUM(CASE WHEN v.VoteTypeId = 1 THEN 1 ELSE 0 END) AS isAccepted,
                      SUM(CASE WHEN v.VoteTypeId = 2 THEN 1 ELSE 0 END) AS upVotes,
                      SUM(CASE WHEN v.VoteTypeId = 3 THEN 1 ELSE 0 END) AS downVotes,
                      parent.Tags AS tags
                    FROM Users u
                    JOIN Posts p ON u.Id = p.OwnerUserId
                    JOIN Posts parent ON p.ParentId = parent.Id
                    LEFT JOIN Votes v ON p.Id = v.PostId
                    WHERE u.Id = ?
                    GROUP BY p.Id, parent.Tags;""");
        } catch (SQLException e) {
            Logger.error("Accessing stack-database failed.", e);
            throw new RuntimeException();
        }
    }


    public static void setCredentials(String user, String password, String url){
        StackDatabase.user = user;
        StackDatabase.password = password;
        StackDatabase.url = url;
        StackDatabase.isCredentialsSet=true;
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

    public static ResultSet getPostsFromUser(int userId){
        ResultSet resultSet;
        try {
            selectPosts.setInt(1, userId);
            resultSet = selectPosts.executeQuery();
        } catch (SQLException e) {
            Logger.error("Querying stack-database for posts from user failed.", e);
            throw new RuntimeException();
        }

        return resultSet;
    }


    public static boolean isCredentialsSet(){
        return isCredentialsSet;
    }

}
