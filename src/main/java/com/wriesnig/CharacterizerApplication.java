package com.wriesnig;


import com.wriesnig.expertise.ExpertiseCalculator;
import com.wriesnig.expertise.User;
import com.wriesnig.githubapi.GitUser;
import com.wriesnig.stackoverflow.api.StackUser;

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
        ids = new ArrayList<>(Arrays.asList(22656, 1144035, 6309, 157882, 29407));
        ids = new ArrayList<>(Arrays.asList(22656, 1144035, 6309, 157882));
        ids = new ArrayList<>(Arrays.asList(22656));
    }

    public void run() {
        ArrayList<Pair<StackUser, GitUser>> linkedAccounts = AccountsFetcher.fetchMatchingAccounts(ids);

        ArrayList<User> users = new ArrayList<>();
        for(Pair<StackUser, GitUser> matching_accounts: linkedAccounts){
            users.add(new User(matching_accounts.getKey(), matching_accounts.getValue()));
        }

        ExpertiseCalculator.computeExpertise(users);
        printExpertisePerUser(users);
    }


    public void printExpertisePerUser(ArrayList<User> users){

    }
}
