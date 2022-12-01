package com.wriesnig;

import com.wriesnig.githubapi.GHUser;
import com.wriesnig.githubapi.GitHubApi;
import com.wriesnig.stackoverflowapi.SOUser;
import com.wriesnig.stackoverflowapi.StackOverFlowApi;
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
     * @param so_ids
     * @return
     */
    public static ArrayList<Pair<SOUser, GHUser>> fetchMatchingAccounts(ArrayList<Integer> so_ids){
        ArrayList<SOUser> so_users = fetchSOUsers(so_ids);
        HashMap<SOUser, ArrayList<GHUser>> potentially_matching_accounts = fetchSOUsersWithPotentialGHUsers(so_users);

        return determineMatchingAccounts(potentially_matching_accounts);
    }

    private static ArrayList<SOUser> fetchSOUsers(ArrayList<Integer> so_ids){
        StackOverFlowApi stackoverflow_api = new StackOverFlowApi();
        return stackoverflow_api.getUsers(so_ids);
    }

    /**
     * Fetches possible GHUsers. Firstly GHUser with same login is stored, secondly User with same full_name
     */
    private static HashMap<SOUser, ArrayList<GHUser>> fetchSOUsersWithPotentialGHUsers(ArrayList<SOUser> so_users) {
        GitHubApi github_api = new GitHubApi();
        HashMap<SOUser, ArrayList<GHUser>> potentially_matching_accounts = new HashMap<>();

        for (SOUser so_user : so_users) {
            ArrayList<GHUser> potential_matches = fetchPotentialGHUsers(so_user);
            potentially_matching_accounts.put(so_user, potential_matches);
        }

        return potentially_matching_accounts;
    }

    /**
     * fetches potential GH Accounts based on Login, Full Name and linked Website
     * @param so_user
     * @return
     */
    private static ArrayList<GHUser> fetchPotentialGHUsers(SOUser so_user){
        GitHubApi github_api = new GitHubApi();
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

        if(isGHUserLink(so_user.getWebsite_url())){
            String[] url_parts = so_user.getWebsite_url().split("/");
            String login = getLoginFromGHUserLink(so_user.getWebsite_url());
            gh_user = github_api.getUserByLogin(login);
            potential_matches.add(gh_user);
        }

        return potential_matches;
    }

    private static String getLoginFromGHUserLink(String url){
        String[] url_parts = url.split("/");
        return url_parts[url_parts.length-1];
    }

    private static boolean isGHUserLink(String link){
        boolean isLink= link.matches("https://github.com/[a-zA-Z0-9-]+");

        String login = getLoginFromGHUserLink(link);
        if(login.startsWith("-") || login.endsWith("-") || login.contains("--"))isLink=false;

        return isLink;
    }

    /**
     * Determines if so and gh accounts are matching
     * @param potentially_matching_accounts
     * @return
     */
    private static ArrayList<Pair<SOUser, GHUser>> determineMatchingAccounts(HashMap<SOUser, ArrayList<GHUser>> potentially_matching_accounts){
        ArrayList<Pair<SOUser, GHUser>> linked_accounts = new ArrayList<>();
        for (SOUser so_user : potentially_matching_accounts.keySet()) {
            Pair<GHUser, Double> highest_match = new Pair<>(null, -1.0);
            for (GHUser gh_user : potentially_matching_accounts.get(so_user)) {
                double score = MatchingScorer.getMatchingScore(so_user, gh_user);
                if(score>highest_match.getValue()) highest_match = new Pair<>(gh_user, score);
                System.out.println("SO-User: " + so_user.getDisplay_name() + "/GH-User: " + gh_user.getName() + " Score: " +
                        score);
            }
            if(highest_match.getValue()>0.5) linked_accounts.add(new Pair<>(so_user, highest_match.getKey()));
        }
        return linked_accounts;
    }

}
