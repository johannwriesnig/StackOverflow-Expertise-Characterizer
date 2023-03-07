package com.wriesnig.db.stack.stackdumpsconvert.datainfo;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;

public class UsersInfo implements DataInfo{
    private final ArrayList<Pair<String, AttributeType>> dataAttributes = new ArrayList<>(Arrays.asList(
            new Pair<>("Id", AttributeType.INTEGER),
            new Pair<>("DisplayName", AttributeType.STRING),
            new Pair<>("AccountId", AttributeType.INTEGER)
    ));

    @Override
    public ArrayList<Pair<String, AttributeType>> getDataAttributes() {
        return dataAttributes;
    }

    @Override
    public String getDataName() {
        return "Users";
    }
}
