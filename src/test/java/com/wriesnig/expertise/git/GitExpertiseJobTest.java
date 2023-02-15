package com.wriesnig.expertise.git;

import com.wriesnig.api.git.Repo;
import com.wriesnig.expertise.Expertise;
import com.wriesnig.expertise.Tags;
import com.wriesnig.expertise.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
    public void findTagInMavenProject() {
        String tag = "spring";
        Tags.tagsToCharacterize = new String[]{tag};
        Repo repo = new Repo("", "", 0);
        repo.setFileName("src/main/resources/test/projects/java/maven");
        gitExpertiseJob.findTagsForJavaProject(repo);
        assertTrue(repo.getPresentTags().contains(tag));
    }

    @Test
    public void findTagsInGradleProject() {
        String tag1 = "hibernate";
        String tag2 = "junit";
        Tags.tagsToCharacterize = new String[]{tag1, tag2};
        Repo repo = new Repo("", "", 0);
        repo.setFileName("src/main/resources/test/projects/java/gradle");
        gitExpertiseJob.findTagsForJavaProject(repo);
        assertTrue(repo.getPresentTags().contains(tag1));
        assertTrue(repo.getPresentTags().contains(tag2));
    }

    @Test
    public void findTagsInPythonProject() {
        String tag1 = "django";
        String tag2 = "flask";
        Tags.tagsToCharacterize = new String[]{tag1, tag2};
        Repo repo = new Repo("repo", "", 0);
        repo.setFileName("src/main/resources/test/projects/python");
        gitExpertiseJob.findTagsForPythonProject(repo);
        assertTrue(repo.getPresentTags().contains(tag1));
        assertTrue(repo.getPresentTags().contains(tag2));
    }

    @Test
    public void getReadMe(){
        Repo repo = new Repo("repo", "", 0);
        repo.setFileName("src/main/resources/test/projects");
        File readMe = gitExpertiseJob.getReadMeFile(repo);
        File expectedFile = new File("src/main/resources/test/projects/readme.adoc");
        assertEquals(expectedFile.getPath(), readMe.getPath());
    }

    @AfterEach
    public void tearDown() {
        gitExpertiseJob = null;
    }
}
