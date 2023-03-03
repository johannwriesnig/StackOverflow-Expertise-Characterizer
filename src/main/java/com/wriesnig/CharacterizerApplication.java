package com.wriesnig;

import com.wriesnig.api.git.DefaultGitUser;
import com.wriesnig.api.git.GitUser;
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
        //22656,157882,139985,57695,203907,571407,922184,70604,1221571,276052,829571,21234,100297
        this.ids = ids;
    }

    public void run() {
        Logger.info("Running characterizer application.");
        StackDatabase.initDB();
        AccountsFetcher accountsFetcher = new AccountsFetcher();
        ArrayList<User> users = accountsFetcher.fetchMatchingAccounts(ids);
        users=usersForGitClassifier(users.get(0));
        runExpertiseJobs(users);
        storeUsersExpertise(users);
        notifyObservers(users);
    }

    public ArrayList<User> usersForGitClassifier(User user){
        ArrayList<User> users = new ArrayList<>();
        users.add(new User(user.getStackUser(),new GitUser("gdb","","","","")));
        users.add(new User(user.getStackUser(),new GitUser("hallacy","","","","")));
        users.add(new User(user.getStackUser(),new GitUser("rachellim","","","","")));
        users.add(new User(user.getStackUser(),new GitUser("ddeville","","","","")));
        users.add(new User(user.getStackUser(),new GitUser("BorisPower","","","","")));
        users.add(new User(user.getStackUser(),new GitUser("logankilpatrick","","","","")));
        users.add(new User(user.getStackUser(),new GitUser("sorinsuciu-msft","","","","")));
        users.add(new User(user.getStackUser(),new GitUser("lilianweng","","","","")));
        users.add(new User(user.getStackUser(),new GitUser("borisdayma","","","","")));
        users.add(new User(user.getStackUser(),new GitUser("madeleineth","","","","")));
        users.add(new User(user.getStackUser(),new GitUser("athyuttamre","","","","")));
        users.add(new User(user.getStackUser(),new GitUser("cmurtz-msft","","","","")));
        users.add(new User(user.getStackUser(),new GitUser("mpokrass","","","","")));


        return users;
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
