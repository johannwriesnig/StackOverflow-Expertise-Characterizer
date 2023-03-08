package com.wriesnig.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoggerTest {
    private ByteArrayOutputStream systemOut;
    private ByteArrayOutputStream systemErrOut;
    private static final String toLog = "This is a test sentence.";

    @BeforeEach
    public void setUp(){
       systemOut = new ByteArrayOutputStream();
       systemErrOut = new ByteArrayOutputStream();
       System.setOut(new PrintStream(systemOut));
       System.setErr(new PrintStream(systemErrOut));
       Logger.activatePrinting();
    }

    @Test
    public void shouldPrintInfo(){
        Logger.info(toLog);
        assertTrue(systemOut.toString().contains(toLog));
    }

    @Test
    public void shouldNotPrintInfo(){
        Logger.deactivatePrinting();
        Logger.info(toLog);
        assertFalse(systemOut.toString().contains(toLog));
    }

    @Test
    public void shouldPrintError(){
        Logger.error(toLog);
        assertTrue(systemOut.toString().contains(toLog));
    }

    @Test
    public void shouldNotPrintError(){
        Logger.deactivatePrinting();
        Logger.error(toLog);
        assertFalse(systemOut.toString().contains(toLog));
    }

    @Test
    public void shouldPrintErrorAndExceptionStackTrace(){
        Exception e = spy(new Exception());
        Logger.error(toLog, e);
        assertTrue(systemOut.toString().contains(toLog));
        assertFalse(systemErrOut.toString().isEmpty());
        verify(e).printStackTrace();
    }

    @Test
    public void shouldNotPrintErrorOrException(){
        Logger.deactivatePrinting();
        Logger.error(toLog, new Exception());
        assertFalse(systemOut.toString().contains(toLog));
        assertTrue(systemErrOut.toString().isEmpty());
    }

    @Test
    public void testLoggingAfterActivateAndDeactivatePrinting(){
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
        systemErrOut = null;
    }

}
