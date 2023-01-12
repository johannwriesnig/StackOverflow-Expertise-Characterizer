package com.wriesnig.expertise;

import com.wriesnig.utils.Logger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExpertiseDatabase {
    private static PreparedStatement insertUser;
    private static PreparedStatement getUsers;
    private static Connection connection;

    public static void initDB(String url, String user, String password) {
        try {
            connection = DriverManager.getConnection(url, user, password);
            insertUser = connection.prepareStatement("INSERT INTO Users VALUES");
            getUsers = connection.prepareStatement("SELECT * FROM Users");
        } catch (SQLException e) {
            Logger.error("Connection issues with ExpertiseDatabase in initDB(...)",e);
            throw new RuntimeException();
        }
    }

    public static void closeConnection(){
        try {
            connection.close();
        } catch (SQLException e) {
            Logger.error("Issues with closing ExpertiseDbConnection...", e);
        }
    }
}
