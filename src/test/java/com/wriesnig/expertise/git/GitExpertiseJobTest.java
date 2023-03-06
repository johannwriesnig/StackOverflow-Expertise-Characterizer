package com.wriesnig.expertise.git;

import com.wriesnig.api.git.FinishRepo;
import com.wriesnig.api.git.GitUser;
import com.wriesnig.api.git.Repo;
import com.wriesnig.api.stack.StackUser;
import com.wriesnig.expertise.Expertise;
import com.wriesnig.expertise.Tags;
import com.wriesnig.expertise.User;
import com.wriesnig.expertise.git.badges.StatusBadgesAnalyser;
import com.wriesnig.expertise.git.metrics.JavaMetrics;
import com.wriesnig.utils.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class GitExpertiseJobTest {
    private GitExpertiseJob gitExpertiseJob;

    @BeforeEach
    public void setUp() {
        User user = mock(User.class);
        when(user.getGitLogin()).thenReturn("user");
        when(user.getExpertise()).thenReturn(new Expertise());
        gitExpertiseJob = new GitExpertiseJob(user);
    }

    @Test
    public void testDetermineReposExpertise() throws InterruptedException {
        BlockingQueue<Repo> downloadedRepos = new LinkedBlockingQueue<>();
        Repo repo = new Repo("repo1", "",false,0);
        downloadedRepos.put(repo);
        downloadedRepos.put(new FinishRepo());

        try(MockedConstruction<GitExpertiseJob.RepoExpertiseJob> mockedRepoExpertiseJob = mockConstruction(GitExpertiseJob.RepoExpertiseJob.class);
            MockedConstruction<ThreadPoolExecutor> mockedExecutorService = mockConstruction(ThreadPoolExecutor.class)){
            gitExpertiseJob.determineReposExpertise(downloadedRepos, "");
            assertEquals(1, mockedRepoExpertiseJob.constructed().size());
            assertEquals(1, mockedExecutorService.constructed().size());
            GitExpertiseJob.RepoExpertiseJob expertiseJob = mockedRepoExpertiseJob.constructed().get(0);
            ExecutorService executorService = mockedExecutorService.constructed().get(0);
            verify(executorService, times(1)).awaitTermination(anyLong(), any());
            verify(executorService, times(1)).execute(expertiseJob);
            verify(executorService, times(1)).shutdown();
        }

    }

    @Test
    public void findTagInMavenProject() {
        String tag = "spring";
        Tags.tagsToCharacterize = new String[]{tag};
        Repo repo = new Repo("", "", false, 0);
        repo.setFileName("src/main/resources/test/projects/java/maven");
        gitExpertiseJob.findTagsForJavaProject(repo);
        assertTrue(repo.getPresentTags().contains(tag));
    }

    @Test
    public void findTagsInGradleProject() {
        String tag1 = "hibernate";
        String tag2 = "junit";
        Tags.tagsToCharacterize = new String[]{tag1, tag2};
        Repo repo = new Repo("", "", false, 0);
        repo.setFileName("src/main/resources/test/projects/java/gradle");
        gitExpertiseJob.findTagsForJavaProject(repo);
        assertTrue(repo.getPresentTags().contains(tag1));
        assertTrue(repo.getPresentTags().contains(tag2));
    }

    @Test
    public void findTagsInJavaProjectByImports(){
        Repo repo = new Repo("", "", false, 0);
        repo.setFileName("src/main/resources/test/projects/java/standard");
        String tag = "hibernate";
        Tags.tagsToCharacterize = new String[]{tag};
        gitExpertiseJob.findTagsInImportLines(repo);
        assertTrue(repo.getPresentTags().contains(tag));
        assertEquals(1, repo.getPresentTags().size());
    }


    @Test
    public void findTagsInPythonProject() {
        String tag1 = "django";
        String tag2 = "flask";
        Tags.tagsToCharacterize = new String[]{tag1, tag2};
        Repo repo = new Repo("repo", "", false, 0);
        repo.setFileName("src/main/resources/test/projects/python");
        gitExpertiseJob.findTagsInImportLines(repo);
        assertTrue(repo.getPresentTags().contains(tag1));
        assertTrue(repo.getPresentTags().contains(tag2));
    }

    @Test
    public void computeAndStoreReposExpertise(){
        String tagJava = "java";
        String tagSpring = "spring";
        Repo javaExpertise3 = new Repo("", "java", false, 0);
        javaExpertise3.addTag(tagJava);
        javaExpertise3.setQuality(3);
        Repo javaExpertise4 = new Repo("", "java", false, 0);
        javaExpertise4.addTag(tagJava);
        javaExpertise4.setQuality(4);
        Repo springAndJavaExpertise5 = new Repo("", "java", false, 0);
        springAndJavaExpertise5.addTag(tagJava);
        springAndJavaExpertise5.addTag(tagSpring);
        springAndJavaExpertise5.setQuality(5);
        Tags.tagsToCharacterize = new String[]{tagJava, tagSpring};

        ArrayList<Repo> repos = new ArrayList<>();
        repos.add(javaExpertise3);
        repos.add(javaExpertise4);
        repos.add(springAndJavaExpertise5);

        User user = new User(new StackUser(1,1,"user","","","",1),
                new GitUser("user","","","",""));

        HashMap<String, ArrayList<Double>> expertisePerTag = gitExpertiseJob.getExpertisePerTag(repos);
        gitExpertiseJob.storeExpertise(expertisePerTag, user);
        HashMap<String, Double> expertise = user.getExpertise().getGitExpertise();
        assertEquals(2, expertise.keySet().size());
        assertEquals(5, expertise.get(tagSpring));
        assertEquals(4, expertise.get(tagJava));
    }




    @Test
    public void getReadMe(){
        Repo repo = new Repo("repo", "", false, 0);
        repo.setFileName("src/main/resources/test/projects");
        File readMe = gitExpertiseJob.getReadMeFile(repo);
        File expectedFile = new File("src/main/resources/test/projects/readme.adoc");
        assertEquals(expectedFile.getPath(), readMe.getPath());
    }

    @Test
    public void cleanseRepos(){
        Tags.tagsToCharacterize = new String[]{"java"};
        Repo repo1 = new Repo("repo1", "java", false, 0);
        Repo repo2 = new Repo("repo2", "java", true, 0);
        Repo repo3 = new Repo("repo3", "", false, 0);

        ArrayList<Repo> repos = new ArrayList<>();
        repos.add(repo1);
        repos.add(repo2);
        repos.add(repo3);

        gitExpertiseJob.cleanseRepos(repos);

        assertEquals(1, repos.size());
        assertEquals(repo1, repos.get(0));
    }

    @Test
    public void determineExpertise(){
        Logger.deactivatePrinting();
        Repo repo = new Repo("repo", "java", false, 0);
        repo.setFileName("src/main/resources/test/projects/java/standard");
        String tag = "hibernate";
        Tags.tagsToCharacterize = new String[]{tag};

        try(MockedStatic<GitClassifier> mockedClassifier = mockStatic(GitClassifier.class);
            MockedConstruction<StatusBadgesAnalyser> mockedAnalyser = mockConstruction(StatusBadgesAnalyser.class);
            MockedConstruction<JavaMetrics> mockedMetricsSetter = mockConstruction(JavaMetrics.class)){
            double classifierOutput = 3.0;
            mockedClassifier.when(()->GitClassifier.classify(any())).thenReturn(classifierOutput);
            gitExpertiseJob.determineExpertise(repo);
            assertEquals(Expertise.CLASSIFIER_OUTPUT[(int) classifierOutput],repo.getQuality());
        }
    }

    @AfterEach
    public void tearDown() {
        gitExpertiseJob = null;
    }
}
