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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountsMatchScorerTest {
    private AccountsMatchScorer accountsMatchScorer;
    private GitUser gitUser;
    private StackUser stackUser;

    private final String picture1 = "Picture1";
    private final String picture1Path = "src/main/resources/test/profileImages/Picture1.jpeg";

    @BeforeAll
    public static void deactivateLogger() {
        Logger.deactivatePrinting();
    }

    @BeforeEach
    public void setUp() throws IOException {
        gitUser = new GitUser("", "", "", "", "");
        stackUser = new StackUser(1, 1, "random", "", "", "", 1);

        accountsMatchScorer = new AccountsMatchScorer(stackUser, gitUser);
    }

    @Test
    public void shouldCallHelperMethodsAndMatchNames(){
        String name = "John Doe";
        stackUser.setDisplayName(name);
        gitUser.setName(name);
        accountsMatchScorer = spy(new AccountsMatchScorer(stackUser, gitUser));
        doReturn(AccountsMatchScorer.NO_MATCH_SCORE).when(accountsMatchScorer).getImageMatchingScore(stackUser, gitUser);
        assertEquals(AccountsMatchScorer.MATCHING_NAMES_SCORE, accountsMatchScorer.getMatchingScore());
    }


    @Test
    public void shouldMatchStackDisplayNameAndGitLogin() {
        String name = "John";
        stackUser.setDisplayName(name);
        gitUser.setLogin(name);
        double expectedScore = AccountsMatchScorer.MATCHING_NAMES_SCORE;
        double actualScore = accountsMatchScorer.getNameMatchingScore();

        assertEquals(expectedScore, actualScore, 0.001);
    }

    @Test
    public void shouldMatchStackDisplayNameAndGitName() {
        String name = "John Doe";
        stackUser.setDisplayName(name);
        gitUser.setName(name);
        double expectedScore = AccountsMatchScorer.MATCHING_NAMES_SCORE;
        double actualScore = accountsMatchScorer.getNameMatchingScore();

        assertEquals(expectedScore, actualScore, 0.001);
    }


    @Test
    public void shouldMatchNameAndWebsite() {
        String name = "John";
        String website = "https://same.website.com";
        stackUser.setDisplayName(name);
        stackUser.setWebsiteUrl(website);
        gitUser.setLogin(name);
        gitUser.setWebsiteUrl(website);
        double expectedScore = AccountsMatchScorer.MATCHING_NAMES_SCORE + AccountsMatchScorer.MATCHING_LINKED_WEBSITES_SCORE;
        double actualScore = accountsMatchScorer.getMatchingScore();

        assertEquals(expectedScore, actualScore, 0.001);
    }

    @Test
    public void shouldMatchPicturesAndWebsite() throws IOException {
        String website = "https://same.website.com";
        stackUser.setProfileImageUrl(picture1);
        stackUser.setWebsiteUrl(website);
        gitUser.setProfileImageUrl(picture1);
        gitUser.setWebsiteUrl(website);
        accountsMatchScorer = spy(new AccountsMatchScorer(stackUser, gitUser));
        doReturn(ImageIO.read(new File(picture1Path))).when(accountsMatchScorer).getImageFromUrl(picture1);
        double expectedScore = AccountsMatchScorer.MATCHING_IMAGES_SCORE + AccountsMatchScorer.MATCHING_LINKED_WEBSITES_SCORE;
        double actualScore = accountsMatchScorer.getMatchingScore();

        assertEquals(expectedScore, actualScore, 0.001);
    }

    @Test
    public void shouldNotMatchPicturesWhenStackOneIsNull() throws IOException {
        String picture3 = "picture3";
        accountsMatchScorer = spy(new AccountsMatchScorer(stackUser, gitUser));
        doReturn(null).when(accountsMatchScorer).getImageFromUrl(picture3);
        doReturn(ImageIO.read(new File(picture1Path))).when(accountsMatchScorer).getImageFromUrl(picture1);
        stackUser.setProfileImageUrl(picture3);
        gitUser.setProfileImageUrl(picture1);
        double expectedScore = AccountsMatchScorer.NO_MATCH_SCORE;
        double actualScore = accountsMatchScorer.getImageMatchingScore(stackUser,gitUser);

        assertEquals(expectedScore, actualScore);
    }

    @Test
    public void shouldNotMatchPicturesWhenGitOneIsNull() throws IOException {
        String picture3 = "picture3";
        accountsMatchScorer = spy(new AccountsMatchScorer(stackUser, gitUser));
        doReturn(null).when(accountsMatchScorer).getImageFromUrl(picture3);
        doReturn(ImageIO.read(new File(picture1Path))).when(accountsMatchScorer).getImageFromUrl(picture1);
        stackUser.setProfileImageUrl(picture1);
        gitUser.setProfileImageUrl(picture3);
        double expectedScore = AccountsMatchScorer.NO_MATCH_SCORE;
        double actualScore = accountsMatchScorer.getImageMatchingScore(stackUser,gitUser);

        assertEquals(expectedScore, actualScore);
    }


    @Test
    public void shouldNotMatchAnything() {
        double actualScore = accountsMatchScorer.getMatchingScore();
        double expectedScore = AccountsMatchScorer.NO_MATCH_SCORE;

        assertEquals(expectedScore, actualScore);
    }



    @Test
    public void shouldReturnNullWhenNoUrl() throws IOException {
        BufferedImage image = accountsMatchScorer.getImageFromUrl("NoUrl");
        assertNull(image);
    }

    @Test
    public void shouldReturnGooglePicture() throws IOException {
        BufferedImage urlImage = ImageIO.read(new File(picture1Path));
        try (MockedStatic<ImageIO> imageIOMockedStatic = Mockito.mockStatic(ImageIO.class)) {
            imageIOMockedStatic.when(() -> ImageIO.read(new URL("https://google/Picture1?s=k-s256"))).
                    thenReturn(urlImage);
            BufferedImage image = accountsMatchScorer.getImageFromUrl("https://google/Picture1?s=k-s123");
            imageIOMockedStatic.verify(()->ImageIO.read((URL) any()),times(1));
            assertNotNull(image);
        }
    }

    @Test
    public void shouldReturn100PercentDissimilarityWhenDifferentWidths() throws IOException {
        BufferedImage normal = ImageIO.read(new File(picture1Path));
        String picture1DifferentWidth = "src/main/resources/test/profileImages/Picture1DifferentWidth.jpg";
        BufferedImage differentWidth = ImageIO.read(new File(picture1DifferentWidth));

        assertEquals(100, accountsMatchScorer.getImagesDissimilarity(normal, differentWidth));
    }

    @Test
    public void shouldReturn100PercentDissimilarityWhenDifferentHeights() throws IOException {
        BufferedImage normal = ImageIO.read(new File(picture1Path));
        String picture1DifferentHeight = "src/main/resources/test/profileImages/Picture1DifferentHeight.jpg";
        BufferedImage differentWidth = ImageIO.read(new File(picture1DifferentHeight));

        assertEquals(100, accountsMatchScorer.getImagesDissimilarity(normal, differentWidth));
    }

    @Test
    public void shouldStandardizeImageUrl(){
        String actualUrl = accountsMatchScorer.adaptUrlForStandardImageSize("https://website/Picture1?s=500");
        String expectedUrl = "https://website/Picture1?&s=256";
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    public void shouldStandardizeImageUrlWithMultipleParameters(){
        String actualUrl = accountsMatchScorer.adaptUrlForStandardImageSize("https://website/Picture1?s=500&k=100&z=1");
        String expectedUrl = "https://website/Picture1?&k=100&z=1&s=256";
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    public void shouldStandardizeGoogleImageUrl(){
        String actualUrl = accountsMatchScorer.adaptUrlForStandardImageSize("https://google/Picture1?s=k-s123");
        String expectedUrl = "https://google/Picture1?s=k-s256";
        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    public void shouldMatchSameWebsites(){
        String website = "https://same.website.com";
        stackUser.setWebsiteUrl(website);
        gitUser.setWebsiteUrl(website);
        assertTrue(accountsMatchScorer.isWebsitesMatching());
    }

    @Test
    public void shouldMatchWebsitesWhenStackAccountLinksGitAccount(){
        String linkToGitAccounts = "https://github.com/user";
        stackUser.setWebsiteUrl(linkToGitAccounts);
        gitUser.setHtmlUrl(linkToGitAccounts);
        assertTrue(accountsMatchScorer.isWebsitesMatching());
    }

    @Test
    public void shouldMatchWebsitesWhenGitAccountLinksStackAccount(){
        String linkToStackAccounts = "https://stackoverflow.com/user";
        stackUser.setLink(linkToStackAccounts);
        gitUser.setWebsiteUrl(linkToStackAccounts);
        assertTrue(accountsMatchScorer.isWebsitesMatching());
    }

    @Test
    public void shouldNotMatchDifferentWebsites(){
        stackUser.setLink("https://stackoverflow.com/user");
        stackUser.setWebsiteUrl("https://stackUserWebsite.com");
        gitUser.setHtmlUrl("https://github.com/user");
        gitUser.setWebsiteUrl("https://gitUserWebsite.com");
        assertFalse(accountsMatchScorer.isWebsitesMatching());
    }



    @AfterEach
    public void tearDown() {
        accountsMatchScorer = null;
        gitUser = null;
        stackUser = null;
    }

}
