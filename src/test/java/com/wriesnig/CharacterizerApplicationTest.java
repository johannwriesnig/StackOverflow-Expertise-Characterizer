package com.wriesnig;

import com.wriesnig.api.git.DefaultGitUser;
import com.wriesnig.api.git.GitUser;
import com.wriesnig.api.stack.StackUser;
import com.wriesnig.db.expertise.ExpertiseDatabase;
import com.wriesnig.db.stack.StackDatabase;
import com.wriesnig.expertise.User;
import com.wriesnig.expertise.git.GitExpertiseJob;
import com.wriesnig.expertise.stack.StackExpertiseJob;
import com.wriesnig.gui.CharacterizerApplicationGui;
import com.wriesnig.utils.AccountsFetcher;
import com.wriesnig.utils.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CharacterizerApplicationTest {
    private CharacterizerApplication characterizerApplication;
    private ArrayList<User> users;


    @BeforeAll
    public static void setUpBeforeAll() {
        Logger.deactivatePrinting();
    }


    @BeforeEach
    public void setUp() {
        users = new ArrayList<>();
        users.add(new User(new StackUser(1, 1, "", "", "", "", 1),
                new GitUser("gitUser1", "", "", "", "")));
        users.add(new User(new StackUser(2, 1, "", "", "", "", 2),
                new DefaultGitUser()));
        characterizerApplication = new CharacterizerApplication(null);
    }

    @Test
    public void shouldCallAllMethods() {
        try (MockedConstruction<AccountsFetcher> accountsFetcherMockedConstruction = getMockedAccountsFetcherThatReturnsUsers()){

            characterizerApplication = mock(CharacterizerApplication.class, withSettings().useConstructor((ArrayList)null));
            doCallRealMethod().when(characterizerApplication).run();
            characterizerApplication.run();

            AccountsFetcher accountsFetcher = accountsFetcherMockedConstruction.constructed().get(0);
            verify(accountsFetcher, times(1)).fetchMatchingAccounts(any());
            verify(characterizerApplication, times(1)).runExpertiseJobs(users);
            verify(characterizerApplication, times(1)).storeUsersExpertise(users);
            verify(characterizerApplication, times(1)).notifyObservers(users);
            verify(characterizerApplication, times(1)).initDBs();
            verify(characterizerApplication, times(1)).closeDBs();
            verify(characterizerApplication, times(1)).initDBs();
        }
    }

    private MockedConstruction<AccountsFetcher> getMockedAccountsFetcherThatReturnsUsers(){
        return mockConstruction(AccountsFetcher.class,
                (mock,context)->{
                    doReturn(users).when(mock).fetchMatchingAccounts(any());
                });
    }

    @Test
    public void shouldInitDbConnections(){
        try(MockedStatic<StackDatabase> stackDbMockedStatic = mockStatic(StackDatabase.class);
            MockedStatic<ExpertiseDatabase> expertiseDbMockedStatic = mockStatic(ExpertiseDatabase.class)){
            characterizerApplication.initDBs();
            stackDbMockedStatic.verify(StackDatabase::initDB, times(1));
            expertiseDbMockedStatic.verify(ExpertiseDatabase::initDB, times(1));
        }
    }

    @Test
    public void shouldCloseDbConnections(){
        try(MockedStatic<StackDatabase> stackDbMockedStatic = mockStatic(StackDatabase.class);
            MockedStatic<ExpertiseDatabase> expertiseDbMockedStatic = mockStatic(ExpertiseDatabase.class)){
            characterizerApplication.closeDBs();
            stackDbMockedStatic.verify(StackDatabase::closeConnection, times(1));
            expertiseDbMockedStatic.verify(ExpertiseDatabase::closeConnection, times(1));
        }
    }

    @Test
    public void shouldInsertUsersInExpertiseDb(){
        try (MockedStatic<ExpertiseDatabase> mockedExpertiseDb = mockStatic(ExpertiseDatabase.class)) {
            characterizerApplication.storeUsersExpertise(users);
            for (User user : users)
                mockedExpertiseDb.verify(() -> ExpertiseDatabase.insertUser(user), times(1));
        }
    }

    @Test
    public void shouldRunStackExpertiseJobs() {
        try (MockedConstruction<StackExpertiseJob> stackJobMockedConstruction = mockConstruction(StackExpertiseJob.class)) {
            characterizerApplication.runStackExpertiseJobs(users);
            assertEquals(users.size(), stackJobMockedConstruction.constructed().size());
            for(StackExpertiseJob job: stackJobMockedConstruction.constructed())
                verify(job, times(1)).run();
        }
    }

    @Test
    public void shouldCallExpertiseJobMethods(){
        characterizerApplication = mock(CharacterizerApplication.class, withSettings().useConstructor((ArrayList)null));
        doCallRealMethod().when(characterizerApplication).runExpertiseJobs(users);
        characterizerApplication.runExpertiseJobs(users);
        verify(characterizerApplication, times(1)).runGitExpertiseJobs(users);
        verify(characterizerApplication, times(1)).runStackExpertiseJobs(users);
    }

    @Test
    public void shouldThrowRunTimeExceptionWhenThreadInterrupted() throws InterruptedException {
        try (MockedConstruction<Thread> mockedThreads = getMockedThreadsThatThrowInterruptedException()) {
            assertThrows(RuntimeException.class, ()-> characterizerApplication.runExpertiseJobs(users));
            Thread stackThread = mockedThreads.constructed().get(0);
            verify(stackThread, times(1)).start();
            verify(stackThread, times(1)).join();
            Thread gitThread = mockedThreads.constructed().get(1);
            verify(gitThread, times(1)).start();
            verify(gitThread, times(0)).join();
        }
    }

    private MockedConstruction<Thread> getMockedThreadsThatThrowInterruptedException(){
        return mockConstruction(Thread.class,
                (mock,context)->{
                    doThrow(InterruptedException.class).when(mock).join();
                });
    }

    @Test
    public void shouldRunGitExpertiseJobsForUsersExceptDefaultOnes() {
        try (MockedConstruction<GitExpertiseJob> gitExpertiseJob = mockConstruction(GitExpertiseJob.class)) {
            characterizerApplication.runGitExpertiseJobs(users);
            assertEquals(1, gitExpertiseJob.constructed().size());
            verify(gitExpertiseJob.constructed().get(0), times(1)).run();
        }
    }

    @Test
    public void shouldNotifyObservers() {
            CharacterizerApplicationGui gui = mock(CharacterizerApplicationGui.class);

            characterizerApplication.addObserver(gui);
            characterizerApplication.notifyObservers(users);
            verify(gui, times(1)).notifyUpdate(users);
    }


    @Test
    public void shouldThrowRuntimeExceptionWhenExecutorInterrupts() throws InterruptedException {
        try (MockedConstruction<ThreadPoolExecutor> mockedExecutorService = getMockedExecutorServiceThatInterrupts()) {
            assertThrows(RuntimeException.class, () -> characterizerApplication.runGitExpertiseJobs(users));
            assertEquals(1, mockedExecutorService.constructed().size());
            ExecutorService executorService = mockedExecutorService.constructed().get(0);
            verify(executorService, times(1)).execute(any());
            verify(executorService, times(1)).shutdown();
            verify(executorService, times(1)).awaitTermination(anyLong(), any());
        }
    }



    public MockedConstruction<ThreadPoolExecutor> getMockedExecutorServiceThatInterrupts() {
        return mockConstruction(ThreadPoolExecutor.class,
                (mock, context) -> {
                    doThrow(InterruptedException.class).when(mock).awaitTermination(anyLong(), any());
                });
    }

    @AfterEach
    public void tearDown() {
        characterizerApplication = null;
    }
}
