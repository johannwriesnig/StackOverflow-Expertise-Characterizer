package com.wriesnig.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class GitClassifierBuilder {

    public static synchronized void writeLine(String string){
        try {
            Files.write(Paths.get("gitClassifierData.txt"), (string+"\n").getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }
}
