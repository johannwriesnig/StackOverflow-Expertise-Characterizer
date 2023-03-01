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
    private static PreparedStatement selectUsersExisting;
    private static PreparedStatement selectPostss;


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
            selectUsersExisting = connection.prepareStatement("SELECT " +
                    "1 FROM Users WHERE id=?");
            selectPostss = connection.prepareStatement("""
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

    public static ResultSet getPostssFromUser(int userId){
        ResultSet resultSet;
        try {
            selectPostss.setInt(1, userId);
            resultSet = selectPostss.executeQuery();
        } catch (SQLException e) {
            Logger.error("Querying stack-database for posts from user failed.", e);
            throw new RuntimeException();
        }

        return resultSet;
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

    public static ResultSet getVotesOfPost(int postId){
        ResultSet resultSet;
        try {
            selectPostVotes.setInt(1, postId);
            resultSet = selectPostVotes.executeQuery();
        } catch (SQLException e) {
            Logger.error("Querying stack-database for votes of post failed.", e);
            throw new RuntimeException();
        }

        return resultSet;
    }

    public static boolean isIdExisting(int id){
        boolean isUserExisting;
        try {
            selectUsersExisting.setInt(1, id);
            ResultSet resultSet = selectUsersExisting.executeQuery();
            isUserExisting = resultSet.isBeforeFirst();
        } catch (SQLException e) {
            Logger.error("Querying stack-database for isUserExisting failed.", e);
            throw new RuntimeException();
        }

        return isUserExisting;
    }

    public static boolean isCredentialsSet(){
        return isCredentialsSet;
    }

}
