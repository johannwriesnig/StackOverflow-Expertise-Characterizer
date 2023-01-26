package com.wriesnig.expertise.git.metrics.java;

import com.wriesnig.expertise.git.metrics.CyclomaticComplexity;
import java.io.File;

public class JavaCyclomaticComplexity extends CyclomaticComplexity {
    public JavaCyclomaticComplexity(File root) {
        super(root);
    }

    @Override
    public double getProjectComplexity() {
        return 0;
    }

}
