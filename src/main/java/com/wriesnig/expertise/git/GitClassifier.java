package com.wriesnig.expertise.git;

class GitClassifier {
    public static double classify(Object[] i) {

        double p = Double.NaN;
        p = GitClassifier.N3a542b7735(i);
        return p;
    }
    static double N3a542b7735(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 522.0) {
            p = GitClassifier.N5618148d36(i);
        } else if (((Double) i[2]).doubleValue() > 522.0) {
            p = GitClassifier.N34e388e052(i);
        }
        return p;
    }
    static double N5618148d36(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 2;
        } else if (i[1].equals("false")) {
            p = GitClassifier.N34119e0437(i);
        } else if (i[1].equals("true")) {
            p = GitClassifier.N5274750346(i);
        }
        return p;
    }
    static double N34119e0437(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 1;
        } else if (i[4].equals("false")) {
            p = 1;
        } else if (i[4].equals("true")) {
            p = GitClassifier.N143f1cb438(i);
        }
        return p;
    }
    static double N143f1cb438(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 1;
        } else if (i[3].equals("false")) {
            p = GitClassifier.Nf2b92d739(i);
        } else if (i[3].equals("true")) {
            p = GitClassifier.N1e80f32a43(i);
        }
        return p;
    }
    static double Nf2b92d739(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 223.0) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() > 223.0) {
            p = GitClassifier.N198143c340(i);
        }
        return p;
    }
    static double N198143c340(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 2.12) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 2.12) {
            p = GitClassifier.N21580f4941(i);
        }
        return p;
    }
    static double N21580f4941(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 425.0) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() > 425.0) {
            p = GitClassifier.N4dee7d6a42(i);
        }
        return p;
    }
    static double N4dee7d6a42(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 6.8) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 6.8) {
            p = 1;
        }
        return p;
    }
    static double N1e80f32a43(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 6.76) {
            p = GitClassifier.Nd8c346244(i);
        } else if (((Double) i[0]).doubleValue() > 6.76) {
            p = 1;
        }
        return p;
    }
    static double Nd8c346244(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 66.0) {
            p = GitClassifier.N6ccf40dd45(i);
        } else if (((Double) i[2]).doubleValue() > 66.0) {
            p = 2;
        }
        return p;
    }
    static double N6ccf40dd45(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 4.89) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 4.89) {
            p = 1;
        }
        return p;
    }
    static double N5274750346(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 0;
        } else if (i[3].equals("false")) {
            p = 0;
        } else if (i[3].equals("true")) {
            p = GitClassifier.N4b74c5c447(i);
        }
        return p;
    }
    static double N4b74c5c447(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 1;
        } else if (i[4].equals("false")) {
            p = GitClassifier.N27b7c7ec48(i);
        } else if (i[4].equals("true")) {
            p = GitClassifier.N31e8b26f49(i);
        }
        return p;
    }
    static double N27b7c7ec48(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 372.0) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() > 372.0) {
            p = 2;
        }
        return p;
    }
    static double N31e8b26f49(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 3;
        } else if (((Double) i[2]).doubleValue() <= 415.0) {
            p = GitClassifier.N40c5053c50(i);
        } else if (((Double) i[2]).doubleValue() > 415.0) {
            p = GitClassifier.N2d72a20551(i);
        }
        return p;
    }
    static double N40c5053c50(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 8.0) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() > 8.0) {
            p = 3;
        }
        return p;
    }
    static double N2d72a20551(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 4;
        } else if (((Double) i[0]).doubleValue() <= 3.66) {
            p = 4;
        } else if (((Double) i[0]).doubleValue() > 3.66) {
            p = 3;
        }
        return p;
    }
    static double N34e388e052(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 2;
        } else if (i[4].equals("false")) {
            p = GitClassifier.N40ec3fd953(i);
        } else if (i[4].equals("true")) {
            p = GitClassifier.N38beede156(i);
        }
        return p;
    }
    static double N40ec3fd953(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() <= 37815.0) {
            p = GitClassifier.N28e83e1d54(i);
        } else if (((Double) i[2]).doubleValue() > 37815.0) {
            p = 3;
        }
        return p;
    }
    static double N28e83e1d54(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 998.0) {
            p = GitClassifier.N1032c38f55(i);
        } else if (((Double) i[2]).doubleValue() > 998.0) {
            p = 2;
        }
        return p;
    }
    static double N1032c38f55(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 3.39) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 3.39) {
            p = 2;
        }
        return p;
    }
    static double N38beede156(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 2;
        } else if (i[3].equals("false")) {
            p = GitClassifier.N198d0fd857(i);
        } else if (i[3].equals("true")) {
            p = GitClassifier.N76d4491959(i);
        }
        return p;
    }
    static double N198d0fd857(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() <= 12657.0) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() > 12657.0) {
            p = GitClassifier.N707cc22258(i);
        }
        return p;
    }
    static double N707cc22258(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 4;
        } else if (((Double) i[0]).doubleValue() <= 3.02) {
            p = 4;
        } else if (((Double) i[0]).doubleValue() > 3.02) {
            p = 3;
        }
        return p;
    }
    static double N76d4491959(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 3;
        } else if (i[1].equals("false")) {
            p = GitClassifier.N39ca73c760(i);
        } else if (i[1].equals("true")) {
            p = GitClassifier.N6ac0656c65(i);
        }
        return p;
    }
    static double N39ca73c760(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 3;
        } else if (((Double) i[2]).doubleValue() <= 4383.0) {
            p = GitClassifier.N484858e961(i);
        } else if (((Double) i[2]).doubleValue() > 4383.0) {
            p = GitClassifier.Na7bf84e63(i);
        }
        return p;
    }
    static double N484858e961(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 3;
        } else if (((Double) i[0]).doubleValue() <= 4.94) {
            p = 3;
        } else if (((Double) i[0]).doubleValue() > 4.94) {
            p = GitClassifier.N5e33fe1062(i);
        }
        return p;
    }
    static double N5e33fe1062(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() <= 1032.0) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() > 1032.0) {
            p = 3;
        }
        return p;
    }
    static double Na7bf84e63(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 4;
        } else if (((Double) i[0]).doubleValue() <= 5.18) {
            p = 4;
        } else if (((Double) i[0]).doubleValue() > 5.18) {
            p = GitClassifier.N60c5e68d64(i);
        }
        return p;
    }
    static double N60c5e68d64(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 3;
        } else if (((Double) i[2]).doubleValue() <= 12657.0) {
            p = 3;
        } else if (((Double) i[2]).doubleValue() > 12657.0) {
            p = 4;
        }
        return p;
    }
    static double N6ac0656c65(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 4;
        } else if (((Double) i[0]).doubleValue() <= 6.11) {
            p = GitClassifier.N2b42d55a66(i);
        } else if (((Double) i[0]).doubleValue() > 6.11) {
            p = GitClassifier.N5892697969(i);
        }
        return p;
    }
    static double N2b42d55a66(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 4;
        } else if (((Double) i[5]).doubleValue() <= 24.0) {
            p = 4;
        } else if (((Double) i[5]).doubleValue() > 24.0) {
            p = GitClassifier.N7e2ee1f467(i);
        }
        return p;
    }
    static double N7e2ee1f467(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 3;
        } else if (((Double) i[5]).doubleValue() <= 75.0) {
            p = GitClassifier.N3110813268(i);
        } else if (((Double) i[5]).doubleValue() > 75.0) {
            p = 4;
        }
        return p;
    }
    static double N3110813268(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 3;
        } else if (((Double) i[2]).doubleValue() <= 2056.0) {
            p = 3;
        } else if (((Double) i[2]).doubleValue() > 2056.0) {
            p = 4;
        }
        return p;
    }
    static double N5892697969(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 3;
        } else if (((Double) i[5]).doubleValue() <= 41.0) {
            p = 3;
        } else if (((Double) i[5]).doubleValue() > 41.0) {
            p = 4;
        }
        return p;
    }
}
