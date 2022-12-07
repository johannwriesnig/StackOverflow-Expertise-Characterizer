package com.wriesnig;

import com.wriesnig.stackoverflow.db.SODatabase;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        //Wie UI aussieht wird noch entschieden
        //Deshalb erstmal mit ausgewählten Usern, hier sind es die Top SO-User
        //Still todo: error handling, code cleanen, api rates, vllt auf spring verzichten

        //SODatabase.initDB();
        SODatabase.initDB("postgres", "123", "jdbc:postgresql://localhost:5432/StackOverflow");
        CharacterizerApplication application = new CharacterizerApplication();
        application.run();

    }
}