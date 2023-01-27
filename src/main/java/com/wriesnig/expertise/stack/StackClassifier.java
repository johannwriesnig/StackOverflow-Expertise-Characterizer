package com.wriesnig.expertise.stack;

public class StackClassifier {
    public static double classify(Object[] i){

        double p = Double.NaN;
        p = StackClassifier.N2b90198c0(i);
        return p;
    }
    static double N2b90198c0(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 1.0) {
            p = StackClassifier.N6a463a9e1(i);
        } else if (((Double) i[0]).doubleValue() > 1.0) {
            p = StackClassifier.N2affbc009(i);
        }
        return p;
    }
    static double N6a463a9e1(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 0;
        } else if (((Double) i[1]).doubleValue() <= 1.0) {
            p = StackClassifier.N3e0633332(i);
        } else if (((Double) i[1]).doubleValue() > 1.0) {
            p = StackClassifier.N3bd439497(i);
        }
        return p;
    }
    static double N3e0633332(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 0;
        } else if (i[3].equals("0")) {
            p = StackClassifier.N1aa8948d3(i);
        } else if (i[3].equals("1")) {
            p = StackClassifier.N6a96fc525(i);
        }
        return p;
    }
    static double N1aa8948d3(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 0.0) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 0.0) {
            p = StackClassifier.N315fa22d4(i);
        }
        return p;
    }
    static double N315fa22d4(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 0;
        } else if (i[4].equals("0")) {
            p = 0;
        } else if (i[4].equals("1")) {
            p = 3;
        }
        return p;
    }
    static double N6a96fc525(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 3;
        } else if (i[4].equals("0")) {
            p = StackClassifier.N6506c5986(i);
        } else if (i[4].equals("1")) {
            p = 4;
        }
        return p;
    }
    static double N6506c5986(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 0.0) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 0.0) {
            p = 3;
        }
        return p;
    }
    static double N3bd439497(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 1;
        } else if (i[3].equals("0")) {
            p = StackClassifier.N7756aad48(i);
        } else if (i[3].equals("1")) {
            p = 3;
        }
        return p;
    }
    static double N7756aad48(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 0.1) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() > 0.1) {
            p = 2;
        }
        return p;
    }
    static double N2affbc009(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() <= 0.78) {
            p = StackClassifier.N798686c210(i);
        } else if (((Double) i[2]).doubleValue() > 0.78) {
            p = StackClassifier.N6382f6ca11(i);
        }
        return p;
    }
    static double N798686c210(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 2;
        } else if (((Double) i[1]).doubleValue() <= 5.0) {
            p = 2;
        } else if (((Double) i[1]).doubleValue() > 5.0) {
            p = 1;
        }
        return p;
    }
    static double N6382f6ca11(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 3;
        } else if (i[4].equals("0")) {
            p = StackClassifier.N2140e06412(i);
        } else if (i[4].equals("1")) {
            p = StackClassifier.N4fb5ea6715(i);
        }
        return p;
    }
    static double N2140e06412(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 3;
        } else if (i[3].equals("0")) {
            p = StackClassifier.N760847f013(i);
        } else if (i[3].equals("1")) {
            p = StackClassifier.N3b756f6514(i);
        }
        return p;
    }
    static double N760847f013(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 2;
        } else if (((Double) i[1]).doubleValue() <= 0.0) {
            p = 2;
        } else if (((Double) i[1]).doubleValue() > 0.0) {
            p = 3;
        }
        return p;
    }
    static double N3b756f6514(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 3;
        } else if (((Double) i[0]).doubleValue() <= 2.0) {
            p = 3;
        } else if (((Double) i[0]).doubleValue() > 2.0) {
            p = 4;
        }
        return p;
    }
    static double N4fb5ea6715(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 4;
        } else if (i[3].equals("0")) {
            p = StackClassifier.N69d0603516(i);
        } else if (i[3].equals("1")) {
            p = 5;
        }
        return p;
    }
    static double N69d0603516(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 4;
        } else if (((Double) i[0]).doubleValue() <= 5.0) {
            p = 4;
        } else if (((Double) i[0]).doubleValue() > 5.0) {
            p = 5;
        }
        return p;
    }
}
