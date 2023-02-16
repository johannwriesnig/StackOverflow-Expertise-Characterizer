package com.wriesnig.db.stack;

import com.wriesnig.utils.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StackDbConnection {
    private final Connection connection;
    private final PreparedStatement postsByUserId;
    private final PreparedStatement votesByPostId;

    public StackDbConnection(String url, String user, String password) {
        try {
            this.connection = DriverManager.getConnection(url, user, password);
            postsByUserId = connection.prepareStatement("SELECT " +
                    "Post.ID, length(Post.Body) as bodyLength, ParentPost.Tags " +
                    "FROM Posts Post, Posts ParentPost " +
                    "WHERE Post.OwnerUserId=? AND Post.ParentId = ParentPost.Id;");

            votesByPostId = connection.prepareStatement("SELECT " +
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

    public PreparedStatement getPostsByUserId() {
        return postsByUserId;
    }

    public PreparedStatement getVotesByPostId() {
        return votesByPostId;
    }

    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            Logger.error("Closing stack-database connection failed.", e);
        }
    }
}
