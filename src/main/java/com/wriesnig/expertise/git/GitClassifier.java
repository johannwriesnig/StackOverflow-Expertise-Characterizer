package com.wriesnig.expertise.git;

class GitClassifier {

    public static double classify(Object[] i) {

        double p = Double.NaN;
        p = GitClassifier.N56b562660(i);
        return p;
    }
    static double N56b562660(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 3;
        } else if (i[1].equals("false")) {
            p = GitClassifier.N655bdf651(i);
        } else if (i[1].equals("true")) {
            p = GitClassifier.N217e98542(i);
        }
        return p;
    }
    static double N655bdf651(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 1;
        } else if (i[3].equals("false")) {
            p = 1;
        } else if (i[3].equals("true")) {
            p = 3;
        }
        return p;
    }
    static double N217e98542(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 4;
        } else if (((Double) i[0]).doubleValue() <= 6.0) {
            p = 4;
        } else if (((Double) i[0]).doubleValue() > 6.0) {
            p = 2;
        }
        return p;
    }
}
