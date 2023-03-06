package com.wriesnig.expertise.git;

class GitClassifier {

    public static double classify(Object[] i){

        double p = Double.NaN;
        p = GitClassifier.N6e61a2ff35(i);
        return p;
    }
    static double N6e61a2ff35(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() <= 522.0) {
            p = GitClassifier.N24dd121d36(i);
        } else if (((Double) i[2]).doubleValue() > 522.0) {
            p = GitClassifier.Nc5249c052(i);
        }
        return p;
    }
    static double N24dd121d36(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 2;
        } else if (i[1].equals("false")) {
            p = GitClassifier.N5b48957837(i);
        } else if (i[1].equals("true")) {
            p = GitClassifier.N97ca3a146(i);
        }
        return p;
    }
    static double N5b48957837(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 1;
        } else if (i[4].equals("false")) {
            p = 1;
        } else if (i[4].equals("true")) {
            p = GitClassifier.N6497bfab38(i);
        }
        return p;
    }
    static double N6497bfab38(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 1;
        } else if (i[3].equals("false")) {
            p = GitClassifier.N7828f9c39(i);
        } else if (i[3].equals("true")) {
            p = GitClassifier.N1ea1805f43(i);
        }
        return p;
    }
    static double N7828f9c39(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 261.0) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() > 261.0) {
            p = GitClassifier.N4b03288b40(i);
        }
        return p;
    }
    static double N4b03288b40(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 2.12) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 2.12) {
            p = GitClassifier.N54ff97c641(i);
        }
        return p;
    }
    static double N54ff97c641(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 425.0) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() > 425.0) {
            p = GitClassifier.Ndcc101542(i);
        }
        return p;
    }
    static double Ndcc101542(Object []i) {
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
    static double N1ea1805f43(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 5.88) {
            p = GitClassifier.N4cb4a47044(i);
        } else if (((Double) i[0]).doubleValue() > 5.88) {
            p = 1;
        }
        return p;
    }
    static double N4cb4a47044(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() <= 66.0) {
            p = GitClassifier.N645d5b8745(i);
        } else if (((Double) i[2]).doubleValue() > 66.0) {
            p = 2;
        }
        return p;
    }
    static double N645d5b8745(Object []i) {
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
    static double N97ca3a146(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 0;
        } else if (i[3].equals("false")) {
            p = 0;
        } else if (i[3].equals("true")) {
            p = GitClassifier.N1b4d79a947(i);
        }
        return p;
    }
    static double N1b4d79a947(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 1;
        } else if (i[4].equals("false")) {
            p = GitClassifier.N72b3d99448(i);
        } else if (i[4].equals("true")) {
            p = GitClassifier.N7cbc252649(i);
        }
        return p;
    }
    static double N72b3d99448(Object []i) {
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
    static double N7cbc252649(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 3;
        } else if (((Double) i[2]).doubleValue() <= 415.0) {
            p = GitClassifier.N1031e0bb50(i);
        } else if (((Double) i[2]).doubleValue() > 415.0) {
            p = GitClassifier.N6041432551(i);
        }
        return p;
    }
    static double N1031e0bb50(Object []i) {
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
    static double N6041432551(Object []i) {
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
    static double Nc5249c052(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 2;
        } else if (i[4].equals("false")) {
            p = GitClassifier.N115ac87953(i);
        } else if (i[4].equals("true")) {
            p = GitClassifier.N7ac1bd5056(i);
        }
        return p;
    }
    static double N115ac87953(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() <= 37815.0) {
            p = GitClassifier.Ne71d6d154(i);
        } else if (((Double) i[2]).doubleValue() > 37815.0) {
            p = 3;
        }
        return p;
    }
    static double Ne71d6d154(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 1009.0) {
            p = GitClassifier.N6443f45155(i);
        } else if (((Double) i[2]).doubleValue() > 1009.0) {
            p = 2;
        }
        return p;
    }
    static double N6443f45155(Object []i) {
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
    static double N7ac1bd5056(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 2;
        } else if (i[3].equals("false")) {
            p = GitClassifier.N3d9fab7157(i);
        } else if (i[3].equals("true")) {
            p = GitClassifier.N4642afcb59(i);
        }
        return p;
    }
    static double N3d9fab7157(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() <= 12657.0) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() > 12657.0) {
            p = GitClassifier.N3d58354d58(i);
        }
        return p;
    }
    static double N3d58354d58(Object []i) {
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
    static double N4642afcb59(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 3;
        } else if (i[1].equals("false")) {
            p = GitClassifier.N311ea16e60(i);
        } else if (i[1].equals("true")) {
            p = GitClassifier.N3993d53065(i);
        }
        return p;
    }
    static double N311ea16e60(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 3;
        } else if (((Double) i[2]).doubleValue() <= 4383.0) {
            p = GitClassifier.N711a501f61(i);
        } else if (((Double) i[2]).doubleValue() > 4383.0) {
            p = GitClassifier.N5efb2bb263(i);
        }
        return p;
    }
    static double N711a501f61(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 3;
        } else if (((Double) i[0]).doubleValue() <= 4.94) {
            p = 3;
        } else if (((Double) i[0]).doubleValue() > 4.94) {
            p = GitClassifier.Nd14f12c62(i);
        }
        return p;
    }
    static double Nd14f12c62(Object []i) {
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
    static double N5efb2bb263(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 4;
        } else if (((Double) i[0]).doubleValue() <= 5.18) {
            p = 4;
        } else if (((Double) i[0]).doubleValue() > 5.18) {
            p = GitClassifier.N1aaae4d264(i);
        }
        return p;
    }
    static double N1aaae4d264(Object []i) {
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
    static double N3993d53065(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 4;
        } else if (((Double) i[0]).doubleValue() <= 6.11) {
            p = GitClassifier.N1324cebf66(i);
        } else if (((Double) i[0]).doubleValue() > 6.11) {
            p = GitClassifier.N7ed95fa969(i);
        }
        return p;
    }
    static double N1324cebf66(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 4;
        } else if (((Double) i[5]).doubleValue() <= 24.0) {
            p = 4;
        } else if (((Double) i[5]).doubleValue() > 24.0) {
            p = GitClassifier.N1405c1d267(i);
        }
        return p;
    }
    static double N1405c1d267(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 3;
        } else if (((Double) i[5]).doubleValue() <= 75.0) {
            p = GitClassifier.N59c43bf468(i);
        } else if (((Double) i[5]).doubleValue() > 75.0) {
            p = 4;
        }
        return p;
    }
    static double N59c43bf468(Object []i) {
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
    static double N7ed95fa969(Object []i) {
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
