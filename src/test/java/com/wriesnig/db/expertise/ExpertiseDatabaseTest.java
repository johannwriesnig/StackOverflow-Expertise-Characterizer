package com.wriesnig.db.expertise;

import com.wriesnig.api.stack.StackApi;
import com.wriesnig.expertise.Tags;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExpertiseDatabaseTest {

    @Test
    public void getInsertUserStatement(){
        Tags.tagsToCharacterize = new String[]{"tag1", "tag2"};
        String insertStatement = ExpertiseDatabase.getInsertUserStatement();
        assertEquals("INSERT INTO Users (id, stackId, gitId, profileImageUrl,tag1,tag2) VALUES (?,?,?,?,?,?);", insertStatement);
    }

    @Test
    public void initDBConnectionFail(){
        try(MockedStatic<DriverManager> mocked = mockStatic(DriverManager.class)){
            mocked.when(()->DriverManager.getConnection(anyString(),anyString(),anyString())).thenThrow(SQLException.class);

            assertThrows(RuntimeException.class, ExpertiseDatabase::initDB);
        }
    }

    @Test
    public void isCredentialsSet(){
        String user = "user";
        String password = "password";
        String url = "url";

        ExpertiseDatabase.setCredentials(user, password, url);

        assertTrue(ExpertiseDatabase.isCredentialsSet());
    }
}
