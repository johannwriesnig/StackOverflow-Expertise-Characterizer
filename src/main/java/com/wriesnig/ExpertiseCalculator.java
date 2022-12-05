package com.wriesnig;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ExpertiseCalculator {
    private ExpertiseCalculator() {
    }

    public static void computeExpertise(ArrayList<User> users) {
        computeSOExpertise(users);
        computeGHExpertise(users);
    }

    private static void computeSOExpertise(ArrayList<User> users) {
        System.out.println("Fetching Posts from User...");
        User user = users.get(0);
        ResultSet result_set = SODatabase.getPostsFromUser(user.getSo_id());
        try {

            while (result_set.next()) {
                String id = result_set.getString("Body");
                System.out.println(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void computeGHExpertise(ArrayList<User> users) {

    }
}
