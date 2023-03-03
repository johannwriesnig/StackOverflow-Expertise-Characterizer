package com.wriesnig.expertise.git.metrics;

import com.wriesnig.api.git.Repo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class PythonMetricsTest {
    private String repoPath = "src/main/resources/test/metrics/pythonRepo";
    private Repo repo;
    private PythonMetrics pythonMetrics;
    private PythonMetrics spyPythonMetrics;
    private File repoFile;


    @BeforeEach
    public void setUp(){
        repo = new Repo("","",1);
        repo.setFileName(repoPath);
        pythonMetrics = new PythonMetrics(repo);
        spyPythonMetrics = spy(pythonMetrics);
        repoFile = new File(repo.getFileName()).getAbsoluteFile();
    }

    @Test
    public void ccIsSetAfterExecution() throws IOException, InterruptedException {
        doReturn(new String(Files.readAllBytes(Path.of(repo.getFileName() + "/ccOutput.json")))).when(spyPythonMetrics).getToolReport(anyString(),any());
        spyPythonMetrics.setAvgCyclomaticComplexity();
        assertEquals(2,repo.getCyclomaticComplexity());
    }
    @Test
    public void slocIsSetAfterExecution() throws IOException, InterruptedException {
        doReturn(new String(Files.readAllBytes(Path.of(repo.getFileName() + "/slocOutput.json")))).when(spyPythonMetrics).getToolReport(anyString(),any());
        spyPythonMetrics.setProjectSourceLinesOfCode();
        assertEquals(35,repo.getSourceLinesOfCode());
    }
    @Test
    public void testDirsSlocIsSetAfterExecution() throws IOException, InterruptedException {
        doReturn(new String(Files.readAllBytes(Path.of(repo.getFileName() + "/testsSlocOutput.json")))).when(spyPythonMetrics).getToolReport(anyString(),any());
        spyPythonMetrics.setTestDirectoriesSourceLinesOfCode();
        assertEquals(22,repo.getTestFilesSourceLinesOfCode());
    }


}
