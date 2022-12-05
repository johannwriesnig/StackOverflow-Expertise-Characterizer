package com.wriesnig;

import java.sql.*;

public class SODatabase {
    private static Connection dbConnection;
    private static PreparedStatement postsByUserId;
    private static String user;
    private static String password;
    private static String url;



    public static void initDB(String user, String password, String url) throws SQLException {
        dbConnection = (DriverManager.getConnection(url, user, password));
        postsByUserId = dbConnection.prepareStatement("Select * FROM Posts" +
                " WHERE OwnerUserId=?;");
    }

    public static ResultSet getPostsFromUser(int user_id){
        ResultSet result_set;
        try {
            postsByUserId.setInt(1, user_id);
            result_set = postsByUserId.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result_set;
    }
}
