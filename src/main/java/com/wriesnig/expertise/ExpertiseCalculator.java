package com.wriesnig.expertise;

import com.wriesnig.githubapi.GitApi;
import com.wriesnig.stackoverflow.db.StackDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExpertiseCalculator {
    private ExpertiseCalculator() {
    }

    public static void computeExpertise(ArrayList<User> users) {
        computeSOExpertise(users);
        computeGHExpertise(users);
        storeUsersExpertise(users);
    }

    private static void computeSOExpertise(ArrayList<User> users) {
        System.out.println("Compute So-Expertise...");
        double start = System.nanoTime();

        ExecutorService executorService = Executors.newFixedThreadPool(StackDatabase.getConnectionSize());
        for(User user: users){
            executorService.execute(new StackExpertiseJob(user));
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
        System.out.println("Compute Gh-Expertise...");
        double start = System.nanoTime();
        GitApi gitApi = new GitApi();

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(User user: users){
            executorService.execute(new GitExpertiseJob(user, gitApi));
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

    private static void storeUsersExpertise(ArrayList<User> users){
        for(User user: users){
            HashMap<String, Double> expertise = user.getExpertise().getFinalExpertise();
            expertise.forEach((key,value) -> {
                System.out.println(user.getStackDisplayName() + " has " + String.format("%.4f",value) + " for " + key);
            });
        }
        //store data into expertise database
    }
}
