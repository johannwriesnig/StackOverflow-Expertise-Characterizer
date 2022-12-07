package com.wriesnig.stackoverflow.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
    private Connection connection;
    private PreparedStatement postsByUserId;
    private PreparedStatement votesByPostId;
    private PreparedStatement tagsByParentsId;

    public DBConnection(Connection connection) throws SQLException {
        this.connection = connection;
        postsByUserId = connection.prepareStatement("SELECT * FROM Posts" +
                " WHERE OwnerUserId=?;");
        votesByPostId = connection.prepareStatement("SELECT * FROM Votes" +
                " WHERE PostId=?;");
        tagsByParentsId = connection.prepareStatement("SELECT * FROM Posts" +
                " WHERE Id=?");
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
