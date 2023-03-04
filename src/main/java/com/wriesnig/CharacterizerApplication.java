package com.wriesnig;

import com.wriesnig.api.git.DefaultGitUser;
import com.wriesnig.db.expertise.ExpertiseDatabase;
import com.wriesnig.db.stack.StackDatabase;
import com.wriesnig.expertise.User;
import com.wriesnig.expertise.git.GitExpertiseJob;
import com.wriesnig.expertise.stack.StackExpertiseJob;
import com.wriesnig.gui.Observable;
import com.wriesnig.gui.Observer;
import com.wriesnig.utils.AccountsFetcher;
import com.wriesnig.utils.Logger;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CharacterizerApplication implements Observable {
    private ArrayList<Observer> observers = new ArrayList<>();
    private ArrayList<Integer> ids;


    public CharacterizerApplication(ArrayList<Integer> ids) {
        this.ids = ids;
    }

    public void run() {
        Logger.info("Running characterizer application.");
        StackDatabase.initDB();
        AccountsFetcher accountsFetcher = new AccountsFetcher();
        ArrayList<User> users = accountsFetcher.fetchMatchingAccounts(ids);
        runExpertiseJobs(users);
        storeUsersExpertise(users);
        notifyObservers(users);
    }

    public void runExpertiseJobs(ArrayList<User> users){
        Thread stack = new Thread(()->runStackExpertiseJobs(users));
        Thread git = new Thread(()->runGitExpertiseJobs(users));

        stack.start();
        git.start();

        try {
            stack.join();
            git.join();
        } catch (InterruptedException e) {
            Logger.error("Joining expertise job threads failed. ", e);
            throw new RuntimeException();
        }
    }


    public void runStackExpertiseJobs(ArrayList<User> users) {
        Logger.info("Running stack-expertise job.");
        for (User user : users) {
            new StackExpertiseJob(user).run();
        }
    }

    public void runGitExpertiseJobs(ArrayList<User> users) {
        Logger.info("Running git-expertise job.");
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (User user : users) {
            if(!(user.getGitUser() instanceof DefaultGitUser))executorService.execute(new GitExpertiseJob(user));
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(24L, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Logger.error("Git expertise thread was interrupted.", e);
            throw new RuntimeException();
        }
    }


    public void storeUsersExpertise(ArrayList<User> users){
        ExpertiseDatabase.initDB();
        for(User user: users){
            ExpertiseDatabase.insertUser(user);
        }
    }

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    public void notifyObservers(ArrayList<User> users){
        for (Observer observer : this.observers) {
            observer.notifyUpdate(users);
        }
    }
}
