package com.wriesnig.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

public class Logger {
    private static final File logFile = new File("log.txt");
    private static final FileWriter writer;

    static {
        try {
            writer = new FileWriter(logFile);
        } catch (IOException e) {
            throw new RuntimeException("Issues with log file",e);
        }
    }

    private Logger(){}

    public static void error(String message, Throwable stackTrace){
        try {
            writer.write(getTimeStamp() + " [ERROR]: " + message + "\n");
            writer.write("\t" + stackTrace);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Issues writing to log file",e);
        }
    }

    public static void error(String message){
        try {
            writer.write(getTimeStamp() + " [ERROR]: " + message + "\n");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Issues writing to log file",e);
        }
    }

    public static void info(String message){
        try {
            writer.write(getTimeStamp() + " [INFO]: " + message + "\n");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Issues writing to log file",e);
        }
    }

    private static String getTimeStamp(){
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        int millis = now.get(ChronoField.MILLI_OF_SECOND);

        return String.format("%02d:%02d:%02d.%03d", hour, minute, second, millis);
    }
}
