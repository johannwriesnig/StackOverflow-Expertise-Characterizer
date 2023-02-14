package com.wriesnig.utils;

import com.wriesnig.api.git.GitUser;
import com.wriesnig.api.stack.StackUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AccountsMatchScorerTest {
    private AccountsMatchScorer accountsMatchScorer;
    private final String picture1 = "src/main/resources/test/profileImages/Picture1.jpeg";
    private final String picture2 = "src/main/resources/test/profileImages/Picture2.jpg";
    private final String picture1DifferentHeight = "src/main/resources/test/profileImages/Picture1DifferentHeight.jpg";
    private final String picture1DifferentWidth = "src/main/resources/test/profileImages/Picture1DifferentWidth.jpg";

    @BeforeAll
    public static void deactivateLogger() {
        Logger.deactivatePrinting();
    }

    @BeforeEach
    public void setUp() throws IOException {
        accountsMatchScorer = spy(new AccountsMatchScorer());
        doReturn(ImageIO.read(new File(picture1))).when(accountsMatchScorer).getImageFromUrl("Picture1");
        doReturn(ImageIO.read(new File(picture2))).when(accountsMatchScorer).getImageFromUrl("Picture2");
    }

    @Test
    public void stackMatchesGitLogin() {
        GitUser gitUser = new GitUser("jondoe", "Picture1", "Jon Doe", "htmlUrl", "websiteUrl");
        StackUser stackUser = new StackUser(1, 1, "jondoe", "websiteUrl", "link", "Picture2", 1);

        double actualScore = accountsMatchScorer.getMatchingScore(stackUser, gitUser);
        double expectedScore = AccountsMatchScorer.MATCHING_NAMES_SCORE;

        assertEquals(expectedScore, actualScore);
    }

    @Test
    public void stackMatchesGitFullName() {
        GitUser gitUser = new GitUser("jondoe", "Picture1", "Jon Doe", "htmlUrl", "websiteUrl");
        StackUser stackUser = new StackUser(1, 1, "Jon Doe", "websiteUrl", "link", "Picture2", 1);

        double actualScore = accountsMatchScorer.getMatchingScore(stackUser, gitUser);
        double expectedScore = AccountsMatchScorer.MATCHING_NAMES_SCORE;

        assertEquals(expectedScore, actualScore);
    }

    @Test
    public void namesAndPicturesMatch() {
        GitUser gitUser = new GitUser("jondoe", "Picture1", "Jon Doe", "htmlUrl", "websiteUrl");
        StackUser stackUser = new StackUser(1, 1, "jondoe", "websiteUrl", "link", "Picture1", 1);

        double actualScore = accountsMatchScorer.getMatchingScore(stackUser, gitUser);
        double expectedScore = AccountsMatchScorer.MATCHING_NAMES_SCORE + AccountsMatchScorer.MATCHING_IMAGES_SCORE;

        assertEquals(expectedScore, actualScore);
    }

    @Test
    public void namesAndWebsiteMatch() {
        GitUser gitUser = new GitUser("jondoe", "Picture1", "Jon Doe", "git/jondoe", "stack/jondoe");
        StackUser stackUser = new StackUser(1, 1, "jondoe", "git/jondoe", "stack/jondoe", "Picture2", 1);

        double actualScore = accountsMatchScorer.getMatchingScore(stackUser, gitUser);
        double expectedScore = AccountsMatchScorer.MATCHING_NAMES_SCORE + AccountsMatchScorer.MATCHING_LINKED_WEBSITES_SCORE;

        assertEquals(expectedScore, actualScore);
    }

    @Test
    public void PicturesAndWebsiteMatch() {
        GitUser gitUser = new GitUser("jondoe1", "Picture2", "Jon Doe", "git/jondoe1", "stack/jondoe");
        StackUser stackUser = new StackUser(1, 1, "jondoe", "git/jondoe1", "stack/jondoe", "Picture2", 1);

        double actualScore = accountsMatchScorer.getMatchingScore(stackUser, gitUser);
        double expectedScore = AccountsMatchScorer.MATCHING_IMAGES_SCORE + AccountsMatchScorer.MATCHING_LINKED_WEBSITES_SCORE;

        assertEquals(expectedScore, actualScore);
    }

    @Test
    public void NoMatchWhereStackProfileImageCouldNotBeRetrieved() {
        doReturn(null).when(accountsMatchScorer).getImageFromUrl("Picture3");

        GitUser gitUser = new GitUser("jondoe1", "Picture2", "Jon Doe", "git/jondoe1", "");
        StackUser stackUser = new StackUser(1, 1, "jondoe", "", "stack/jondoe", "Picture3", 1);

        double actualScore = accountsMatchScorer.getMatchingScore(stackUser, gitUser);
        double expectedScore = AccountsMatchScorer.NO_MATCH_SCORE;

        assertEquals(expectedScore, actualScore);
    }

    @Test
    public void NoMatchWhereGitProfileImageCouldNotBeRetrieved() {
        doReturn(null).when(accountsMatchScorer).getImageFromUrl("Picture3");

        GitUser gitUser = new GitUser("jondoe1", "Picture3", "Jon Doe", "git/jondoe1", "");
        StackUser stackUser = new StackUser(1, 1, "jondoe", "", "stack/jondoe", "Picture1", 1);

        double actualScore = accountsMatchScorer.getMatchingScore(stackUser, gitUser);
        double expectedScore = AccountsMatchScorer.NO_MATCH_SCORE;

        assertEquals(expectedScore, actualScore);
    }


    @Test
    public void noMatch() {
        GitUser gitUser = new GitUser("jondoe1", "Picture1", "Jon Doe", "git/random", "");
        StackUser stackUser = new StackUser(1, 1, "jondoe", "git/jondoe1", "", "Picture2", 1);

        double actualScore = accountsMatchScorer.getMatchingScore(stackUser, gitUser);
        double expectedScore = AccountsMatchScorer.NO_MATCH_SCORE;

        assertEquals(expectedScore, actualScore);
    }

    @Test
    public void stackLinksGitAccount() {
        GitUser gitUser = new GitUser("jondoe1", "Picture1", "Jon Doe", "git/jondoe1", "");
        StackUser stackUser = new StackUser(1, 1, "jondoe", "git/jondoe1", "stack/jondoe", "Picture2", 1);

        double actualScore = accountsMatchScorer.getMatchingScore(stackUser, gitUser);
        double expectedScore = AccountsMatchScorer.MATCHING_LINKED_WEBSITES_SCORE;

        assertEquals(expectedScore, actualScore);
    }

    @Test
    public void gitLinksStackAccount() {
        GitUser gitUser = new GitUser("jondoe1", "Picture1", "Jon Doe", "git/jondoe1", "stack/jondoe");
        StackUser stackUser = new StackUser(1, 1, "jondoe", "", "stack/jondoe", "Picture2", 1);

        double actualScore = accountsMatchScorer.getMatchingScore(stackUser, gitUser);
        double expectedScore = AccountsMatchScorer.MATCHING_LINKED_WEBSITES_SCORE;

        assertEquals(expectedScore, actualScore);
    }

    @Test
    public void imageRetrievalNoUrl() throws IOException {
        BufferedImage image = accountsMatchScorer.getImageFromUrl("NoUrl");
        assertNull(image);
    }

    @Test
    public void imageRetrievalRightUrl() throws IOException {
        BufferedImage urlImage = ImageIO.read(new File(picture1));
        try (MockedStatic<ImageIO> dummy = Mockito.mockStatic(ImageIO.class)) {
            dummy.when(() -> ImageIO.read(new URL("https://website/Picture1?&s=256"))).
                    thenReturn(urlImage);
            BufferedImage image = accountsMatchScorer.getImageFromUrl("https://website/Picture1?s=500");
            assertNotNull(image);
        }
    }

    @Test
    public void imageRetrievalGoogleUrl() throws IOException {
        BufferedImage urlImage = ImageIO.read(new File(picture1));
        try (MockedStatic<ImageIO> dummy = Mockito.mockStatic(ImageIO.class)) {
            dummy.when(() -> ImageIO.read(new URL("https://google/Picture1?s=k-s256"))).
                    thenReturn(urlImage);
            BufferedImage image = accountsMatchScorer.getImageFromUrl("https://google/Picture1?s=k-s123");
            assertNotNull(image);
        }
    }

    @Test
    public void imageDissimilarityWithDifferentWidth() throws IOException {
        BufferedImage normal = ImageIO.read(new File(picture1));
        BufferedImage differentWidth = ImageIO.read(new File(picture1DifferentWidth));

        assertEquals(100, accountsMatchScorer.getImagesDissimilarity(normal, differentWidth));
    }

    @Test
    public void imageDissimilarityWithDifferentHeight() throws IOException {
        BufferedImage normal = ImageIO.read(new File(picture1));
        BufferedImage differentWidth = ImageIO.read(new File(picture1DifferentHeight));

        assertEquals(100, accountsMatchScorer.getImagesDissimilarity(normal, differentWidth));
    }


    @AfterEach
    public void tearDown() {
        accountsMatchScorer = null;
    }

}
