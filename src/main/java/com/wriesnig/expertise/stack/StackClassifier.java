package com.wriesnig.expertise.stack;

class StackClassifier {

    public static double classify(Object[] i) {
        double p = Double.NaN;
        p = StackClassifier.N1150919a0(i);
        return p;
    }
    static double N1150919a0(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 0.73) {
            p = StackClassifier.N38fe96551(i);
        } else if (((Double) i[2]).doubleValue() > 0.73) {
            p = StackClassifier.N13b3dc1a6(i);
        }
        return p;
    }
    static double N38fe96551(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 1;
        } else if (i[3].equals("false")) {
            p = StackClassifier.N2c98e6ad2(i);
        } else if (i[3].equals("true")) {
            p = StackClassifier.N347594c24(i);
        }
        return p;
    }
    static double N2c98e6ad2(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 0;
        } else if (((Double) i[2]).doubleValue() <= 0.5) {
            p = StackClassifier.N213c0d3e3(i);
        } else if (((Double) i[2]).doubleValue() > 0.5) {
            p = 1;
        }
        return p;
    }
    static double N213c0d3e3(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 0;
        } else if (i[4].equals("false")) {
            p = 0;
        } else if (i[4].equals("true")) {
            p = 1;
        }
        return p;
    }
    static double N347594c24(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 1;
        } else if (i[4].equals("false")) {
            p = StackClassifier.N78f5f0275(i);
        } else if (i[4].equals("true")) {
            p = 2;
        }
        return p;
    }
    static double N78f5f0275(Object []i) {
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
    static double N13b3dc1a6(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 2;
        } else if (i[3].equals("false")) {
            p = StackClassifier.N522766af7(i);
        } else if (i[3].equals("true")) {
            p = StackClassifier.N55b8362216(i);
        }
        return p;
    }
    static double N522766af7(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 1.0) {
            p = StackClassifier.N4aec3bad8(i);
        } else if (((Double) i[0]).doubleValue() > 1.0) {
            p = StackClassifier.N1d4f8e1f9(i);
        }
        return p;
    }
    static double N4aec3bad8(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 1;
        } else if (i[4].equals("false")) {
            p = 1;
        } else if (i[4].equals("true")) {
            p = 2;
        }
        return p;
    }
    static double N1d4f8e1f9(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 3;
        } else if (((Double) i[1]).doubleValue() <= 0.0) {
            p = StackClassifier.N1011c2a010(i);
        } else if (((Double) i[1]).doubleValue() > 0.0) {
            p = StackClassifier.N796adc1311(i);
        }
        return p;
    }
    static double N1011c2a010(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 2;
        } else if (i[4].equals("false")) {
            p = 2;
        } else if (i[4].equals("true")) {
            p = 3;
        }
        return p;
    }
    static double N796adc1311(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 2;
        } else if (i[4].equals("false")) {
            p = StackClassifier.N5270a1c012(i);
        } else if (i[4].equals("true")) {
            p = StackClassifier.N291f91115(i);
        }
        return p;
    }
    static double N5270a1c012(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 0.86) {
            p = StackClassifier.N33aef2e713(i);
        } else if (((Double) i[2]).doubleValue() > 0.86) {
            p = 2;
        }
        return p;
    }
    static double N33aef2e713(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() <= 1.0) {
            p = StackClassifier.N2db432f014(i);
        } else if (((Double) i[1]).doubleValue() > 1.0) {
            p = 1;
        }
        return p;
    }
    static double N2db432f014(Object []i) {
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
    static double N291f91115(Object []i) {
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
    static double N55b8362216(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 2;
        } else if (i[4].equals("false")) {
            p = StackClassifier.N11c5dfb817(i);
        } else if (i[4].equals("true")) {
            p = StackClassifier.N1d6a5cc218(i);
        }
        return p;
    }
    static double N11c5dfb817(Object []i) {
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
    static double N1d6a5cc218(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 3;
        } else if (((Double) i[0]).doubleValue() <= 1.0) {
            p = 3;
        } else if (((Double) i[0]).doubleValue() > 1.0) {
            p = StackClassifier.N103e770f19(i);
        }
        return p;
    }
    static double N103e770f19(Object []i) {
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



