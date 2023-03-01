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

    @BeforeEach
    public void setUp() {
        Logger.deactivatePrinting();
        users = new ArrayList<>();
        users.add(new User(new StackUser(1,1,"", "","","",1),
                new GitUser("gitUser1", "", "", "","")));
        users.add(new User(new StackUser(2,1,"", "","","",2),
                new DefaultGitUser()));
    }

    @Test
    public void runIsCallingHelper(){
        try (MockedStatic<StackDatabase> mockedDb = mockStatic(StackDatabase.class);
             MockedConstruction<AccountsFetcher> mockedAccountsFetcher = mockConstruction(AccountsFetcher.class);
             MockedConstruction<CharacterizerApplication> mockedApp = mockConstruction(CharacterizerApplication.class,
                     (mock,context)->{
                        doCallRealMethod().when(mock).run();
                     })) {
            characterizerApplication = new CharacterizerApplication(null);
            characterizerApplication.run();
            assertEquals(1, mockedAccountsFetcher.constructed().size());
            AccountsFetcher createdFetcher = mockedAccountsFetcher.constructed().get(0);
            verify(createdFetcher, times(1)).fetchMatchingAccounts(any());
            verify(characterizerApplication, times(1)).runExpertiseJobs(any());
            verify(characterizerApplication, times(1)).storeUsersExpertise(any());
            verify(characterizerApplication, times(1)).notifyObservers(any());
        }
    }

    @Test
    public void runStackExpertiseJobForAllUsers(){
        try (MockedConstruction<StackExpertiseJob> stackExpertiseJob = getMockedStackExpertiseJob()) {

            characterizerApplication = new CharacterizerApplication(null);
            characterizerApplication.runStackExpertiseJobs(users);
            assertEquals(users.size(),stackExpertiseJob.constructed().size());
        }
    }

    @Test
    public void runGitExpertiseJobForAllUsersExceptDefaultOne(){
        try (MockedConstruction<GitExpertiseJob> gitExpertiseJob = getMockedGitExpertiseJob()) {

            characterizerApplication = new CharacterizerApplication(null);
            characterizerApplication.runGitExpertiseJobs(users);
            assertEquals(1,gitExpertiseJob.constructed().size());
        }
    }

    @Test
    public void runGitAndStackJobsThreaded() throws InterruptedException {
        try (MockedConstruction<Thread> mockedThreads = mockConstruction(Thread.class)) {

            characterizerApplication = new CharacterizerApplication(null);
            characterizerApplication.runExpertiseJobs(users);

            assertEquals(2, mockedThreads.constructed().size());
            for(Thread thread: mockedThreads.constructed()){
                verify(thread,times(1)).start();
                verify(thread,times(1)).join();
            }
        }
    }

    @Test
    public void insertAllUsers(){
        try (MockedStatic<ExpertiseDatabase> mockedExpertiseDb = mockStatic(ExpertiseDatabase.class)) {

            characterizerApplication = new CharacterizerApplication(null);
            characterizerApplication.storeUsersExpertise(users);
            for(User user: users)
                mockedExpertiseDb.verify(() -> ExpertiseDatabase.insertUser(user), times(1));
        }
    }

    @Test
    public void notifyingGui(){
        try (MockedConstruction<CharacterizerApplicationGui> mockedGui = mockConstruction(CharacterizerApplicationGui.class)) {
            CharacterizerApplicationGui gui = new CharacterizerApplicationGui();

            characterizerApplication = new CharacterizerApplication(null);
            characterizerApplication.addObserver(gui);
            characterizerApplication.notifyObservers(users);

            verify(gui, times(1)).notifyUpdate(users);
        }
    }

    @Test
    public void threadInterruptWhileJoinedThrowsRuntimeException() throws InterruptedException {
        try (MockedConstruction<Thread> mockedThreads = mockConstruction(Thread.class,
                     (mock, context)->{
                        doThrow(InterruptedException.class).when(mock).join();
                     })) {

            characterizerApplication = new CharacterizerApplication(null);
            assertThrows(RuntimeException.class, ()->characterizerApplication.runExpertiseJobs(users));
            assertEquals(2, mockedThreads.constructed().size());
            Thread firstThread = mockedThreads.constructed().get(0);
            Thread secondThread = mockedThreads.constructed().get(1);
            verify(firstThread, times(1)).start();
            verify(firstThread, times(1)).join();
            verify(secondThread, times(1)).start();
            verify(secondThread, times(0)).join();
        }
    }

    @Test
    public void threadInterruptWhileExecutorServiceAwait() throws InterruptedException {
        try (MockedConstruction<ThreadPoolExecutor> mockedExecutorService = mockConstruction(ThreadPoolExecutor.class,
                (mock, context)->{
                    doThrow(InterruptedException.class).when(mock).awaitTermination(anyLong(), any());
        })) {

            characterizerApplication = new CharacterizerApplication(null);
            assertThrows(RuntimeException.class, ()->characterizerApplication.runGitExpertiseJobs(users));
            assertEquals(1, mockedExecutorService.constructed().size());
            ExecutorService executorService = mockedExecutorService.constructed().get(0);
            verify(executorService, times(1)).execute(any());
            verify(executorService, times(1)).shutdown();
            verify(executorService, times(1)).awaitTermination(anyLong(), any());
        }
    }



    public MockedConstruction<StackExpertiseJob> getMockedStackExpertiseJob() {
        return mockConstruction(StackExpertiseJob.class,
                (mock, context) -> {
                    doNothing().when(mock).run();
                });
    }

    public MockedConstruction<GitExpertiseJob> getMockedGitExpertiseJob() {
        return mockConstruction(GitExpertiseJob.class,
                (mock, context) -> {
                    doNothing().when(mock).run();
                });
    }

    public MockedConstruction<ThreadPoolExecutor> getMockedExecutorService() {
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
