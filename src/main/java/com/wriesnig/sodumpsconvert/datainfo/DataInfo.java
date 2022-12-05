package com.wriesnig.sodumpsconvert.datainfo;

import javafx.util.Pair;

import java.util.ArrayList;

public interface DataInfo {
    ArrayList<Pair<String, AttributeType>> getData_attributes();

    String getDataName();
}
