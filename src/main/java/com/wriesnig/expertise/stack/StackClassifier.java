package com.wriesnig.expertise.stack;

public class StackClassifier {

    public static double classify(Object[] i) {

        double p = Double.NaN;
        p = StackClassifier.N5aa51d970(i);
        return p;
    }
    static double N5aa51d970(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 1.0) {
            p = StackClassifier.N30e3fb181(i);
        } else if (((Double) i[0]).doubleValue() > 1.0) {
            p = StackClassifier.N66acd15511(i);
        }
        return p;
    }
    static double N30e3fb181(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 0;
        } else if (i[3].equals("0")) {
            p = StackClassifier.N72066c712(i);
        } else if (i[3].equals("1")) {
            p = StackClassifier.N5587ab46(i);
        }
        return p;
    }
    static double N72066c712(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 0;
        } else if (i[5].equals("0")) {
            p = 0;
        } else if (i[5].equals("1")) {
            p = StackClassifier.N195311e83(i);
        }
        return p;
    }
    static double N195311e83(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 0;
        } else if (i[4].equals("0")) {
            p = 0;
        } else if (i[4].equals("1")) {
            p = StackClassifier.N6be8eef04(i);
        }
        return p;
    }
    static double N6be8eef04(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 0.38) {
            p = StackClassifier.N447ce3ae5(i);
        } else if (((Double) i[2]).doubleValue() > 0.38) {
            p = 2;
        }
        return p;
    }
    static double N447ce3ae5(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() <= 2.0) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() > 2.0) {
            p = 0;
        }
        return p;
    }
    static double N5587ab46(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 2;
        } else if (i[4].equals("0")) {
            p = StackClassifier.N485e7a67(i);
        } else if (i[4].equals("1")) {
            p = StackClassifier.N283606a88(i);
        }
        return p;
    }
    static double N485e7a67(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 0.0) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 0.0) {
            p = 2;
        }
        return p;
    }
    static double N283606a88(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 0.0) {
            p = StackClassifier.Nb74e1939(i);
        } else if (((Double) i[0]).doubleValue() > 0.0) {
            p = 3;
        }
        return p;
    }
    static double Nb74e1939(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 2;
        } else if (i[5].equals("0")) {
            p = 2;
        } else if (i[5].equals("1")) {
            p = StackClassifier.N4dbfa79310(i);
        }
        return p;
    }
    static double N4dbfa79310(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 3;
        } else if (((Double) i[1]).doubleValue() <= 1.0) {
            p = 3;
        } else if (((Double) i[1]).doubleValue() > 1.0) {
            p = 2;
        }
        return p;
    }
    static double N66acd15511(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 0.78) {
            p = StackClassifier.N7eb7986e12(i);
        } else if (((Double) i[2]).doubleValue() > 0.78) {
            p = StackClassifier.N20c0e30c16(i);
        }
        return p;
    }
    static double N7eb7986e12(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() <= 5.0) {
            p = StackClassifier.N52e3965213(i);
        } else if (((Double) i[1]).doubleValue() > 5.0) {
            p = 0;
        }
        return p;
    }
    static double N52e3965213(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 1;
        } else if (i[3].equals("0")) {
            p = StackClassifier.N625020a214(i);
        } else if (i[3].equals("1")) {
            p = 2;
        }
        return p;
    }
    static double N625020a214(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() <= 3.0) {
            p = StackClassifier.N627673c415(i);
        } else if (((Double) i[1]).doubleValue() > 3.0) {
            p = 1;
        }
        return p;
    }
    static double N627673c415(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 4.0) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 4.0) {
            p = 1;
        }
        return p;
    }
    static double N20c0e30c16(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 2;
        } else if (i[4].equals("0")) {
            p = StackClassifier.N6edfa04017(i);
        } else if (i[4].equals("1")) {
            p = StackClassifier.N2b0b643622(i);
        }
        return p;
    }
    static double N6edfa04017(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 2;
        } else if (i[3].equals("0")) {
            p = StackClassifier.Nd69d80918(i);
        } else if (i[3].equals("1")) {
            p = StackClassifier.N7b2f223620(i);
        }
        return p;
    }
    static double Nd69d80918(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() <= 0.0) {
            p = StackClassifier.N5c50623319(i);
        } else if (((Double) i[1]).doubleValue() > 0.0) {
            p = 2;
        }
        return p;
    }
    static double N5c50623319(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 2;
        } else if (i[5].equals("0")) {
            p = 2;
        } else if (i[5].equals("1")) {
            p = 1;
        }
        return p;
    }
    static double N7b2f223620(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 2.0) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 2.0) {
            p = StackClassifier.N68e150b321(i);
        }
        return p;
    }
    static double N68e150b321(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 3;
        } else if (((Double) i[1]).doubleValue() <= 0.0) {
            p = 3;
        } else if (((Double) i[1]).doubleValue() > 0.0) {
            p = 2;
        }
        return p;
    }
    static double N2b0b643622(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 3;
        } else if (i[3].equals("0")) {
            p = StackClassifier.N7415da9e23(i);
        } else if (i[3].equals("1")) {
            p = StackClassifier.N6475266126(i);
        }
        return p;
    }
    static double N7415da9e23(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 3;
        } else if (((Double) i[0]).doubleValue() <= 5.0) {
            p = StackClassifier.N762978b824(i);
        } else if (((Double) i[0]).doubleValue() > 5.0) {
            p = StackClassifier.N142fda0625(i);
        }
        return p;
    }
    static double N762978b824(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 2;
        } else if (i[5].equals("0")) {
            p = 2;
        } else if (i[5].equals("1")) {
            p = 3;
        }
        return p;
    }
    static double N142fda0625(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 3;
        } else if (i[5].equals("0")) {
            p = 3;
        } else if (i[5].equals("1")) {
            p = 4;
        }
        return p;
    }
    static double N6475266126(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 3;
        } else if (i[5].equals("0")) {
            p = 3;
        } else if (i[5].equals("1")) {
            p = 4;
        }
        return p;
    }
}
