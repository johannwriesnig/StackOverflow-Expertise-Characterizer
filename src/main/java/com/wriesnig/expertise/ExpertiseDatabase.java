package com.wriesnig.expertise;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExpertiseDatabase {
    private static PreparedStatement insertUser;
    private static PreparedStatement getUsers;
    private static Connection connection;

    public static void initDB(String url, String user, String password) throws SQLException {
        connection = DriverManager.getConnection(url, user, password);
        insertUser = connection.prepareStatement("INSERT INTO Users VALUES");
        getUsers = connection.prepareStatement("SELECT * FROM Users");
    }
}
