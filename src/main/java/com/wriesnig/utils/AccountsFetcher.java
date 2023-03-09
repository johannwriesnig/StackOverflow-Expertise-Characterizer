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
    private GitApi gitApi;
    private StackApi stackApi;

    public AccountsFetcher(){
        gitApi = GitApi.getInstance();
        stackApi = StackApi.getInstance();
    }

    public ArrayList<User> fetchMatchingAccounts(ArrayList<Integer> stackIds) {
        ArrayList<StackUser> stackUsers = getStackUsersFromStackApi(stackIds);
        HashMap<StackUser, ArrayList<GitUser>> stackAccountsWithPotentialGitAccounts = getPotentialGitAccountsForStackUsers(stackUsers);
        return matchAccounts(stackAccountsWithPotentialGitAccounts);
    }

    public ArrayList<StackUser> getStackUsersFromStackApi(ArrayList<Integer> ids){
        ArrayList<StackUser> stackUsers = stackApi.getUsers(ids);
        for (StackUser user : stackUsers)
            user.setMainTags(stackApi.getMainTags(user.getId()));
        return stackUsers;
    }

    public HashMap<StackUser, ArrayList<GitUser>> getPotentialGitAccountsForStackUsers(ArrayList<StackUser> stackUsers) {
        HashMap<StackUser, ArrayList<GitUser>> potentiallyMatchingAccounts = new HashMap<>();

        for (StackUser stackUser : stackUsers) {
            ArrayList<GitUser> potentialGitAccountsForStackUser = getPotentialGitAccountsForSingleStackUser(stackUser);
            potentiallyMatchingAccounts.put(stackUser, potentialGitAccountsForStackUser);
        }

        return potentiallyMatchingAccounts;
    }

    public ArrayList<GitUser> getPotentialGitAccountsForSingleStackUser(StackUser stackUser) {
        ArrayList<GitUser> potentialGitAccounts = new ArrayList<>();
        potentialGitAccounts.add(gitApi.getUserByLogin(stackUser.getDisplayName()));
        potentialGitAccounts.addAll(getPotentialGitAccountsByFullName(stackUser));
        if(isGitUserLink(stackUser.getWebsiteUrl()))
            potentialGitAccounts.add(getGitUserFromWebsiteLink(stackUser));

        potentialGitAccounts.removeIf(user-> user instanceof DefaultGitUser);
        return potentialGitAccounts;
    }

    public ArrayList<GitUser> getPotentialGitAccountsByFullName(StackUser stackUser){
        ArrayList<GitUser> potentialGitAccounts = new ArrayList<>();
        GitUser gitUser;
        ArrayList<String> fullNames = gitApi.getUsersByFullName(stackUser.getDisplayName());
        for (String login : fullNames) {
            gitUser = gitApi.getUserByLogin(login);
            if (gitUser.getName().equals(stackUser.getDisplayName()))
                potentialGitAccounts.add(gitUser);
        }
        return potentialGitAccounts;
    }

    private GitUser getGitUserFromWebsiteLink(StackUser stackUser){
        String login = getLoginFromGitUserLink(stackUser.getWebsiteUrl());
        return gitApi.getUserByLogin(login);
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

    public ArrayList<User> matchAccounts(HashMap<StackUser, ArrayList<GitUser>> stackUsersWithPotentialGitAccounts) {
        ArrayList<Pair<StackUser, GitUser>> pairs = new ArrayList<>();
        for (StackUser stackUser : stackUsersWithPotentialGitAccounts.keySet()) {
            pairs.add(getMatchingStackAndGit(stackUser, stackUsersWithPotentialGitAccounts.get(stackUser)));
        }

        return getUsersFromPairs(pairs);
    }

    public Pair<StackUser,GitUser> getMatchingStackAndGit(StackUser stackUser, ArrayList<GitUser> potentialGitAccounts){
        Pair<GitUser, Double> highest_match = getHighestMatch(stackUser, potentialGitAccounts);
        //only match git account with stack user if the score is higher than 0.6
        if (highest_match.getValue()-(AccountsMatchScorer.MATCHING_NAMES_SCORE+AccountsMatchScorer.MATCHING_LINKED_WEBSITES_SCORE) >= -0.001) {
            Logger.info("Matched " + stackUser.getDisplayName() + "/" + highest_match.getKey().getLogin() + " with score " + highest_match.getValue()+".");
            return new Pair<>(stackUser, highest_match.getKey());
        } else {
            Logger.info("No github account could be found for " + stackUser.getDisplayName() + ".");
            return new Pair<>(stackUser, new DefaultGitUser());
        }

    }

    private Pair<GitUser, Double> getHighestMatch(StackUser stackUser, ArrayList<GitUser> potentialGitAccounts){
        Pair<GitUser, Double> highest_match = new Pair<>(null, -1.0);
        for (GitUser gitUser : potentialGitAccounts) {
            AccountsMatchScorer accountsMatchScorer = new AccountsMatchScorer(stackUser, gitUser);
            double score = accountsMatchScorer.getMatchingScore();
            if (score > highest_match.getValue()) highest_match = new Pair<>(gitUser, score);
        }
        return highest_match;
    }

    //Creating users from the matching accounts
    private ArrayList<User> getUsersFromPairs(ArrayList<Pair<StackUser, GitUser>> pairs){
        AccountsMatchScorer accountsMatchScorer = new AccountsMatchScorer(null, null);
        ArrayList<User> users = new ArrayList<>();
        for (Pair<StackUser, GitUser> matching_accounts : pairs) {
            StackUser stackUser = matching_accounts.getKey();
            GitUser gitUser = matching_accounts.getValue();
            stackUser.setProfileImage(accountsMatchScorer.getImageFromUrl(stackUser.getProfileImageUrl()));
            users.add(new User(stackUser, gitUser));
        }
        return users;
    }
}
