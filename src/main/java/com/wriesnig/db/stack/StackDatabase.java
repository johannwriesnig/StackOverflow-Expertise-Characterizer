package com.wriesnig.db.stack;

import java.sql.*;

public class StackDatabase {
    private static ConnectionPool connectionPool;
    private static final int connectionSize = 3;
    private static String user;
    private static String password;
    private static String url;
    private static boolean isCredentialsSet=false;

    private StackDatabase(){}


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

    public static void setCredentials(String user, String password, String url){
        StackDatabase.user = user;
        StackDatabase.password = password;
        StackDatabase.url = url;
        StackDatabase.isCredentialsSet=true;
    }

    public static void closeConnections(){
        if(connectionPool!=null)
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

    public static boolean isCredentialsSet(){
        return isCredentialsSet;
    }
    public static StackDbConnection getConnection(){
        if(connectionPool==null)
            connectionPool = new ConnectionPool(connectionSize, url, password, user);
        return connectionPool.getDBConnection();
    }

    public static void releaseConnection(StackDbConnection dbConnection){
        connectionPool.releaseDBConnection(dbConnection);
    }

    public static int getConnectionSize(){
        return connectionSize;
    }

}
