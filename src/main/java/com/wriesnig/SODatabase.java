package com.wriesnig;

import java.sql.*;
import java.sql.Connection;

public class SODatabase {
    private static ConnectionPool connectionPool;
    private static final int connectionSize = 3;
    private static String user;
    private static String password;
    private static String url;


    public static ResultSet getVotesOfPost(DBConnection dbConnection, int postId){
        PreparedStatement statement = dbConnection.getVotesByPostId();
        ResultSet resultSet;
        try {
            statement.setInt(1, postId);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultSet;
    }

    public static void initDB(String user, String password, String url) throws SQLException {
        connectionPool = new ConnectionPool(connectionSize, url, password, user);
    }

    public static String getTagsFromParentPost(DBConnection dbConnection, int parentId){
        PreparedStatement statement = dbConnection.getTagsByParentsId();
        String tags="";
        try {
            statement.setInt(1, parentId);
            ResultSet returnVal = statement.executeQuery();
            if(returnVal.next())tags=returnVal.getString("tags");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tags;
    }

    public static ResultSet getPostsFromUser(DBConnection dbConnection, int userId){
        PreparedStatement statement = dbConnection.getPostsByUserId();
        ResultSet resultSet;
        try {
            statement.setInt(1, userId);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultSet;
    }

    public static ConnectionPool getConnectionPool() {
        return connectionPool;
    }
}
