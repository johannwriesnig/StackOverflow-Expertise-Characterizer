package com.wriesnig.db.stack.stackdumpsconvert.datainfo;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;

public class PostsInfo implements DataInfo{
    private final ArrayList<Pair<String, AttributeType>> dataAttributes = new ArrayList<>(Arrays.asList(
            new Pair<>("Id", AttributeType.INTEGER),
            new Pair<>("PostTypeId", AttributeType.INTEGER),
            new Pair<>("ParentId",AttributeType.INTEGER),
            new Pair<>("AcceptedAnswerId", AttributeType.INTEGER),
            new Pair<>("OwnerUserId", AttributeType.INTEGER),
            new Pair<>("Tags", AttributeType.STRING)
    ));
    @Override
    public ArrayList<Pair<String, AttributeType>> getDataAttributes() {
        return dataAttributes;
    }

    @Override
    public String getDataName() {
        return "Posts";
    }


}
