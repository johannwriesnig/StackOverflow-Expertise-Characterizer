package com.wriesnig.sodumpsconvert.datainfo;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class UsersInfo implements DataInfo{
    private final ArrayList<Pair<String, AttributeType>> data_attributes = new ArrayList<>(Arrays.asList(
            new Pair<>("Id", AttributeType.INTEGER),
            new Pair<>("Reputation", AttributeType.INTEGER),
            new Pair<>("DisplayName", AttributeType.STRING),
            new Pair<>("WebsiteUrl", AttributeType.STRING),
            new Pair<>("Location", AttributeType.STRING),
            new Pair<>("ProfileImage", AttributeType.STRING),
            new Pair<>("AccountId", AttributeType.INTEGER)
    ));

    @Override
    public ArrayList<Pair<String, AttributeType>> getData_attributes() {
        return data_attributes;
    }

    @Override
    public String getDataName() {
        return "Users";
    }
}
