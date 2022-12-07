package com.wriesnig.expertise;

import com.wriesnig.githubapi.GitHubApi;
import com.wriesnig.stackoverflow.db.SODatabase;

import java.util.ArrayList;
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
        System.out.println("Compute Gh-Expertise...");
        double start = System.nanoTime();
        GitHubApi gitHubApi = new GitHubApi();

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(User user: users){
            executorService.execute(new GHExpertiseJob(user, gitHubApi));
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
        //store data into expertise database
    }
}
