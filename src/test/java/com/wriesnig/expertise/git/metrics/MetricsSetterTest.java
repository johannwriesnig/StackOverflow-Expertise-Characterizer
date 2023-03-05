package com.wriesnig.expertise.git.metrics;

import com.wriesnig.api.git.Repo;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class MetricsSetterTest {
    private final String repoPath = "src/main/resources/test/metrics/javaRepo/";

    @Test
    public void getProjectTestDirectories(){
        Repo repo = new Repo("","", false, 0);
        repo.setFileName(repoPath);
        File testDir1 = new File(repoPath + "module1/src/tests").getAbsoluteFile();
        File testDir2 = new File(repoPath + "module2/src/tests").getAbsoluteFile();
        MetricsSetter metricsSetter = mock(MetricsSetter.class, withSettings()
                .useConstructor(repo)
                .defaultAnswer(CALLS_REAL_METHODS));

        ArrayList<File> testDirs = metricsSetter.getTestDirectories();
        assertEquals(2, testDirs.size());
        assertTrue(testDirs.stream().map(File::getPath).anyMatch(name -> testDir1.getPath().equals(name)));
        assertTrue(testDirs.stream().map(File::getPath).anyMatch(name -> testDir2.getPath().equals(name)));
    }
}
