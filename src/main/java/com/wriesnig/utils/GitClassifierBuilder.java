package com.wriesnig.utils;

import java.io.FileWriter;
import java.io.IOException;

public class GitClassifierBuilder {
    private static FileWriter writer;

    static {
        try {
            writer = new FileWriter("gitClassifierData.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static synchronized void writeLine(String string){
        try {
            writer.write(string+"\n");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
