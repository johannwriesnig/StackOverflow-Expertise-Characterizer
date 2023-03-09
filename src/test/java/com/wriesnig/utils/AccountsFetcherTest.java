package com.wriesnig.utils;

import com.wriesnig.api.git.DefaultGitUser;
import com.wriesnig.api.git.GitApi;
import com.wriesnig.api.git.GitUser;
import com.wriesnig.api.stack.StackApi;
import com.wriesnig.api.stack.StackUser;
import com.wriesnig.expertise.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountsFetcherTest {
    private AccountsFetcher accountsFetcher;
    private final String _4Potentials1Match = "user";
    private final String _2Potentials1Match = "user1";
    private final String gitUserInLink = "https://github.com/user6";
    private StackUser stackUser1;
    private StackUser stackUser2;
    private ArrayList<Integer> ids;
    private GitApi gitApi;
    private StackApi stackApi;

    private HashMap<StackUser, ArrayList<GitUser>> potentialMatches = new HashMap<>() {{
        put(new StackUser(0, 1, _4Potentials1Match, gitUserInLink, "", "", 0),
                new ArrayList<>() {{
                    add(new GitUser(_4Potentials1Match, "", "user 0", "", ""));
                    add(new GitUser("randomLogin1", "", "user 0", "", ""));
                    add(new GitUser("randomLogin2", "", "user0", "", ""));
                    add(new GitUser("user5", "", "user0", "", ""));
                }});
        put(new StackUser(1, 1, _2Potentials1Match, "https://same.com", "", "", 1),
                new ArrayList<>() {{
                    add(new GitUser(_2Potentials1Match, "", "user 1", "https://same.com", ""));
                    add(new GitUser("randomLogin3", "", "user 11", "", ""));
                }});
    }};


    @BeforeAll
    public static void deactivateLogger() {
        Logger.deactivatePrinting();
    }

    @BeforeEach
    public void setUp() {
        stackUser1 = new StackUser(1, 1, "user1", "", "","",1);
        stackUser2 = new StackUser(2, 1, "user2", "", "","",2);
        ids = new ArrayList<>();
        ids.add(stackUser1.getId());
        ids.add(stackUser2.getId());
        gitApi = mock(GitApi.class);
        stackApi = mock(StackApi.class);
        accountsFetcher = new AccountsFetcher();
    }

    @Test
    public void shouldCallHelperMethods() {
        accountsFetcher = mock(AccountsFetcher.class);
        doCallRealMethod().when(accountsFetcher).fetchMatchingAccounts(any());
        accountsFetcher.fetchMatchingAccounts(ids);
        verify(accountsFetcher, times(1)).getStackUsersFromStackApi(ids);
        verify(accountsFetcher, times(1)).getPotentialGitAccountsForStackUsers(any());
        verify(accountsFetcher, times(1)).matchAccounts(any());
    }


    @Test
    public void shouldReturnGitUsersForStackAccount(){
        StackUser stackUser = new StackUser(1, 1,"Jon Doe", "","","",1);
        GitUser gitUser = new GitUser("user1", "","Jon Doe","","");
        ArrayList<String> fullNames = new ArrayList<>();
        fullNames.add(gitUser.getName());
        try(MockedStatic<GitApi> gitApiMockedStatic = mockStatic(GitApi.class)){
            gitApiMockedStatic.when(GitApi::getInstance).thenReturn(gitApi);
            doReturn(gitUser).when(gitApi).getUserByLogin(gitUser.getName());
            doReturn(fullNames).when(gitApi).getUsersByFullName(stackUser.getDisplayName());
            accountsFetcher = new AccountsFetcher();
            ArrayList<GitUser> gitUsers = accountsFetcher.getPotentialGitAccountsByFullName(stackUser);
            assertEquals(1, gitUsers.size());
            assertEquals(gitUser, gitUsers.get(0));
            verify(gitApi, times(1)).getUserByLogin(gitUser.getName());
            verify(gitApi, times(1)).getUsersByFullName(stackUser.getDisplayName());
        }
    }


    @Test
    public void shouldReturnStackUsersWithSetMainTags(){
        ArrayList<StackUser> stackUsers = new ArrayList<>();
        String tag = "tag";
        ArrayList<String> tags = new ArrayList<>();
        tags.add(tag);
        stackUsers.add(stackUser1);
        stackUsers.add(stackUser2);
        try(MockedStatic<StackApi> stackApiMockedStatic = mockStatic(StackApi.class)){
            stackApiMockedStatic.when(StackApi::getInstance).thenReturn(stackApi);
            doReturn(stackUsers).when(stackApi).getUsers(ids);
            doReturn(tags).when(stackApi).getMainTags(anyInt());
            accountsFetcher = new AccountsFetcher();
            ArrayList<StackUser> users = accountsFetcher.getStackUsersFromStackApi(ids);
            assertEquals(stackUser1, users.get(0));
            assertEquals(stackUser2, users.get(1));
            for(StackUser stackUser: users)
                assertTrue(stackUser.getMainTags().contains(tag));
            verify(stackApi, times(1)).getUsers(ids);
            verify(stackApi, times(2)).getMainTags(anyInt());
        }
    }



    @Test
    public void shouldReturnStackUserWithPotentialGitUser() {
        try (MockedStatic<GitApi> gitApiMockedStatic = Mockito.mockStatic(GitApi.class)) {
            gitApiMockedStatic.when(GitApi::getInstance).thenReturn(gitApi);
            initGitApiMock();
            accountsFetcher = new AccountsFetcher();
            ArrayList<StackUser> stackApiReturnList = new ArrayList<>(potentialMatches.keySet());
            HashMap<StackUser, ArrayList<GitUser>> potentiallyMatchingAccounts = accountsFetcher.getPotentialGitAccountsForStackUsers(stackApiReturnList);

            for (StackUser stackUser : potentialMatches.keySet()) {
                verify(gitApi, times(1)).getUsersByFullName(stackUser.getDisplayName());
                if (stackUser.getDisplayName().equals(_2Potentials1Match) || stackUser.getDisplayName().equals(_4Potentials1Match))
                    verify(gitApi, times(2)).getUserByLogin(stackUser.getDisplayName());
            }
            verify(gitApi, times(1)).getUserByLogin(accountsFetcher.getLoginFromGitUserLink(gitUserInLink));

            for (StackUser stackUser : potentialMatches.keySet()) {
                assertTrue(potentiallyMatchingAccounts.containsKey(stackUser));
            }


        }
    }

    private void initGitApiMock() {
        for (ArrayList<GitUser> gitUserArrayList : potentialMatches.values()) {
            for (GitUser gitUser : gitUserArrayList) {
                doReturn(gitUser).when(gitApi).getUserByLogin(gitUser.getLogin());
            }
        }

        for (StackUser user : potentialMatches.keySet()) {
            String displayName = user.getDisplayName();

            ArrayList<String> fullNames = new ArrayList<>();
            for (GitUser gitUser : potentialMatches.get(user))
                fullNames.add(gitUser.getLogin());
            doReturn(fullNames).when(gitApi).getUsersByFullName(displayName);
            if (potentialMatches.get(user).isEmpty())
                doReturn(null).when(gitApi).getUserByLogin(displayName);

        }

    }


    @Test
    public void matchingAccountsReturnsPair() {
        HashMap<StackUser, ArrayList<GitUser>> hashMap = new HashMap<>();
        String name = "Jon";
        String website = "https://same.website.com";
        StackUser stackUser = new StackUser(0, 1, name, "", website, "", 0);
        GitUser gitUser1 = new GitUser("", "", "", "", "");
        GitUser gitUser2= new GitUser(name, "", "", "", website);
        GitUser gitUser3 = new GitUser("", "", "", "", "");
        hashMap.put(stackUser,
                new ArrayList<>() {{
                    add(gitUser1);
                    add(gitUser2);
                    add(gitUser3);
                }});

        try(MockedConstruction<AccountsMatchScorer> mockedScorer = mockConstruction(AccountsMatchScorer.class,
                (mock,context)->{
                    StackUser argStackUser = (StackUser) context.arguments().get(0);
                    GitUser argGitUser = (GitUser) context.arguments().get(1);
                    if(argStackUser == stackUser && argGitUser == gitUser2)
                        doReturn(AccountsMatchScorer.MATCHING_NAMES_SCORE + AccountsMatchScorer.MATCHING_LINKED_WEBSITES_SCORE).when(mock).getMatchingScore();
                    else
                        doReturn(AccountsMatchScorer.NO_MATCH_SCORE).when(mock).getMatchingScore();
                })){

            ArrayList<User> users = accountsFetcher.matchAccounts(hashMap);
            assertEquals(1, users.size());
            User user = users.get(0);

            assertSame(stackUser, user.getStackUser());
            assertSame(gitUser2, user.getGitUser());
        }
    }

    @Test
    public void shouldCreateDefaultGitUser() {
        HashMap<StackUser, ArrayList<GitUser>> hashMap = new HashMap<>();
        StackUser stackUser = new StackUser(0, 1, "Jon", "", "", "", 0);
        GitUser gitUser1 = new GitUser("", "", "", "", "");
        GitUser gitUser2= new GitUser("", "", "", "", "");
        GitUser gitUser3 = new GitUser("", "", "", "", "");
        hashMap.put(stackUser,
                new ArrayList<>() {{
                    add(gitUser1);
                    add(gitUser2);
                    add(gitUser3);
                }});

        try(MockedConstruction<AccountsMatchScorer> mockedScorer = mockConstruction(AccountsMatchScorer.class,
                (mock,context)->{
                        doReturn(AccountsMatchScorer.NO_MATCH_SCORE).when(mock).getMatchingScore();
                })){

            ArrayList<User> users = accountsFetcher.matchAccounts(hashMap);
            assertEquals(1, users.size());
            User user = users.get(0);

            assertSame(stackUser, user.getStackUser());
            assertTrue(user.getGitUser() instanceof DefaultGitUser);
        }
    }


    @Test
    public void testGitUserLinks() {
        assertTrue(accountsFetcher.isGitUserLink("https://github.com/user123"));
        assertTrue(accountsFetcher.isGitUserLink("https://github.com/123user"));
        assertTrue(accountsFetcher.isGitUserLink("https://github.com/1us-er1"));
    }

    @Test
    public void testNoGitUserLink() {
        assertFalse(accountsFetcher.isGitUserLink("https://randomWebsite.com/user123"));
        assertFalse(accountsFetcher.isGitUserLink("https://github.de/123user"));
        assertFalse(accountsFetcher.isGitUserLink("https://github.com/-user123"));
        assertFalse(accountsFetcher.isGitUserLink("https://github.com/user123-"));
        assertFalse(accountsFetcher.isGitUserLink("https://github.com/user--123"));
    }

    @Test
    public void shouldExtractLoginFromGithubUserLink() {
        String link = "https://github.com/user123";
        String extractedUser = accountsFetcher.getLoginFromGitUserLink(link);
        assertEquals("user123", extractedUser);
    }



    @AfterEach
    public void tearDown() {
        accountsFetcher = null;
    }

}
