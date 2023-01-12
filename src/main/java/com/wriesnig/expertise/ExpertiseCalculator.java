package com.wriesnig.expertise;

import com.wriesnig.githubapi.GitApi;
import com.wriesnig.githubapi.GitUser;
import com.wriesnig.stackoverflow.api.StackUser;
import com.wriesnig.stackoverflow.db.StackDatabase;
import com.wriesnig.utils.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExpertiseCalculator {
    private ExpertiseCalculator() {
    }

    public static void computeExpertise(ArrayList<User> users) {
        Logger.info("Starting expertise computation...");
        computeSOExpertise(users);
        //users.add(new User(new StackUser(1,1,"","","","",1),new GitUser("johannwriesnig", "", "", "", "")));
        computeGHExpertise(users);
        storeUsersExpertise(users);
    }

    private static void computeSOExpertise(ArrayList<User> users) {
        startThreadedComputation(users, StackExpertiseJob.class, StackDatabase.getConnectionSize());
    }

    private static void computeGHExpertise(ArrayList<User> users) {
        File reposDir = new File("repos");
        reposDir.mkdirs();
        startThreadedComputation(users, GitExpertiseJob.class, 5);
        reposDir.delete();
    }

    private static void startThreadedComputation(ArrayList<User> users, Class<?> clazz, int threadPoolSize){
        if(!(clazz == StackExpertiseJob.class || clazz == GitExpertiseJob.class)) return;
        Logger.info("Starting expertise computation");
        double start = System.nanoTime();
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
        for(User user: users){
            if(clazz == StackExpertiseJob.class)executorService.execute(new StackExpertiseJob(user));
            else executorService.execute(new GitExpertiseJob(user));
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        double stop = System.nanoTime() - start;
        stop /= 1000000000.0;
        Logger.info("Computing took " + stop + " secs");
    }

    private static void storeUsersExpertise(ArrayList<User> users){
        for(User user: users){
            HashMap<String, Double> expertise = user.getExpertise().getFinalExpertise();
            expertise.forEach((key,value) -> {

            });
        }
        //store data into expertise database
    }
}
