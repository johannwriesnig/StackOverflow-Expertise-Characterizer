package com.wriesnig.expertise;

import com.wriesnig.api.git.GitUser;
import com.wriesnig.api.stack.StackUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private final GitUser gitUser = new GitUser("gitUser", "https://gitUser.com", "git User", "https://github.com/gitUser", "https://mywebsite.com");
    private final StackUser stackUser = new StackUser(34, 30000, "stack User", "https://test.com", "https://stackoverflow.com/34/stack+User","https://stackUser.com",20);
    private User user;

    @BeforeEach
    public void setUp(){
        user = new User(stackUser, gitUser);
    }

    @Test
    public void getStackDisplayName(){
        assertEquals(stackUser.getDisplayName(), user.getStackDisplayName());
    }

    @Test
    public void getGitLogin(){
        assertEquals(gitUser.getLogin(), user.getGitLogin());
    }

    @Test
    public void getExpertise(){
        Expertise expertise = new Expertise();
        user.setExpertise(expertise);
        assertEquals(expertise, user.getExpertise());
    }

    @Test
    public void isUserEstablished(){
        assertTrue( user.getIsEstablishedOnStack());
    }

    @Test
    public void getMainTags(){
        ArrayList<String> mainTags = new ArrayList<>();
        mainTags.add("tag1");
        mainTags.add("tag2");
        stackUser.setMainTags(mainTags);
        assertEquals(mainTags, user.getMainTags());
    }

    @Test
    public void getProfileImageUrl(){
        assertEquals(stackUser.getProfileImageUrl(), user.getProfileImageUrl());
    }

    @AfterEach
    public void tearDown(){
        user = null;
    }
}
