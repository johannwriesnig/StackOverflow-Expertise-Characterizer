package com.wriesnig.expertise.git;

class GitClassifier {

    public static double classify(Object[] i) {

        double p = Double.NaN;
        p = GitClassifier.N592d7e1e0(i);
        return p;
    }
    static double N592d7e1e0(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 0;
        } else if (((Double) i[3]).doubleValue() <= 1.0) {
            p = 0;
        } else if (((Double) i[3]).doubleValue() > 1.0) {
            p = GitClassifier.N1402042a1(i);
        }
        return p;
    }
    static double N1402042a1(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 4;
        } else if (((Double) i[3]).doubleValue() <= 90.0) {
            p = 4;
        } else if (((Double) i[3]).doubleValue() > 90.0) {
            p = 3;
        }
        return p;
    }
}
