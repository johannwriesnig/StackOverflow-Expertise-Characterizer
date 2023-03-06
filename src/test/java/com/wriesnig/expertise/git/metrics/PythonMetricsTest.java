package com.wriesnig.expertise.git.metrics;

import com.wriesnig.api.git.Repo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class PythonMetricsTest {
    private String repoPath = "src/main/resources/test/metrics/pythonRepo";
    private Repo repo;
    private PythonMetrics pythonMetrics;
    private PythonMetrics spyPythonMetrics;
    private File repoFile;


    @BeforeEach
    public void setUp(){
        repo = new Repo("","",false, 0);
        repo.setFileName(repoPath);
        pythonMetrics = new PythonMetrics(repo);
        spyPythonMetrics = spy(pythonMetrics);
        repoFile = new File(repo.getFileName()).getAbsoluteFile();
    }

    @Test
    public void radonProcessButDoesNotPass() throws IOException, InterruptedException {
        String command = "cc";
        doCallRealMethod().when(spyPythonMetrics).callRadonProcess(any(),any());
        doCallRealMethod().when(spyPythonMetrics).setProcessProperties(anyString(),anyString(),anyString());
        Process radonProcess = mock(Process.class);
        doReturn(false).when(radonProcess).waitFor(anyLong(), any());
        doReturn(InputStream.nullInputStream()).when(radonProcess).getErrorStream();
        try(MockedConstruction<ProcessBuilder> mockedProcessBuilder = mockConstruction(ProcessBuilder.class,
                (mock,context)->{
                    doReturn(radonProcess).when(mock).start();
                })){
            assertThrows(InterruptedException.class, ()-> spyPythonMetrics.callRadonProcess(command,pythonMetrics.root));
            ProcessBuilder processBuilder = mockedProcessBuilder.constructed().get(0);
            assertEquals(1, mockedProcessBuilder.constructed().size());
            verify(processBuilder, times(1)).start();
        }
        verify(spyPythonMetrics, times(1)).setProcessProperties(command,pythonMetrics.root.getAbsolutePath(), pythonMetrics.output.getAbsolutePath());
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
