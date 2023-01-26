package com.wriesnig;

import com.wriesnig.expertise.ExpertiseCalculator;
import com.wriesnig.expertise.User;
import com.wriesnig.githubapi.GitUser;
import com.wriesnig.stackoverflow.api.StackUser;
import com.wriesnig.utils.AccountsFetcher;
import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;

public class CharacterizerApplication {
    private ArrayList<Integer> ids;

    public CharacterizerApplication() {
        //Jon Skeet, Gordon Linoff, Von C, BalusC, Darin Dimitrov, Tomasz Nuzrkie(spring), jb nizet(spring), daniel roseman(django), chris pratt(django), davidism(flusk)
        ids = new ArrayList<>(Arrays.asList(22656));
        ids = new ArrayList<>(Arrays.asList(22656, 1144035, 6309, 157882, 29407, 605744, 571407, 104349, 104349, 654031, 400617));
        ids = new ArrayList<>(Arrays.asList(605744,654031,400617, 1663352));
        ids = new ArrayList<>(Arrays.asList(654031));
    }

    public void run() {
        ArrayList<Pair<StackUser, GitUser>> linkedAccounts = AccountsFetcher.fetchMatchingAccounts(ids);

        ArrayList<User> users = new ArrayList<>();
        for(Pair<StackUser, GitUser> matching_accounts: linkedAccounts){
            users.add(new User(matching_accounts.getKey(), matching_accounts.getValue()));
        }

        ExpertiseCalculator.computeExpertise(users);
    }

}
