package com.wriesnig;

import com.wriesnig.api.git.DefaultGitUser;
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
        ids = new ArrayList<>(Arrays.asList(1079354,2740539));
        ids = new ArrayList<>(Arrays.asList(22656,157882,139985,57695,203907,571407,922184,70604,1221571,276052,829571,21234,100297));
        ids = new ArrayList<>(Arrays.asList(100297));//,571407));
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
        StackDatabase.initDB();
        for (User user : users) {
            new StackExpertiseJob(user).run();
        }
    }

    private void runGitExpertiseJob(ArrayList<User> users) {
        Logger.info("Running git-expertise job.");
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (User user : users) {
            if(!(user.getGitUser() instanceof DefaultGitUser))executorService.execute(new GitExpertiseJob(user));
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Logger.error("Expertise thread was interrupted.", e);
        }
    }


    public void storeUsersExpertise(ArrayList<User> users){
        ExpertiseDatabase.initDB();
        for(User user: users){
            ExpertiseDatabase.insertUser(user);
        }
    }

}
