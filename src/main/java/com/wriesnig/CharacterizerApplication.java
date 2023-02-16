package com.wriesnig;

import com.wriesnig.db.expertise.ExpertiseDatabase;
import com.wriesnig.expertise.ExpertiseCalculator;
import com.wriesnig.expertise.User;
import com.wriesnig.utils.AccountsFetcher;
import com.wriesnig.utils.Logger;

import java.util.ArrayList;
import java.util.Arrays;

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
        ExpertiseCalculator.computeExpertise(users);
        storeUsersExpertise(users);
    }

    public void storeUsersExpertise(ArrayList<User> users){
        ExpertiseDatabase.initDB();
        for(User user: users){
            ExpertiseDatabase.insertUser(user);
        }
    }

}
