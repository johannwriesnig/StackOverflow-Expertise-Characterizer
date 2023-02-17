package com.wriesnig.db.expertise;

import com.wriesnig.api.stack.StackApi;
import com.wriesnig.expertise.Tags;
import com.wriesnig.utils.Logger;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.postgresql.PGConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
    public void initDBConnectionFailsDueGetConnection(){
        try(MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class);
            MockedStatic<Logger> mockedLogger = mockStatic(Logger.class)){
            mockedDriverManager.when(()->DriverManager.getConnection(any(),any(),any())).thenThrow(SQLException.class);

            assertThrows(RuntimeException.class, ExpertiseDatabase::initDB);
            mockedLogger.verify(()-> Logger.error(any(), any()),times(1));
        }
    }

    @Test
    public void closeConnectionFails(){
        try(MockedStatic<Logger> mockedLogger = mockStatic(Logger.class)){

            ExpertiseDatabase.closeConnection();
            mockedLogger.verify(()-> Logger.error(any(), any()),times(1));
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
