package com.wriesnig;

import com.wriesnig.expertise.User;
import com.wriesnig.expertise.git.GitExpertiseJob;
import com.wriesnig.expertise.stack.StackExpertiseJob;
import com.wriesnig.utils.AccountsFetcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedConstruction;
import java.util.ArrayList;
import java.util.concurrent.ThreadPoolExecutor;
import static org.mockito.Mockito.*;

public class CharacterizerApplicationTest {
    private CharacterizerApplication characterizerApplication;

    @BeforeEach
    public void setUp() {
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(1);
        characterizerApplication = new CharacterizerApplication(ids);
    }

    /*
    @Test
    public void run() {
        try (MockedConstruction<AccountsFetcher> accountsFetcher = getMockedAccountsFetcher();
             MockedConstruction<GitExpertiseJob> gitExpertiseJob = getMockedGitExpertiseJob();
             MockedConstruction<StackExpertiseJob> stackExpertiseJob = getMockedStackExpertiseJob();
             MockedStatic<ExpertiseDatabase> mockedExpertiseDb = mockStatic(ExpertiseDatabase.class);
             MockedStatic<StackDatabase> mockedStackDb = mockStatic(StackDatabase.class);) {
            characterizerApplication = new CharacterizerApplication();
            characterizerApplication.run();
            AccountsFetcher fetcherInstance = accountsFetcher.constructed().get(0);
            verify(fetcherInstance, times(1)).fetchMatchingAccounts(any());
            assertNotEquals(0, gitExpertiseJob.constructed().size());
            assertNotEquals(0, stackExpertiseJob.constructed().size());
            mockedExpertiseDb.verify(ExpertiseDatabase::initDB, times(1));
            mockedExpertiseDb.verify(() -> ExpertiseDatabase.insertUser(any()), times(2));
        }
    }

    @Test
    public void executorServiceAwaitFails() {
        try (MockedConstruction<AccountsFetcher> accountsFetcher = getMockedAccountsFetcher();
             MockedConstruction<ThreadPoolExecutor> executorService = getMockedExecutorService();
             MockedStatic<ExpertiseDatabase> mockedExpertiseDb = mockStatic(ExpertiseDatabase.class);
             MockedStatic<Logger> mockedLogger = mockStatic(Logger.class);
             MockedStatic<StackDatabase> mockedStackDb = mockStatic(StackDatabase.class);) {

            characterizerApplication = new CharacterizerApplication();
        }
    }
*/

    public MockedConstruction<AccountsFetcher> getMockedAccountsFetcher() {
        return mockConstruction(AccountsFetcher.class,
                (mock, context) -> {
                    User user1 = mock(User.class);
                    User user2 = mock(User.class);
                    ArrayList<User> users = new ArrayList<>();
                    users.add(user1);
                    users.add(user2);
                    doReturn(users).when(mock).fetchMatchingAccounts(any());
                });
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
