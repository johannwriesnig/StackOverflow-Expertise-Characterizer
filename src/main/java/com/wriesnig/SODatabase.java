package com.wriesnig;

import com.wriesnig.stackoverflowapi.SOUser;

import java.sql.*;
import java.util.ArrayList;

public class SODatabase {
    private static Connection db_connection;
    private static PreparedStatement get_posts_by_userid;
    private static String user;
    private static String password;
    private static String url;



    public static void initDB(String user, String password, String url) throws SQLException {
        db_connection = (DriverManager.getConnection(url, user, password));
        get_posts_by_userid = db_connection.prepareStatement("Select * FROM Posts" +
                " WHERE OwnerUserId=?;");
    }

    public static ResultSet getPostsFromUser(int user_id){
        ResultSet result_set;
        try {
            get_posts_by_userid.setInt(1, user_id);
            result_set = get_posts_by_userid.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result_set;
    }
}
