package com.wriesnig.utils;

import com.wriesnig.api.git.DefaultGitUser;
import com.wriesnig.expertise.User;
import com.wriesnig.api.git.GitUser;
import com.wriesnig.api.git.GitApi;
import com.wriesnig.api.stack.StackUser;
import com.wriesnig.api.stack.StackApi;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.HashMap;


public class AccountsFetcher {
    private final AccountsMatchScorer accountsMatchScorer;

    public AccountsFetcher() {
        accountsMatchScorer = new AccountsMatchScorer();
    }

    public ArrayList<User> fetchMatchingAccounts(ArrayList<Integer> stackIds) {
        ArrayList<StackUser> stackUsers = StackApi.getUsers(stackIds);
        for (StackUser user : stackUsers)
            user.setMainTags(StackApi.getMainTags(user.getId()));
        HashMap<StackUser, ArrayList<GitUser>> potentially_matching_accounts = getStackUsersWithPotentialGitUsers(stackUsers);

        return matchAccounts(potentially_matching_accounts);
    }

    public HashMap<StackUser, ArrayList<GitUser>> getStackUsersWithPotentialGitUsers(ArrayList<StackUser> stackUsers) {
        HashMap<StackUser, ArrayList<GitUser>> potentiallyMatchingAccounts = new HashMap<>();

        for (StackUser stackUser : stackUsers) {
            ArrayList<GitUser> potential_matches = fetchPotentialGitUsers(stackUser);
            potentiallyMatchingAccounts.put(stackUser, potential_matches);
        }

        return potentiallyMatchingAccounts;
    }

    public ArrayList<GitUser> fetchPotentialGitUsers(StackUser stackUser) {
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

        if (isGitUserLink(stackUser.getWebsiteUrl())) {
            String login = getLoginFromGitUserLink(stackUser.getWebsiteUrl());
            gitUser = GitApi.getUserByLogin(login);
            potentialMatches.add(gitUser);
        }

        return potentialMatches;
    }

    public String getLoginFromGitUserLink(String url) {
        String[] urlParts = url.split("/");
        return urlParts[urlParts.length - 1];
    }

    public boolean isGitUserLink(String link) {
        boolean isLink = link.matches("https://github.com/[a-zA-Z0-9-]+");

        String login = getLoginFromGitUserLink(link);
        if (login.startsWith("-") || login.endsWith("-") || login.contains("--")) isLink = false;

        return isLink;
    }

    public ArrayList<User> matchAccounts(HashMap<StackUser, ArrayList<GitUser>> potentiallyMatchingAccounts) {
        ArrayList<Pair<StackUser, GitUser>> linkedAccounts = new ArrayList<>();
        for (StackUser stackUser : potentiallyMatchingAccounts.keySet()) {
            Pair<GitUser, Double> highest_match = new Pair<>(null, -1.0);
            for (GitUser gitUser : potentiallyMatchingAccounts.get(stackUser)) {
                double score = accountsMatchScorer.getMatchingScore(stackUser, gitUser);
                if (score > highest_match.getValue()) highest_match = new Pair<>(gitUser, score);
            }
            if (highest_match.getValue()-(AccountsMatchScorer.MATCHING_NAMES_SCORE+AccountsMatchScorer.MATCHING_LINKED_WEBSITES_SCORE) >= -0.001) {
                linkedAccounts.add(new Pair<>(stackUser, highest_match.getKey()));
                Logger.info("Matched " + stackUser.getDisplayName() + "/" + highest_match.getKey().getLogin() + " with score " + highest_match.getValue()+".");
            } else {
                linkedAccounts.add(new Pair<>(stackUser, new DefaultGitUser()));
                Logger.info("No github account could be found for " + stackUser.getDisplayName() + ".");
            }
        }

        ArrayList<User> users = new ArrayList<>();
        for (Pair<StackUser, GitUser> matching_accounts : linkedAccounts) {
            users.add(new User(matching_accounts.getKey(), matching_accounts.getValue()));
        }

        return users;
    }

}
