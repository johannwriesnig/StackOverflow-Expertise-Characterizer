package com.wriesnig.expertise.git.badges;

import com.wriesnig.utils.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class StatusBadgesAnalyserTest {
    private StatusBadgesAnalyser statusBadgesAnalyser;


    @TempDir
    Path tempDir;


    @BeforeEach
    public void setUp() throws IOException {
        Logger.deactivatePrinting();
        statusBadgesAnalyser = mock(StatusBadgesAnalyser.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    }

    @Test
    public void shouldReturnBuildPassing() throws IOException {
        final Path buildPassingReadMe = tempDir.resolve("readMe.md");
        Files.writeString(buildPassingReadMe, StatusBadgesHTML.READ_ME_BUILD_PASSING_AND_CODE_COV_86_READ);
        doReturn(StatusBadgesHTML.getBuildPassing()).when(statusBadgesAnalyser).getInputStreamFromBadge(StatusBadgesHTML.LINK_BUILD_PASSING);
        statusBadgesAnalyser.initBadges(buildPassingReadMe.toFile());
        assertEquals(BuildStatus.PASSING,statusBadgesAnalyser.getBuildStatus());
    }

    @Test
    public void shouldReturnBuildFailing() throws IOException {
        final Path buildFailingReadMe = tempDir.resolve("readMe.md");
        Files.writeString(buildFailingReadMe, StatusBadgesHTML.READ_ME_ME_BUILD_FAILING_AND_CODE_COV_76);
        doReturn(StatusBadgesHTML.getBuildFailing()).when(statusBadgesAnalyser).getInputStreamFromBadge(StatusBadgesHTML.LINK_BUILD_FAILING);
        statusBadgesAnalyser.initBadges(buildFailingReadMe.toFile());
        assertEquals(BuildStatus.FAILING,statusBadgesAnalyser.getBuildStatus());
    }

    @Test
    public void shouldReturn76Coverage() throws IOException {
        final Path _76CoverageReadMe = tempDir.resolve("readMe.md");
        Files.writeString(_76CoverageReadMe, StatusBadgesHTML.READ_ME_ME_BUILD_FAILING_AND_CODE_COV_76);
        doReturn(StatusBadgesHTML.getCodeCov76()).when(statusBadgesAnalyser).getInputStreamFromBadge(StatusBadgesHTML.LINK_CODE_COV_76);
        statusBadgesAnalyser.initBadges(_76CoverageReadMe.toFile());
        assertEquals(76,statusBadgesAnalyser.getCoverage());
    }

    @Test
    public void shouldReturn86Coverage() throws IOException {
        final Path _86CoverageReadMe = tempDir.resolve("readMe.md");
        Files.writeString(_86CoverageReadMe, StatusBadgesHTML.READ_ME_BUILD_PASSING_AND_CODE_COV_86_READ);
        doReturn(StatusBadgesHTML.getCodeCoverage86()).when(statusBadgesAnalyser).getInputStreamFromBadge(StatusBadgesHTML.LINK_CODE_COV_86);
        statusBadgesAnalyser.initBadges(_86CoverageReadMe.toFile());
        assertEquals(86,statusBadgesAnalyser.getCoverage());
    }

    @Test
    public void shouldReturnBuildNotGiven() throws IOException {
        final Path emptyReadMe = tempDir.resolve("readMe.md");
        Files.writeString(emptyReadMe, StatusBadgesHTML.READ_ME_NO_BUILD_AND_COVE_COV_NO_VALUE);
        statusBadgesAnalyser.initBadges(emptyReadMe.toFile());
        assertEquals(BuildStatus.NOT_GIVEN,statusBadgesAnalyser.getBuildStatus());
    }

    @Test
    public void shouldReturnMinus1Coverage() throws IOException {
        final Path emptyReadMe = tempDir.resolve("readMe.md");
        Files.writeString(emptyReadMe, StatusBadgesHTML.READ_ME_NO_BUILD_AND_COVE_COV_NO_VALUE);
        statusBadgesAnalyser.initBadges(emptyReadMe.toFile());
        assertEquals(-1,statusBadgesAnalyser.getCoverage());
    }

    @AfterEach
    public void tearDown() {
        statusBadgesAnalyser = null;
    }
}
