package com.wriesnig.stackoverflow.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StackDbConnection {
    private Connection connection;
    private PreparedStatement postsByUserId;
    private PreparedStatement votesByPostId;
    private PreparedStatement tagsByParentsId;

    public StackDbConnection(String url, String user, String password) {
        try {
            this.connection = DriverManager.getConnection(url, user, password);
            postsByUserId = connection.prepareStatement("SELECT * FROM Posts" +
                    " WHERE OwnerUserId=?;");
            votesByPostId = connection.prepareStatement("SELECT * FROM Votes" +
                    " WHERE PostId=?;");
            tagsByParentsId = connection.prepareStatement("SELECT * FROM Posts" +
                    " WHERE Id=?");
        } catch (SQLException e) {
            throw new RuntimeException("Connection issues to StackDatabase in StackDbConnection constructor", e);
        }
    }

    public PreparedStatement getPostsByUserId() {
        return postsByUserId;
    }

    public PreparedStatement getVotesByPostId() {
        return votesByPostId;
    }

    public PreparedStatement getTagsByParentsId() {
        return tagsByParentsId;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}
