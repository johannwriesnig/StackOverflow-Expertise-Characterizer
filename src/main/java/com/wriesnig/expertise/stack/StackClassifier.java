package com.wriesnig.expertise.stack;

class StackClassifier {

    public static double classify(Object[] i) {

        double p = Double.NaN;
        p = StackClassifier.N1983a2f4213(i);
        return p;
    }
    static double N1983a2f4213(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 0.73) {
            p = StackClassifier.N6c64ef33214(i);
        } else if (((Double) i[2]).doubleValue() > 0.73) {
            p = StackClassifier.N71a59182219(i);
        }
        return p;
    }
    static double N6c64ef33214(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 1;
        } else if (i[3].equals("0")) {
            p = StackClassifier.N3c4affbf215(i);
        } else if (i[3].equals("1")) {
            p = StackClassifier.N51af4214217(i);
        }
        return p;
    }
    static double N3c4affbf215(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 0;
        } else if (((Double) i[2]).doubleValue() <= 0.5) {
            p = StackClassifier.N553fce45216(i);
        } else if (((Double) i[2]).doubleValue() > 0.5) {
            p = 1;
        }
        return p;
    }
    static double N553fce45216(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 0;
        } else if (i[4].equals("0")) {
            p = 0;
        } else if (i[4].equals("1")) {
            p = 1;
        }
        return p;
    }
    static double N51af4214217(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 1;
        } else if (i[4].equals("0")) {
            p = StackClassifier.N1af93265218(i);
        } else if (i[4].equals("1")) {
            p = 2;
        }
        return p;
    }
    static double N1af93265218(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 0.62) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() > 0.62) {
            p = 2;
        }
        return p;
    }
    static double N71a59182219(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 2;
        } else if (i[3].equals("0")) {
            p = StackClassifier.N5ac33415220(i);
        } else if (i[3].equals("1")) {
            p = StackClassifier.N7a1d996229(i);
        }
        return p;
    }
    static double N5ac33415220(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 1.0) {
            p = StackClassifier.N71d762bb221(i);
        } else if (((Double) i[0]).doubleValue() > 1.0) {
            p = StackClassifier.N22ee0cfe222(i);
        }
        return p;
    }
    static double N71d762bb221(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 1;
        } else if (i[4].equals("0")) {
            p = 1;
        } else if (i[4].equals("1")) {
            p = 2;
        }
        return p;
    }
    static double N22ee0cfe222(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 3;
        } else if (((Double) i[1]).doubleValue() <= 0.0) {
            p = StackClassifier.N7f9ab0a0223(i);
        } else if (((Double) i[1]).doubleValue() > 0.0) {
            p = StackClassifier.N3cba50da224(i);
        }
        return p;
    }
    static double N7f9ab0a0223(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 2;
        } else if (i[4].equals("0")) {
            p = 2;
        } else if (i[4].equals("1")) {
            p = 3;
        }
        return p;
    }
    static double N3cba50da224(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 2;
        } else if (i[4].equals("0")) {
            p = StackClassifier.N76323f4c225(i);
        } else if (i[4].equals("1")) {
            p = StackClassifier.N600e4284228(i);
        }
        return p;
    }
    static double N76323f4c225(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 0.86) {
            p = StackClassifier.N24695488226(i);
        } else if (((Double) i[2]).doubleValue() > 0.86) {
            p = 2;
        }
        return p;
    }
    static double N24695488226(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() <= 1.0) {
            p = StackClassifier.Ne5cff15227(i);
        } else if (((Double) i[1]).doubleValue() > 1.0) {
            p = 1;
        }
        return p;
    }
    static double Ne5cff15227(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 3.0) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 3.0) {
            p = 2;
        }
        return p;
    }
    static double N600e4284228(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 7.0) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 7.0) {
            p = 3;
        }
        return p;
    }
    static double N7a1d996229(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 2;
        } else if (i[4].equals("0")) {
            p = StackClassifier.N690200fe230(i);
        } else if (i[4].equals("1")) {
            p = StackClassifier.N4ffe9c231(i);
        }
        return p;
    }
    static double N690200fe230(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 14.0) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 14.0) {
            p = 3;
        }
        return p;
    }
    static double N4ffe9c231(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 3;
        } else if (((Double) i[0]).doubleValue() <= 1.0) {
            p = 3;
        } else if (((Double) i[0]).doubleValue() > 1.0) {
            p = StackClassifier.N12479673232(i);
        }
        return p;
    }
    static double N12479673232(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 3;
        } else if (((Double) i[2]).doubleValue() <= 0.83) {
            p = 3;
        } else if (((Double) i[2]).doubleValue() > 0.83) {
            p = 4;
        }
        return p;
    }
}



