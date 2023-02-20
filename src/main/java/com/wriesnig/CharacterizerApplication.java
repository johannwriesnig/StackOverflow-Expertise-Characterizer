package com.wriesnig;

import com.wriesnig.db.expertise.ExpertiseDatabase;
import com.wriesnig.db.stack.StackDatabase;
import com.wriesnig.expertise.User;
import com.wriesnig.expertise.git.GitExpertiseJob;
import com.wriesnig.expertise.stack.StackExpertiseJob;
import com.wriesnig.utils.AccountsFetcher;
import com.wriesnig.utils.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CharacterizerApplication {
    private ArrayList<Integer> ids;
    private final AccountsFetcher accountsFetcher;

    public CharacterizerApplication() {
        //Jon Skeet, Gordon Linoff, Von C, BalusC, Darin Dimitrov, Tomasz Nuzrkie(spring), jb nizet(spring), daniel roseman(django), chris pratt(django), davidism(flusk)
        //ids = new ArrayList<>(Arrays.asList(22656, 1144035, 6309, 157882, 29407, 605744, 571407, 104349, 104349, 654031, 400617));
        ids = new ArrayList<>(Arrays.asList(605744, 654031, 400617, 1663352));
        ids = new ArrayList<>(Arrays.asList(22656));
        ids = new ArrayList<>(Arrays.asList(22656,157882,139985,57695,203907,571407,922184,70604,1221571,276052,829571,21234));
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
        //runGitExpertiseJob(users);
    }


    private void runStackExpertiseJob(ArrayList<User> users) {
        Logger.info("Running stack-expertise job.");
        StackDatabase.initDB();
        startThreadedComputation(users, StackExpertiseJob.class, 1);
    }

    private void runGitExpertiseJob(ArrayList<User> users) {
        Logger.info("Running git-expertise job.");
        startThreadedComputation(users, GitExpertiseJob.class, 5);
    }

    private void startThreadedComputation(ArrayList<User> users, Class<?> clazz, int threadPoolSize) {
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
            Logger.error("Expertise thread was interrupted.", e);
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
