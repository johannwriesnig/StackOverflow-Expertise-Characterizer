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
    private static PreparedStatement selectPostVotes;


    private StackDatabase(){}

    public static void initDB() {
        try {
            connection = DriverManager.getConnection(url, user, password);
            selectPosts = connection.prepareStatement("SELECT " +
                    "Post.ID, ParentPost.Tags " +
                    "FROM Posts Post, Posts ParentPost " +
                    "WHERE Post.OwnerUserId=? AND Post.ParentId = ParentPost.Id;");
            selectPostVotes = connection.prepareStatement("SELECT " +
                    "(SELECT count(*) from Votes v1 " +
                    "where v1.PostId=p.id and v1.VoteTypeId=2) as upVotes, " +
                    "(SELECT count(*) from Votes v2 " +
                    "where v2.PostId=p.id and v2.VoteTypeId=3) as downVotes, " +
                    "(SELECT count(*) from Votes v3\n" +
                    "where v3.PostId=p.id and v3.VoteTypeId=1) as isAccepted " +
                    "from posts p  " +
                    "where id=?");
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
            throw new RuntimeException(e);
        }

        return resultSet;
    }

    public static ResultSet getVotesOfPost(int postId){
        ResultSet resultSet;
        try {
            selectPostVotes.setInt(1, postId);
            resultSet = selectPostVotes.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return resultSet;
    }

    public static boolean isCredentialsSet(){
        return isCredentialsSet;
    }

}
