package com.wriesnig;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

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
        Thread thread = new Thread(new SOExpertiseJob(user, SODatabase.getConnectionPool().getDBConnection()));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }



    }

    private static void computeGHExpertise(ArrayList<User> users) {

    }
}
