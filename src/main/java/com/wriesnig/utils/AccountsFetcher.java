package com.wriesnig.utils;

import com.wriesnig.githubapi.GitUser;
import com.wriesnig.githubapi.GitApi;
import com.wriesnig.stackoverflow.api.StackUser;
import com.wriesnig.stackoverflow.api.StackApi;
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
    public static ArrayList<Pair<StackUser, GitUser>> fetchMatchingAccounts(ArrayList<Integer> soIds){
        ArrayList<StackUser> stackUsers = fetchSOUsers(soIds);
        HashMap<StackUser, ArrayList<GitUser>> potentially_matching_accounts = fetchSOUsersWithPotentialGHUsers(stackUsers);

        return determineMatchingAccounts(potentially_matching_accounts);
    }

    private static ArrayList<StackUser> fetchSOUsers(ArrayList<Integer> soIds){
        StackApi stackoverflowApi = new StackApi();
        return stackoverflowApi.getUsers(soIds);
    }

    /**
     * Fetches possible GHUsers. Firstly GHUser with same login is stored, secondly User with same full_name
     */
    private static HashMap<StackUser, ArrayList<GitUser>> fetchSOUsersWithPotentialGHUsers(ArrayList<StackUser> stackUsers) {
        HashMap<StackUser, ArrayList<GitUser>> potentiallyMatchingAccounts = new HashMap<>();

        for (StackUser stackUser : stackUsers) {
            ArrayList<GitUser> potential_matches = fetchPotentialGHUsers(stackUser);
            potentiallyMatchingAccounts.put(stackUser, potential_matches);
        }

        return potentiallyMatchingAccounts;
    }

    /**
     * fetches potential GH Accounts based on Login, Full Name and linked Website
     * @param stackUser
     * @return
     */
    private static ArrayList<GitUser> fetchPotentialGHUsers(StackUser stackUser){
        GitApi githubApi = new GitApi();
        ArrayList<GitUser> potentialMatches = new ArrayList<>();
        GitUser gitUser;

        gitUser = githubApi.getUserByLogin(stackUser.getDisplayName());
        if (gitUser != null) potentialMatches.add(gitUser);

        ArrayList<String> fullNames = githubApi.getUsersByFullName(stackUser.getDisplayName());
        for (String login : fullNames) {
            gitUser = githubApi.getUserByLogin(login);
            if (!gitUser.getName().equals(stackUser.getDisplayName())) continue;
            potentialMatches.add(gitUser);
        }

        if(isGHUserLink(stackUser.getWebsiteUrl())){
            String login = getLoginFromGHUserLink(stackUser.getWebsiteUrl());
            gitUser = githubApi.getUserByLogin(login);
            potentialMatches.add(gitUser);
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
    private static ArrayList<Pair<StackUser, GitUser>> determineMatchingAccounts(HashMap<StackUser, ArrayList<GitUser>> potentiallyMatchingAccounts){
        ArrayList<Pair<StackUser, GitUser>> linkedAccounts = new ArrayList<>();
        for (StackUser so_user : potentiallyMatchingAccounts.keySet()) {
            Pair<GitUser, Double> highest_match = new Pair<>(null, -1.0);
            for (GitUser gh_user : potentiallyMatchingAccounts.get(so_user)) {
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
