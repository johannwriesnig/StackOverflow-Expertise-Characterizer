package com.wriesnig.db.stack.stackdumpsconvert.datainfo;

import javafx.util.Pair;
import java.util.ArrayList;

public interface DataInfo {
    ArrayList<Pair<String, AttributeType>> getDataAttributes();

    String getDataName();
}
