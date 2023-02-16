package com.wriesnig.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

public class Logger {
    private static boolean shouldPrint = true;

    private Logger() {
    }

    public static void deactivatePrinting() {
        shouldPrint = false;
    }

    public static void error(String message, Throwable throwable) {
        if (!shouldPrint) return;
        System.out.println(getTimeStamp() + " [ERROR]: " + message);
        throwable.printStackTrace();
    }

    public static void error(String message) {
        if (!shouldPrint) return;
        System.out.println(getTimeStamp() + " [ERROR]: " + message);
    }

    public static void info(String message) {
        if (!shouldPrint) return;
        System.out.println(getTimeStamp() + " [INFO]: " + message);
    }

    private static String getTimeStamp() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        int millis = now.get(ChronoField.MILLI_OF_SECOND);

        return String.format("%02d:%02d:%02d.%03d", hour, minute, second, millis);
    }
}
