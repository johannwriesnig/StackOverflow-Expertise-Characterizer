package com.wriesnig.sodumpsconvert.datainfo;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class VotesInfo implements DataInfo{
    private final ArrayList<Pair<String, AttributeType>> data_attributes = new ArrayList<>(Arrays.asList(
            new Pair<>("Id", AttributeType.INTEGER),
            new Pair<>("PostId", AttributeType.INTEGER),
            new Pair<>("VoteTypeId", AttributeType.INTEGER),
            new Pair<>("UserId", AttributeType.INTEGER)
    ));
    @Override
    public ArrayList<Pair<String, AttributeType>> getData_attributes() {
        return data_attributes;
    }

    @Override
    public String getDataName() {
        return "Votes";
    }
}
