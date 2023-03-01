package com.wriesnig.expertise.git.metrics;

import com.wriesnig.api.git.Repo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mockStatic;

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
        doNothing().when(spyPythonMetrics).callRadonProcess(anyString(),any(),any());
        Path path = Path.of(repo.getFileName() + "\\ccOutput.json");
        byte[] content = Files.readAllBytes(path);

        try(MockedStatic<Files> mockedFiles = mockStatic(Files.class)){
            mockedFiles.when(()->Files.readAllBytes(any())).thenReturn(content);
            spyPythonMetrics.setAvgCyclomaticComplexity();
            assertEquals(2,repo.getCyclomaticComplexity());
        }
    }
    @Test
    public void slocIsSetAfterExecution() throws IOException, InterruptedException {
        doNothing().when(spyPythonMetrics).callRadonProcess(anyString(),any(),any());
        Path path = Path.of(repo.getFileName() + "\\slocOutput.json");
        byte[] content = Files.readAllBytes(path);

        try(MockedStatic<Files> mockedFiles = mockStatic(Files.class)){
            mockedFiles.when(()->Files.readAllBytes(any())).thenReturn(content);
            spyPythonMetrics.setProjectSourceLinesOfCode(repoFile);
            assertEquals(35,repo.getSourceLinesOfCode());
        }
    }
    @Test
    public void testDirsSlocIsSetAfterExecution() throws IOException, InterruptedException {
        doNothing().when(spyPythonMetrics).callRadonProcess(anyString(),any(),any());
        ArrayList<File> testDirs = new ArrayList<>();
        testDirs.add(new File(repo.getFileName()+"\\module1\\tests"));
        testDirs.add(new File(repo.getFileName()+"\\module2\\tests"));
        doReturn(testDirs).when(spyPythonMetrics).getTestDirectories();
        Path path = Path.of(repo.getFileName() + "\\testsSlocOutput.json");
        byte[] content = Files.readAllBytes(path);

        try(MockedStatic<Files> mockedFiles = mockStatic(Files.class)){
            mockedFiles.when(()->Files.readAllBytes(any())).thenReturn(content);
            mockedFiles.when(()->Files.walk(any())).thenCallRealMethod();
            spyPythonMetrics.setTestDirectoriesSourceLinesOfCode(repoFile);
            assertEquals(22,repo.getTestFilesSourceLinesOfCode());
        }
    }
}
