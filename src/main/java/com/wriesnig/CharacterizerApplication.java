package com.wriesnig;

import com.wriesnig.githubapi.GHUser;
import com.wriesnig.githubapi.GitHubApi;
import com.wriesnig.stackoverflowapi.StackOverFlowApi;
import com.wriesnig.stackoverflowapi.SOUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CharacterizerApplication {
    private ArrayList<Integer> ids;
    private ArrayList<SOUser> so_users;
    private StackOverFlowApi stackoverflow_api;
    private GitHubApi github_api;
    private HashMap<SOUser, ArrayList<GHUser>> potentially_matching_accounts;


    public CharacterizerApplication(){
        ids = new ArrayList<>(Arrays.asList(22656, 2333));
        so_users = new ArrayList<>();
        stackoverflow_api = new StackOverFlowApi();
        github_api = new GitHubApi();
    }

    public void run(){
        initSOUserData();
        fetchPossibleGHUsers();
        findMatchingPairs();
    }
    public void findMatchingPairs(){

    }

    public void fetchPossibleGHUsers(){
        for(SOUser so_user: so_users){
            ArrayList<GHUser> potential_matches = new ArrayList<>();
            GHUser gh_user;
            gh_user = github_api.getUserByLogin(so_user.getDisplay_name());
            if(gh_user != null) potential_matches.add(gh_user);
            ArrayList<String> full_names = github_api.getUsersByFullName(so_user.getDisplay_name());
            for(String login: full_names){
                gh_user = github_api.getUserByLogin(login);
                potential_matches.add(gh_user);
            }

            potentially_matching_accounts.put(so_user, potential_matches);
        }
    }

    public void initSOUserData(){
        so_users = stackoverflow_api.getUsers(ids);
    }
}
