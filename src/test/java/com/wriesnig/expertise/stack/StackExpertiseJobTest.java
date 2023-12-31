package com.wriesnig.expertise.stack;

import com.wriesnig.api.git.DefaultGitUser;
import com.wriesnig.api.stack.StackUser;
import com.wriesnig.expertise.Tags;
import com.wriesnig.expertise.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class StackExpertiseJobTest {
    private StackExpertiseJob stackExpertiseJob;
    private User user;

    @BeforeEach
    public void setUp(){
        user = new User(new StackUser(1,1,"user", "","","",1), new DefaultGitUser());
        stackExpertiseJob = new StackExpertiseJob(user);
    }

    @Test
    public void getEmptyScoresPerTag(){
        Tags.tagsToCharacterize = new String[]{"tag1","tag2"};
        HashMap<String, ArrayList<Double>> scoresPerTag = stackExpertiseJob.getEmptyScoresPerTag();
        assertEquals(Tags.tagsToCharacterize.length, scoresPerTag.keySet().size());
        for(String tag: Tags.tagsToCharacterize){
            assertTrue(scoresPerTag.containsKey(tag));
            assertEquals(0, scoresPerTag.get(tag).size());
        }
    }

    @Test
    public void postTagsContainTagsToCharacterize(){
        Tags.tagsToCharacterize = new String[]{"java","ruby"};
        String postTags = "<java><c#><python>";
        assertTrue(stackExpertiseJob.postTagsContainTagsToCharacterize(postTags));
    }

    @Test
    public void getScoresPerTagFromPosts() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getInt("upVotes")).thenReturn(5);
        when(resultSet.getInt("downVotes")).thenReturn(0);
        when(resultSet.getString("isAccepted")).thenReturn("1");
        when(resultSet.getString("tags")).thenReturn("<java>");
        Tags.tagsToCharacterize = new String[]{"java"};

        try(MockedStatic<StackClassifier> mockedClassifier = mockStatic(StackClassifier.class)){
            HashMap<String, ArrayList<Double>> scoresPerTag = stackExpertiseJob.getScoresPerTagFromPosts(resultSet);
            assertEquals(1, scoresPerTag.keySet().size());
            assertTrue(scoresPerTag.containsKey("java"));
            mockedClassifier.verify(()->StackClassifier.classify(any()), times(1));
        }
    }

    @Test
    public void setUsersExpertise(){
        ArrayList<Double> avg3dot5 = new ArrayList<>();
        avg3dot5.add(5.0);
        avg3dot5.add(3.0);
        avg3dot5.add(2.0);
        avg3dot5.add(4.0);
        ArrayList<Double> avg2dot2 = new ArrayList<>();
        avg2dot2.add(2.0);
        avg2dot2.add(1.0);
        avg2dot2.add(1.0);
        avg2dot2.add(4.0);
        avg2dot2.add(3.0);

        HashMap<String, ArrayList<Double>> scoresPerTag = new HashMap<>();
        scoresPerTag.put("java", avg3dot5);
        scoresPerTag.put("python", avg2dot2);

        stackExpertiseJob.setUsersExpertise(scoresPerTag);
        HashMap<String, Double> expertise = user.getExpertise().getStackExpertise();
        assertEquals(2.2, expertise.get("python"));
        assertEquals(3.5, expertise.get("java"));
    }

    @AfterEach
    public void tearDown(){

    }
}
