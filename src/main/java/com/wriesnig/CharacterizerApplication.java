package com.wriesnig;

import com.wriesnig.db.expertise.ExpertiseDatabase;
import com.wriesnig.db.stack.StackDatabase;
import com.wriesnig.expertise.User;
import com.wriesnig.expertise.git.GitExpertiseJob;
import com.wriesnig.expertise.stack.StackExpertiseJob;
import com.wriesnig.utils.AccountsFetcher;
import com.wriesnig.utils.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CharacterizerApplication {
    private final ArrayList<Integer> ids;
    private final AccountsFetcher accountsFetcher;

    public CharacterizerApplication() {
        //Jon Skeet, Gordon Linoff, Von C, BalusC, Darin Dimitrov, Tomasz Nuzrkie(spring), jb nizet(spring), daniel roseman(django), chris pratt(django), davidism(flusk)
        //ids = new ArrayList<>(Arrays.asList(22656, 1144035, 6309, 157882, 29407, 605744, 571407, 104349, 104349, 654031, 400617));
        ids = new ArrayList<>(Arrays.asList(605744, 654031, 400617, 1663352));

        accountsFetcher = new AccountsFetcher();
    }

    public void run() {
        Logger.info("Running characterizer application.");
        ArrayList<User> users = accountsFetcher.fetchMatchingAccounts(ids);
        runExpertiseJobs(users);
        storeUsersExpertise(users);
    }

    public void runExpertiseJobs(ArrayList<User> users){
        runStackExpertiseJob(users);
        runGitExpertiseJob(users);
    }


    private void runStackExpertiseJob(ArrayList<User> users) {
        Logger.info("Running stack-expertise job.");
        startThreadedComputation(users, StackExpertiseJob.class, StackDatabase.getConnectionSize());
    }

    private void runGitExpertiseJob(ArrayList<User> users) {
        Logger.info("Running git-expertise job.");
        startThreadedComputation(users, GitExpertiseJob.class, 5);
    }

    private void startThreadedComputation(ArrayList<User> users, Class<?> clazz, int threadPoolSize) {
        if (!(clazz == StackExpertiseJob.class || clazz == GitExpertiseJob.class)) return;
        double start = System.nanoTime();
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
        for (User user : users) {
            if (clazz == StackExpertiseJob.class) executorService.execute(new StackExpertiseJob(user));
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
        Logger.info("Expertise-characterization took " + stop + " seconds.");
    }

    public void storeUsersExpertise(ArrayList<User> users){
        ExpertiseDatabase.initDB();
        for(User user: users){
            ExpertiseDatabase.insertUser(user);
        }
    }

}
