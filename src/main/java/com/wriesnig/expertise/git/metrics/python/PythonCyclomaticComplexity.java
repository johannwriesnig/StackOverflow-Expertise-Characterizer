package com.wriesnig.expertise.git.metrics.python;

import com.wriesnig.expertise.git.metrics.CyclomaticComplexity;

import java.io.File;

public class PythonCyclomaticComplexity extends CyclomaticComplexity {
    public PythonCyclomaticComplexity(File root) {
        super(root);
    }

    @Override
    public double getProjectComplexity() {

        return 0;
    }
}
