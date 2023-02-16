package com.wriesnig.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoggerTest {
    private ByteArrayOutputStream systemOut;
    private static final String toLog = "This is a test sentence.";

    @BeforeEach
    public void setUp(){
       systemOut = new ByteArrayOutputStream();
       System.setOut(new PrintStream(systemOut));
       Logger.activatePrinting();
    }

    @Test
    public void logInfo(){
        Logger.info(toLog);
        assertTrue(systemOut.toString().contains(toLog));
    }

    @Test
    public void deactivatePrintingAndLogInfo(){
        Logger.deactivatePrinting();
        Logger.info(toLog);
        assertFalse(systemOut.toString().contains(toLog));
    }

    @Test
    public void logError(){
        Logger.error(toLog);
        assertTrue(systemOut.toString().contains(toLog));
    }

    @Test
    public void deactivatePrintingAndLogError(){
        Logger.deactivatePrinting();
        Logger.error(toLog);
        assertFalse(systemOut.toString().contains(toLog));
    }

    @Test
    public void logErrorWithException(){
        Exception e = spy(new Exception());
        Logger.error(toLog, e);
        assertTrue(systemOut.toString().contains(toLog));
        verify(e).printStackTrace();
    }

    @Test
    public void deactivatePrintingAndLogErrorWithException(){
        Logger.deactivatePrinting();
        Logger.error(toLog, new Exception());
        assertFalse(systemOut.toString().contains(toLog));
    }
    @Test
    public void loggingAfterUsingActivateAndDeactivatePrinting(){
        Logger.deactivatePrinting();
        Logger.activatePrinting();
        Logger.info(toLog);
        assertTrue(systemOut.toString().contains(toLog));
        systemOut.reset();
        Logger.deactivatePrinting();
        Logger.info(toLog);
        assertFalse(systemOut.toString().contains(toLog));
    }


    @BeforeEach
    public void tearDown(){
        systemOut = null;
    }

}
