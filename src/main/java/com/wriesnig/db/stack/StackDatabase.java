package com.wriesnig.db.stack;

import java.sql.*;

public class StackDatabase {
    private static ConnectionPool connectionPool;
    private static final int connectionSize = 3;
    private static String user;
    private static String password;
    private static String url;


    public static ResultSet getVotesOfPost(StackDbConnection stackDbConnection, int postId){
        PreparedStatement statement = stackDbConnection.getVotesByPostId();
        ResultSet resultSet;
        try {
            statement.setInt(1, postId);
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultSet;
    }

    public static void initDB(String user, String password, String url){
        connectionPool = new ConnectionPool(connectionSize, url, password, user);
    }

    public static void closeConnections(){
        connectionPool.closeConnections();
    }


    public static ResultSet getPostsFromUser(StackDbConnection stackDbConnection, int userId){
        PreparedStatement statement = stackDbConnection.getPostsByUserId();
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

    public static int getConnectionSize(){
        return connectionSize;
    }

}
