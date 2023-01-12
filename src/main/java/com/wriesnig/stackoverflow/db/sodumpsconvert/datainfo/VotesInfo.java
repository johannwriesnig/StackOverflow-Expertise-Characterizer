package com.wriesnig.stackoverflow.db.sodumpsconvert.datainfo;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;

public class VotesInfo implements DataInfo{
    private final ArrayList<Pair<String, AttributeType>> dataAttributes = new ArrayList<>(Arrays.asList(
            new Pair<>("Id", AttributeType.INTEGER),
            new Pair<>("PostId", AttributeType.INTEGER),
            new Pair<>("VoteTypeId", AttributeType.INTEGER),
            new Pair<>("UserId", AttributeType.INTEGER)
    ));
    @Override
    public ArrayList<Pair<String, AttributeType>> getDataAttributes() {
        return dataAttributes;
    }

    @Override
    public String getDataName() {
        return "Votes";
    }
}
