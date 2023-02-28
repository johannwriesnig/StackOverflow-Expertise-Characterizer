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
    private GitUser gitUser;
    private StackUser stackUser;

    private final String picture1 = "Picture1";
    private final String picture2 = "Picture2";
    private final String picture1Path = "src/main/resources/test/profileImages/Picture1.jpeg";
    private final String picture2Path = "src/main/resources/test/profileImages/Picture2.jpg";
    private final String picture1DifferentHeight = "src/main/resources/test/profileImages/Picture1DifferentHeight.jpg";
    private final String picture1DifferentWidth = "src/main/resources/test/profileImages/Picture1DifferentWidth.jpg";


    @BeforeAll
    public static void deactivateLogger() {
        Logger.deactivatePrinting();
    }

    @BeforeEach
    public void setUp() throws IOException {
        gitUser = new GitUser("", "", "", "", "");
        stackUser = new StackUser(1, 1, "random", "", "", "", 1);

        accountsMatchScorer = spy(new AccountsMatchScorer(stackUser, gitUser));
        doReturn(ImageIO.read(new File(picture1Path))).when(accountsMatchScorer).getImageFromUrl(picture1);
        doReturn(ImageIO.read(new File(picture2Path))).when(accountsMatchScorer).getImageFromUrl(picture2);
        doReturn(null).when(accountsMatchScorer).getImageFromUrl("");
    }


    @Test
    public void stackMatchesGitLogin() {
        String name = "John";
        stackUser.setDisplayName(name);
        gitUser.setLogin(name);
        double actualScore = accountsMatchScorer.getMatchingScore();
        double expectedScore = AccountsMatchScorer.MATCHING_NAMES_SCORE;

        assertEquals(expectedScore, actualScore);
    }

    @Test
    public void stackMatchesGitFullName() {
        String name = "John Doe";
        stackUser.setDisplayName(name);
        gitUser.setName(name);
        double actualScore = accountsMatchScorer.getMatchingScore();
        double expectedScore = AccountsMatchScorer.MATCHING_NAMES_SCORE;

        assertEquals(expectedScore, actualScore);
    }

    @Test
    public void namesAndPicturesMatch() {
        String name = "John";
        stackUser.setDisplayName(name);
        stackUser.setProfileImageUrl(picture1);
        gitUser.setLogin(name);
        gitUser.setProfileImageUrl(picture1);
        double actualScore = accountsMatchScorer.getMatchingScore();
        double expectedScore = AccountsMatchScorer.MATCHING_NAMES_SCORE + AccountsMatchScorer.MATCHING_IMAGES_SCORE;

        assertEquals(expectedScore, actualScore);
    }

    @Test
    public void namesAndWebsiteMatch() {
        String name = "John";
        String website = "https://same.website.com";
        stackUser.setDisplayName(name);
        stackUser.setWebsiteUrl(website);
        gitUser.setLogin(name);
        gitUser.setWebsiteUrl(website);
        double actualScore = accountsMatchScorer.getMatchingScore();
        double expectedScore = AccountsMatchScorer.MATCHING_NAMES_SCORE + AccountsMatchScorer.MATCHING_LINKED_WEBSITES_SCORE;

        assertEquals(expectedScore, actualScore, 0.001);
    }

    @Test
    public void picturesAndWebsiteMatch() {
        String website = "https://same.website.com";
        stackUser.setProfileImageUrl(picture1);
        stackUser.setWebsiteUrl(website);
        gitUser.setProfileImageUrl(picture1);
        gitUser.setWebsiteUrl(website);
        double actualScore = accountsMatchScorer.getMatchingScore();
        double expectedScore = AccountsMatchScorer.MATCHING_IMAGES_SCORE + AccountsMatchScorer.MATCHING_LINKED_WEBSITES_SCORE;

        assertEquals(expectedScore, actualScore, 0.001);
    }

    @Test
    public void noMatchWhenStackProfileImageCouldNotBeRetrieved() {
        String picture3 = "picture3";
        doReturn(null).when(accountsMatchScorer).getImageFromUrl(picture3);
        stackUser.setProfileImageUrl(picture3);
        gitUser.setProfileImageUrl(picture1);
        double actualScore = accountsMatchScorer.getMatchingScore();
        double expectedScore = AccountsMatchScorer.NO_MATCH_SCORE;

        assertEquals(expectedScore, actualScore);
    }

    @Test
    public void NoMatchWhenGitProfileImageCouldNotBeRetrieved() {
        String picture3 = "picture3";
        doReturn(null).when(accountsMatchScorer).getImageFromUrl(picture3);
        stackUser.setProfileImageUrl(picture1);
        gitUser.setProfileImageUrl(picture3);

        double actualScore = accountsMatchScorer.getMatchingScore();
        double expectedScore = AccountsMatchScorer.NO_MATCH_SCORE;

        assertEquals(expectedScore, actualScore);
    }


    @Test
    public void noMatch() {
        double actualScore = accountsMatchScorer.getMatchingScore();
        double expectedScore = AccountsMatchScorer.NO_MATCH_SCORE;

        assertEquals(expectedScore, actualScore);
    }



    @Test
    public void imageRetrievalNoUrl() throws IOException {
        BufferedImage image = accountsMatchScorer.getImageFromUrl("NoUrl");
        assertNull(image);
    }

    @Test
    public void imageRetrievalRightUrl() throws IOException {
        BufferedImage urlImage = ImageIO.read(new File(picture1Path));
        try (MockedStatic<ImageIO> dummy = Mockito.mockStatic(ImageIO.class)) {
            dummy.when(() -> ImageIO.read(new URL("https://website/Picture1?&s=256"))).
                    thenReturn(urlImage);
            BufferedImage image = accountsMatchScorer.getImageFromUrl("https://website/Picture1?s=500");
            assertNotNull(image);
        }
    }

    @Test
    public void readImageFromUrlThrowsIOException(){
        try (MockedStatic<ImageIO> mockedImageIO = Mockito.mockStatic(ImageIO.class);
            MockedStatic<Logger> mockedLogger = Mockito.mockStatic(Logger.class)) {
            mockedImageIO.when(()-> ImageIO.read((URL) any())).thenThrow(IOException.class);
            accountsMatchScorer.getImageFromUrl("https://website/Picture1?s=500");
            mockedLogger.verify(()-> Logger.error(any(), any()),times(1));
        }
    }

    @Test
    public void imageRetrievalGoogleUrl() throws IOException {
        BufferedImage urlImage = ImageIO.read(new File(picture1Path));
        try (MockedStatic<ImageIO> dummy = Mockito.mockStatic(ImageIO.class)) {
            dummy.when(() -> ImageIO.read(new URL("https://google/Picture1?s=k-s256"))).
                    thenReturn(urlImage);
            BufferedImage image = accountsMatchScorer.getImageFromUrl("https://google/Picture1?s=k-s123");
            assertNotNull(image);
        }
    }

    @Test
    public void imageDissimilarityWithDifferentWidth() throws IOException {
        BufferedImage normal = ImageIO.read(new File(picture1Path));
        BufferedImage differentWidth = ImageIO.read(new File(picture1DifferentWidth));

        assertEquals(100, accountsMatchScorer.getImagesDissimilarity(normal, differentWidth));
    }

    @Test
    public void imageDissimilarityWithDifferentHeight() throws IOException {
        BufferedImage normal = ImageIO.read(new File(picture1Path));
        BufferedImage differentWidth = ImageIO.read(new File(picture1DifferentHeight));

        assertEquals(100, accountsMatchScorer.getImagesDissimilarity(normal, differentWidth));
    }




    @AfterEach
    public void tearDown() {
        accountsMatchScorer = null;
        gitUser = null;
        stackUser = null;
    }

}
