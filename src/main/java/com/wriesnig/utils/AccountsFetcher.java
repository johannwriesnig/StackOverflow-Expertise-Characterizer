package com.wriesnig.utils;

import com.wriesnig.githubapi.GHUser;
import com.wriesnig.githubapi.GitHubApi;
import com.wriesnig.stackoverflow.api.SOUser;
import com.wriesnig.stackoverflow.api.StackOverFlowApi;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * utility class to fetch so-accounts with their possible gh-account
 */
public class AccountsFetcher {
    private AccountsFetcher(){}

    /**
     * Based on a list of StackoverflowUsers this method returns all matching Stackoverflow- and GithubAccounts
     * @param soIds
     * @return
     */
    public static ArrayList<Pair<SOUser, GHUser>> fetchMatchingAccounts(ArrayList<Integer> soIds){
        ArrayList<SOUser> soUsers = fetchSOUsers(soIds);
        HashMap<SOUser, ArrayList<GHUser>> potentially_matching_accounts = fetchSOUsersWithPotentialGHUsers(soUsers);

        return determineMatchingAccounts(potentially_matching_accounts);
    }

    private static ArrayList<SOUser> fetchSOUsers(ArrayList<Integer> soIds){
        StackOverFlowApi stackoverflowApi = new StackOverFlowApi();
        return stackoverflowApi.getUsers(soIds);
    }

    /**
     * Fetches possible GHUsers. Firstly GHUser with same login is stored, secondly User with same full_name
     */
    private static HashMap<SOUser, ArrayList<GHUser>> fetchSOUsersWithPotentialGHUsers(ArrayList<SOUser> soUsers) {
        HashMap<SOUser, ArrayList<GHUser>> potentiallyMatchingAccounts = new HashMap<>();

        for (SOUser soUser : soUsers) {
            ArrayList<GHUser> potential_matches = fetchPotentialGHUsers(soUser);
            potentiallyMatchingAccounts.put(soUser, potential_matches);
        }

        return potentiallyMatchingAccounts;
    }

    /**
     * fetches potential GH Accounts based on Login, Full Name and linked Website
     * @param soUser
     * @return
     */
    private static ArrayList<GHUser> fetchPotentialGHUsers(SOUser soUser){
        GitHubApi githubApi = new GitHubApi();
        ArrayList<GHUser> potentialMatches = new ArrayList<>();
        GHUser ghUser;

        ghUser = githubApi.getUserByLogin(soUser.getDisplayName());
        if (ghUser != null) potentialMatches.add(ghUser);

        ArrayList<String> fullNames = githubApi.getUsersByFullName(soUser.getDisplayName());
        for (String login : fullNames) {
            ghUser = githubApi.getUserByLogin(login);
            if (!ghUser.getName().equals(soUser.getDisplayName())) continue;
            potentialMatches.add(ghUser);
        }

        if(isGHUserLink(soUser.getWebsiteUrl())){
            String login = getLoginFromGHUserLink(soUser.getWebsiteUrl());
            ghUser = githubApi.getUserByLogin(login);
            potentialMatches.add(ghUser);
        }

        return potentialMatches;
    }

    private static String getLoginFromGHUserLink(String url){
        String[] urlParts = url.split("/");
        return urlParts[urlParts.length-1];
    }

    private static boolean isGHUserLink(String link){
        boolean isLink= link.matches("https://github.com/[a-zA-Z0-9-]+");

        String login = getLoginFromGHUserLink(link);
        if(login.startsWith("-") || login.endsWith("-") || login.contains("--"))isLink=false;

        return isLink;
    }

    /**
     * Determines if so and gh accounts are matching
     * @param potentiallyMatchingAccounts
     * @return
     */
    private static ArrayList<Pair<SOUser, GHUser>> determineMatchingAccounts(HashMap<SOUser, ArrayList<GHUser>> potentiallyMatchingAccounts){
        ArrayList<Pair<SOUser, GHUser>> linkedAccounts = new ArrayList<>();
        for (SOUser so_user : potentiallyMatchingAccounts.keySet()) {
            Pair<GHUser, Double> highest_match = new Pair<>(null, -1.0);
            for (GHUser gh_user : potentiallyMatchingAccounts.get(so_user)) {
                double score = MatchingScorer.getMatchingScore(so_user, gh_user);
                if(score>highest_match.getValue()) highest_match = new Pair<>(gh_user, score);
                System.out.println("SO-User: " + so_user.getDisplayName() + "/GH-User: " + gh_user.getName() + " Score: " +
                        score);
            }
            if(highest_match.getValue()>0.5) linkedAccounts.add(new Pair<>(so_user, highest_match.getKey()));
        }
        return linkedAccounts;
    }

}
