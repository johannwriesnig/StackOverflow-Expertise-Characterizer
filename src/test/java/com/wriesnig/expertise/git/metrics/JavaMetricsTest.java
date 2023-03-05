package com.wriesnig.expertise.git.metrics;

import com.wriesnig.api.git.Repo;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JavaMetricsTest {
    @Test
    public void repoMetricsSetAfterExecution() throws FileNotFoundException {
        Repo repo = new Repo("", "", false, 0);
        repo.setFileName("src/main/resources/test/metrics/javaRepo");
        JavaMetrics javaMetrics = new JavaMetrics(repo);
        javaMetrics.setMetrics();
        assertEquals(3.5,repo.getCyclomaticComplexity());
        assertEquals(29,repo.getSourceLinesOfCode());
        assertTrue(repo.isHasTests());
        new PrintWriter(repo.getFileName()+"/output.json").close();
    }
}
