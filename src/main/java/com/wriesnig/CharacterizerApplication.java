package com.wriesnig;


import com.wriesnig.githubapi.GHUser;
import com.wriesnig.githubapi.GitHubApi;
import com.wriesnig.stackoverflowapi.StackOverFlowApi;
import com.wriesnig.stackoverflowapi.SOUser;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Application
 */
public class CharacterizerApplication {
    private ArrayList<Integer> ids;
    private ArrayList<SOUser> so_users;
    private StackOverFlowApi stackoverflow_api;
    private GitHubApi github_api;
    private HashMap<SOUser, ArrayList<GHUser>> potentially_matching_accounts;
    private ArrayList<Pair<SOUser, GHUser>> linked_accounts;


    public CharacterizerApplication() {
        //Ids are top five so users: Jon Skeet, Gordon Linoff, Von C, BalusC, Darin Dimitrov
        ids = new ArrayList<>(Arrays.asList(22656, 1144035, 6309, 157882, 29407));
        so_users = new ArrayList<>();
        stackoverflow_api = new StackOverFlowApi();
        github_api = new GitHubApi();
        potentially_matching_accounts = new HashMap<>();
        linked_accounts = new ArrayList<>();
    }


    public void run() {
        initSOUserData();
        System.out.println("this is the size " + so_users.size());
        fetchPossibleGHUsers();
        linkAccounts();

        printCurrentMatches();
    }

    public void printCurrentMatches(){
        for(Pair<SOUser, GHUser> pair: linked_accounts){
            System.out.println("Matched pair -> SO: "+ pair.getKey().getDisplay_name() + " GH: " + pair.getValue().getLogin());
        }
    }

    /**
     * Looking for similarity in Accounts and try to link them
     */
    public void linkAccounts() {
        for (SOUser so_user : potentially_matching_accounts.keySet()) {
            Pair<GHUser, Double> highest_match = new Pair<>(null, -1.0);
            for (GHUser gh_user : potentially_matching_accounts.get(so_user)) {
                double score = MatchScorer.getMatchingScore(so_user, gh_user);
                if(score>highest_match.getValue()) highest_match = new Pair<>(gh_user, score);
                System.out.println("SO-User: " + so_user.getDisplay_name() + "/GH-User: " + gh_user.getName() + " Score: " +
                        score);
            }
            if(highest_match.getValue()>0) linked_accounts.add(new Pair<>(so_user, highest_match.getKey()));
        }
    }



    /**
     * Fetches possible GHUsers. Firstly GHUser with same login is stored, secondly User with same full_name
     */
    public void fetchPossibleGHUsers() {
        for (SOUser so_user : so_users) {
            ArrayList<GHUser> potential_matches = new ArrayList<>();
            GHUser gh_user;
            gh_user = github_api.getUserByLogin(so_user.getDisplay_name());
            if (gh_user != null) potential_matches.add(gh_user);
            ArrayList<String> full_names = github_api.getUsersByFullName(so_user.getDisplay_name());
            for (String login : full_names) {
                gh_user = github_api.getUserByLogin(login);
                if (!gh_user.getName().equals(so_user.getDisplay_name())) continue;
                potential_matches.add(gh_user);
            }

            potentially_matching_accounts.put(so_user, potential_matches);
        }
    }

    /**
     * Initialises so_users based on static ids
     */
    public void initSOUserData() {
        so_users = stackoverflow_api.getUsers(ids);
    }
}
