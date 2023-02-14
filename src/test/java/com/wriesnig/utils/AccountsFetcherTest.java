package com.wriesnig.utils;

import com.wriesnig.api.git.GitApi;
import com.wriesnig.api.git.GitUser;
import com.wriesnig.api.stack.StackApi;
import com.wriesnig.api.stack.StackUser;
import com.wriesnig.expertise.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AccountsFetcherTest {
    private AccountsFetcher accountsFetcher;


    private HashMap<StackUser, ArrayList<GitUser>> potentialMatches = new HashMap<>(){{
       put(new StackUser(0, 1, "user0", "", "https://github.com/user5", "", 0),
               new ArrayList<>(){{
                   add(new GitUser("user0", "", "user 0", "", ""));
                   add(new GitUser("randomLogin1", "", "user 0", "", ""));
                   add(new GitUser("randomLogin2", "", "user0", "", ""));
                   add(new GitUser("user5", "", "user0", "", ""));
               }});
        put(new StackUser(1, 1, "user1", "https://github.com/user1", "", "", 1),
                new ArrayList<>(){{
                    add(new GitUser("user1", "", "user 1", "https://github.com/user1", ""));
                    add(new GitUser("randomLogin3", "", "user 11", "", ""));
                    add(new GitUser("randomLogin4", "", "user1", "", ""));
                }});
        put(new StackUser(2, 1, "user2", "", "", "", 2),
                new ArrayList<>());
    }};


    @BeforeAll
    public static void deactivateLogger() {
        Logger.deactivatePrinting();
    }

    @BeforeEach
    public void setUp() {
        accountsFetcher = new AccountsFetcher();
    }

    @Test
    public void fetchMatchingAccounts(){
        Logger.deactivatePrinting();
        try (MockedStatic<GitApi> gitMock = Mockito.mockStatic(GitApi.class);
                MockedStatic<StackApi> stackApi = Mockito.mockStatic(StackApi.class)) {

            ArrayList<StackUser> users = new ArrayList<>(potentialMatches.keySet());
            ArrayList<Integer> ids = new ArrayList<>();
            for(StackUser user: users)
                ids.add(user.getId());

            stackApi.when(()->StackApi.getUsers(ids))
                    .thenReturn(new ArrayList<>(potentialMatches.keySet()));
            stackApi.when(()->StackApi.getMainTags(anyInt()))
                    .thenReturn(new ArrayList<>());

            for(ArrayList<GitUser> gitUsers: potentialMatches.values()){
                for(GitUser gitUser: gitUsers){
                    gitMock.when(()-> GitApi.getUserByLogin(gitUser.getLogin()))
                            .thenReturn(gitUser);
                }
            }

            for(StackUser user: potentialMatches.keySet()){
                String displayName = user.getDisplayName();

                ArrayList<String> fullNames = new ArrayList<>();
                for(GitUser gitUser: potentialMatches.get(user))
                    fullNames.add(gitUser.getLogin());
                gitMock.when(()-> GitApi.getUsersByFullName(displayName)).thenReturn(fullNames);
                if(potentialMatches.get(user).isEmpty()) gitMock.when(()-> GitApi.getUserByLogin(displayName)).thenReturn(null);
            }

            ArrayList<User> matchedAccounts = accountsFetcher.fetchMatchingAccounts(ids);
            for(User user: matchedAccounts){
                Optional<Map.Entry<StackUser, ArrayList<GitUser>>> entry = potentialMatches.entrySet().stream().filter(e->e.getKey().getId() == user.getStackId()).findFirst();
                assertTrue(entry.isPresent());
                StackUser stackUser = entry.get().getKey();
            }
        }
    }


    @Test
    public void isGitUserLink(){
        assertTrue(accountsFetcher.isGitUserLink("https://github.com/user123"));
        assertTrue(accountsFetcher.isGitUserLink("https://github.com/123user"));
        assertTrue(accountsFetcher.isGitUserLink("https://github.com/1us-er1"));
    }

    @Test
    public void isNotGitUserLink(){
        assertFalse(accountsFetcher.isGitUserLink("https://randomWebsite.com/user123"));
        assertFalse(accountsFetcher.isGitUserLink("https://github.de/123user"));
        assertFalse(accountsFetcher.isGitUserLink("https://github.com/-user123"));
        assertFalse(accountsFetcher.isGitUserLink("https://github.com/user123-"));
        assertFalse(accountsFetcher.isGitUserLink("https://github.com/user--123"));
    }

    @Test
    public void extractLoginFromGithubUserLink(){
        String link = "https://github.com/user123";
        String extractedUser = accountsFetcher.getLoginFromGitUserLink(link);
        assertEquals("user123", extractedUser);
    }


    @AfterEach
    public void tearDown() {
        accountsFetcher = null;
    }

}
