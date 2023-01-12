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
     * @param stackIds
     * @return
     */
    public static ArrayList<Pair<StackUser, GitUser>> fetchMatchingAccounts(ArrayList<Integer> stackIds){
        ArrayList<StackUser> stackUsers = fetchStackUsers(stackIds);
        HashMap<StackUser, ArrayList<GitUser>> potentially_matching_accounts = fetchStackUsersWithPotentialGitUsers(stackUsers);

        return determineMatchingAccounts(potentially_matching_accounts);
    }

    private static ArrayList<StackUser> fetchStackUsers(ArrayList<Integer> stackIds){
        return StackApi.getUsers(stackIds);
    }

    /**
     * Fetches possible GHUsers. Firstly GHUser with same login is stored, secondly User with same full_name
     */
    private static HashMap<StackUser, ArrayList<GitUser>> fetchStackUsersWithPotentialGitUsers(ArrayList<StackUser> stackUsers) {
        HashMap<StackUser, ArrayList<GitUser>> potentiallyMatchingAccounts = new HashMap<>();

        for (StackUser stackUser : stackUsers) {
            ArrayList<GitUser> potential_matches = fetchPotentialGitUsers(stackUser);
            potentiallyMatchingAccounts.put(stackUser, potential_matches);
        }

        return potentiallyMatchingAccounts;
    }

    /**
     * fetches potential GH Accounts based on Login, Full Name and linked Website
     * @param stackUser
     * @return
     */
    private static ArrayList<GitUser> fetchPotentialGitUsers(StackUser stackUser){
        ArrayList<GitUser> potentialMatches = new ArrayList<>();
        GitUser gitUser;

        gitUser = GitApi.getUserByLogin(stackUser.getDisplayName());
        if (gitUser != null) potentialMatches.add(gitUser);

        ArrayList<String> fullNames = GitApi.getUsersByFullName(stackUser.getDisplayName());
        for (String login : fullNames) {
            gitUser = GitApi.getUserByLogin(login);
            if (!gitUser.getName().equals(stackUser.getDisplayName())) continue;
            potentialMatches.add(gitUser);
        }

        if(isGitUserLink(stackUser.getWebsiteUrl())){
            String login = getLoginFromGitUserLink(stackUser.getWebsiteUrl());
            gitUser = GitApi.getUserByLogin(login);
            potentialMatches.add(gitUser);
        }

        return potentialMatches;
    }

    private static String getLoginFromGitUserLink(String url){
        String[] urlParts = url.split("/");
        return urlParts[urlParts.length-1];
    }

    private static boolean isGitUserLink(String link){
        boolean isLink= link.matches("https://github.com/[a-zA-Z0-9-]+");

        String login = getLoginFromGitUserLink(link);
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
        for (StackUser stackUser : potentiallyMatchingAccounts.keySet()) {
            Pair<GitUser, Double> highest_match = new Pair<>(null, -1.0);
            for (GitUser gitUser : potentiallyMatchingAccounts.get(stackUser)) {
                double score = MatchingScorer.getMatchingScore(stackUser, gitUser);
                if(score>highest_match.getValue()) highest_match = new Pair<>(gitUser, score);
            }
            if(highest_match.getValue()>0.5) {
                linkedAccounts.add(new Pair<>(stackUser, highest_match.getKey()));
                Logger.info("Matched accounts " + stackUser.getDisplayName() + "/" + highest_match.getKey().getLogin() + " with score of " + highest_match.getValue());
            }
        }
        return linkedAccounts;
    }

}
