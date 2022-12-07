package com.wriesnig;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
        double start = System.nanoTime();

        ExecutorService executorService = Executors.newFixedThreadPool(SODatabase.getConnectionSize());
        for(User user: users){
            executorService.execute(new SOExpertiseJob(user));
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        double stop = System.nanoTime() - start;
        stop /= 1000000000.0;
        System.out.println("Computing took " + stop + " secs");

    }

    private static void computeGHExpertise(ArrayList<User> users) {

    }
}
