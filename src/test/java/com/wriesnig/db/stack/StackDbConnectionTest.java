package com.wriesnig.db.stack;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;

public class StackDbConnectionTest {
    @Test
    public void initDBConnectionFail(){
        try(MockedStatic<DriverManager> mocked = mockStatic(DriverManager.class)){
            mocked.when(()->DriverManager.getConnection(anyString(),anyString(),anyString())).thenThrow(SQLException.class);

            assertThrows(RuntimeException.class, ()-> new StackDbConnection("","",""));
        }
    }
}
