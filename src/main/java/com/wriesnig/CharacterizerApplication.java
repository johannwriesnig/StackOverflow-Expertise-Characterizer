package com.wriesnig;


import com.wriesnig.githubapi.GHUser;
import com.wriesnig.stackoverflowapi.SOUser;

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
    }

    public void run() {
        ArrayList<Pair<SOUser, GHUser>> linked_accounts = AccountsFetcher.fetchMatchingAccounts(ids);
        printCurrentMatches(linked_accounts);

        //computeSOExpertise
        //computeGHExpertise
    }

    public void printCurrentMatches(ArrayList<Pair<SOUser, GHUser>> linked_accounts){
        for(Pair<SOUser, GHUser> pair: linked_accounts){
            System.out.println("Matched pair -> SO: "+ pair.getKey().getDisplay_name() + " GH: " + pair.getValue().getLogin());
        }
    }
}
