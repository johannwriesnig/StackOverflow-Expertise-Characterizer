package com.wriesnig.expertise.git.badges;

import com.wriesnig.expertise.stack.StackExpertiseJob;
import com.wriesnig.utils.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class StatusBadgesAnalyserTest {
    private StatusBadgesAnalyser buildPassingAnalyser;
    private StatusBadgesAnalyser buildPassingSpy;

    private StatusBadgesAnalyser buildFailingAnalyser;
    private StatusBadgesAnalyser buildFailingSpy;

    private final String buildPassingAndCodeCove84ReadMePath = "src/main/resources/test/badges/buildPassingAndCodeCove86.md";
    private final String buildFailingAndCodeCove76Path = "src/main/resources/test/badges/buildFailingAndCodeCove76.md";


    @BeforeEach
    public void setUp() throws IOException {
        Logger.deactivatePrinting();
        buildPassingAnalyser = new StatusBadgesAnalyser(new File(buildPassingAndCodeCove84ReadMePath));
        buildPassingSpy = spy(buildPassingAnalyser);
        doReturn(new FileInputStream("src/main/resources/test/badges/buildPassing.html")).when(buildPassingSpy).getInputStreamFromBadge("https://website.com/buildPassing");
        doReturn(new FileInputStream("src/main/resources/test/badges/codecoverage86.html")).when(buildPassingSpy).getInputStreamFromBadge("https://website.com/codecoverage86");
        buildPassingSpy.initBadges();

        buildFailingAnalyser = new StatusBadgesAnalyser(new File(buildFailingAndCodeCove76Path));
        buildFailingSpy = spy(buildFailingAnalyser);
        doReturn(new FileInputStream("src/main/resources/test/badges/buildFailing.html")).when(buildFailingSpy).getInputStreamFromBadge("https://website.com/buildFailing");
        doReturn(new FileInputStream("src/main/resources/test/badges/codecov76.html")).when(buildFailingSpy).getInputStreamFromBadge("https://website.com/codecove76");
        buildFailingSpy.initBadges();
    }

    @Test
    public void buildPassingAnd84CodeCoverage() throws IOException {
        assertEquals(BuildStatus.PASSING,buildPassingSpy.getBuildStatus());
        assertEquals(86, buildPassingSpy.getCoverage());
    }

    @Test
    public void setBuildFailingAnd76CodeCov(){
        assertEquals(BuildStatus.FAILING, buildFailingSpy.getBuildStatus());
        assertEquals(76, buildFailingSpy.getCoverage());
    }
    @Test
    public void notExistingReadMe(){
        StatusBadgesAnalyser spy = spy(new StatusBadgesAnalyser(new File("Not existing")));
        spy.initBadges();
        assertEquals(BuildStatus.NOT_GIVEN, spy.getBuildStatus());
        assertEquals(-1, spy.getCoverage());
    }

    @Test
    public void fileReadingFails(){
        try(MockedStatic<Files> mockedFiles = mockStatic(Files.class);
            MockedStatic<Logger> mockedLogger = mockStatic(Logger.class)){
            mockedFiles.when(()-> Files.readString(any())).thenThrow(IOException.class);
            StatusBadgesAnalyser statusBadgesAnalyser = new StatusBadgesAnalyser(new File(buildPassingAndCodeCove84ReadMePath));
            statusBadgesAnalyser.initBadges();
            mockedLogger.verify(()-> Logger.error(anyString(), any()),times(1));
        }
    }

    @Test
    public void badgeUrlInputStreamFails(){
        try(MockedStatic<Logger> mockedLogger = mockStatic(Logger.class);
            MockedConstruction<URL> mockedUrl = getMockedUrl()){
            StatusBadgesAnalyser statusBadgesAnalyser = new StatusBadgesAnalyser(new File(buildPassingAndCodeCove84ReadMePath));
            statusBadgesAnalyser.initBadges();
            mockedLogger.verify(()-> Logger.error(any(), any()),times(2));
        }
    }

    public MockedConstruction<URL> getMockedUrl() {
        return mockConstruction(URL.class,
                (mock, context) -> {
                    doThrow(MalformedURLException.class).when(mock).openStream();
                });
    }


    @AfterEach
    public void tearDown(){
        buildPassingSpy = null;
        buildPassingAnalyser = null;
    }
}
