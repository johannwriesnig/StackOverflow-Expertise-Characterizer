package com.wriesnig;


import com.wriesnig.expertise.ExpertiseCalculator;
import com.wriesnig.expertise.User;
import com.wriesnig.githubapi.GHUser;
import com.wriesnig.stackoverflow.api.SOUser;

import com.wriesnig.utils.AccountsFetcher;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Application
 */
public class CharacterizerApplication {
    private ArrayList<Integer> ids;

    public CharacterizerApplication() {
        //Ids are top five so users: Jon Skeet, Gordon Linoff, Von C, BalusC, Darin Dimitrov
        ids = new ArrayList<>(Arrays.asList(6309));
        ids = new ArrayList<>(Arrays.asList(22656, 1144035, 6309, 157882, 29407));
    }

    public void run() {
        ArrayList<Pair<SOUser, GHUser>> linkedAccounts = AccountsFetcher.fetchMatchingAccounts(ids);
        printCurrentMatches(linkedAccounts);

        ArrayList<User> users = new ArrayList<>();
        for(Pair<SOUser, GHUser> matching_accounts: linkedAccounts){
            users.add(new User(matching_accounts.getKey(), matching_accounts.getValue()));
        }

        ExpertiseCalculator.computeExpertise(users);
        printExpertisePerUser(users);
    }

    public void printCurrentMatches(ArrayList<Pair<SOUser, GHUser>> linkedAccounts){
        for(Pair<SOUser, GHUser> pair: linkedAccounts){
            System.out.println("Matched pair -> SO: "+ pair.getKey().getDisplayName() + " GH: " + pair.getValue().getLogin());
        }
    }

    public void printExpertisePerUser(ArrayList<User> users){

    }
}
