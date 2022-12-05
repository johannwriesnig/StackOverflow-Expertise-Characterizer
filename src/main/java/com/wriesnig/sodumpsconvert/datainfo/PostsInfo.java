package com.wriesnig.sodumpsconvert.datainfo;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class PostsInfo implements DataInfo{
    private final ArrayList<Pair<String, AttributeType>> data_attributes = new ArrayList<>(Arrays.asList(
            new Pair<>("Id", AttributeType.INTEGER),
            new Pair<>("PostTypeId", AttributeType.INTEGER),
            new Pair<>("ParentId",AttributeType.INTEGER),
            new Pair<>("AcceptedAnswerId", AttributeType.INTEGER),
            new Pair<>("Score", AttributeType.INTEGER),
            new Pair<>("ViewCount", AttributeType.INTEGER),
            new Pair<>("Body", AttributeType.STRING),
            new Pair<>("OwnerUserId", AttributeType.INTEGER),
            new Pair<>("Title", AttributeType.STRING),
            new Pair<>("Tags", AttributeType.STRING)
    ));
    @Override
    public ArrayList<Pair<String, AttributeType>> getData_attributes() {
        return data_attributes;
    }

    @Override
    public String getDataName() {
        return "Posts";
    }


}
