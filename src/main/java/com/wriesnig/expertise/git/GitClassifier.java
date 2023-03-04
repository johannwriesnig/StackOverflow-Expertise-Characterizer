package com.wriesnig.expertise.git;

class GitClassifier {

    public static double classify(Object[] i) {

        double p = Double.NaN;
        p = GitClassifier.N70a2bb4345(i);
        return p;
    }
    static double N70a2bb4345(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 2;
        } else if (i[1].equals("false")) {
            p = GitClassifier.N5bf658ed46(i);
        } else if (i[1].equals("true")) {
            p = GitClassifier.N5d4895672(i);
        }
        return p;
    }
    static double N5bf658ed46(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 655.0) {
            p = GitClassifier.N76c7190947(i);
        } else if (((Double) i[2]).doubleValue() > 655.0) {
            p = GitClassifier.N450c65fb60(i);
        }
        return p;
    }
    static double N76c7190947(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 218.0) {
            p = GitClassifier.N1af64d8148(i);
        } else if (((Double) i[2]).doubleValue() > 218.0) {
            p = GitClassifier.N5321321153(i);
        }
        return p;
    }
    static double N1af64d8148(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 71.0) {
            p = GitClassifier.N7feb953f49(i);
        } else if (((Double) i[2]).doubleValue() > 71.0) {
            p = GitClassifier.N7040134b52(i);
        }
        return p;
    }
    static double N7feb953f49(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 0;
        } else if (i[3].equals("false")) {
            p = 0;
        } else if (i[3].equals("true")) {
            p = GitClassifier.N16339b1f50(i);
        }
        return p;
    }
    static double N16339b1f50(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 0;
        } else if (i[4].equals("false")) {
            p = 0;
        } else if (i[4].equals("true")) {
            p = GitClassifier.N5872d78151(i);
        }
        return p;
    }
    static double N5872d78151(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 5.12) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 5.12) {
            p = 0;
        }
        return p;
    }
    static double N7040134b52(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() <= 34.0) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() > 34.0) {
            p = 2;
        }
        return p;
    }
    static double N5321321153(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 1;
        } else if (i[4].equals("false")) {
            p = GitClassifier.N59c0b00954(i);
        } else if (i[4].equals("true")) {
            p = GitClassifier.N3d013ed157(i);
        }
        return p;
    }
    static double N59c0b00954(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 3.48) {
            p = GitClassifier.N1740aa6f55(i);
        } else if (((Double) i[0]).doubleValue() > 3.48) {
            p = GitClassifier.N67ea398756(i);
        }
        return p;
    }
    static double N1740aa6f55(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() <= 359.0) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() > 359.0) {
            p = 1;
        }
        return p;
    }
    static double N67ea398756(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 4.94) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 4.94) {
            p = 1;
        }
        return p;
    }
    static double N3d013ed157(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 1;
        } else if (i[3].equals("false")) {
            p = GitClassifier.N611d818058(i);
        } else if (i[3].equals("true")) {
            p = GitClassifier.N731c4c1e59(i);
        }
        return p;
    }
    static double N611d818058(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 3.25) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 3.25) {
            p = 1;
        }
        return p;
    }
    static double N731c4c1e59(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 7.0) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 7.0) {
            p = 1;
        }
        return p;
    }
    static double N450c65fb60(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 1;
        } else if (i[4].equals("false")) {
            p = GitClassifier.N71cf2c3d61(i);
        } else if (i[4].equals("true")) {
            p = GitClassifier.N7c53b20562(i);
        }
        return p;
    }
    static double N71cf2c3d61(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 3685.0) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() > 3685.0) {
            p = 2;
        }
        return p;
    }
    static double N7c53b20562(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 2;
        } else if (i[3].equals("false")) {
            p = 2;
        } else if (i[3].equals("true")) {
            p = GitClassifier.N6dbe6d2d63(i);
        }
        return p;
    }
    static double N6dbe6d2d63(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() <= 1128.0) {
            p = GitClassifier.N4bbbfedc64(i);
        } else if (((Double) i[2]).doubleValue() > 1128.0) {
            p = GitClassifier.N282f5e8d68(i);
        }
        return p;
    }
    static double N4bbbfedc64(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 2;
        } else if (((Double) i[5]).doubleValue() <= 77.0) {
            p = GitClassifier.N431d9d5265(i);
        } else if (((Double) i[5]).doubleValue() > 77.0) {
            p = 3;
        }
        return p;
    }
    static double N431d9d5265(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() <= 858.0) {
            p = GitClassifier.N22bfccae66(i);
        } else if (((Double) i[2]).doubleValue() > 858.0) {
            p = 2;
        }
        return p;
    }
    static double N22bfccae66(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() <= 840.0) {
            p = GitClassifier.N3cf61ad167(i);
        } else if (((Double) i[2]).doubleValue() > 840.0) {
            p = 3;
        }
        return p;
    }
    static double N3cf61ad167(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 3;
        } else if (((Double) i[0]).doubleValue() <= 2.77) {
            p = 3;
        } else if (((Double) i[0]).doubleValue() > 2.77) {
            p = 2;
        }
        return p;
    }
    static double N282f5e8d68(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 3;
        } else if (((Double) i[0]).doubleValue() <= 5.53) {
            p = GitClassifier.N777a1d769(i);
        } else if (((Double) i[0]).doubleValue() > 5.53) {
            p = GitClassifier.N575d6e0671(i);
        }
        return p;
    }
    static double N777a1d769(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 3;
        } else if (((Double) i[2]).doubleValue() <= 105997.0) {
            p = 3;
        } else if (((Double) i[2]).doubleValue() > 105997.0) {
            p = GitClassifier.N5b71d72b70(i);
        }
        return p;
    }
    static double N5b71d72b70(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 4;
        } else if (((Double) i[0]).doubleValue() <= 4.21) {
            p = 4;
        } else if (((Double) i[0]).doubleValue() > 4.21) {
            p = 3;
        }
        return p;
    }
    static double N575d6e0671(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() <= 25254.0) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() > 25254.0) {
            p = 3;
        }
        return p;
    }
    static double N5d4895672(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() <= 361.0) {
            p = GitClassifier.N3f97e92273(i);
        } else if (((Double) i[2]).doubleValue() > 361.0) {
            p = GitClassifier.N4804957c78(i);
        }
        return p;
    }
    static double N3f97e92273(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 0;
        } else if (((Double) i[2]).doubleValue() <= 70.0) {
            p = GitClassifier.N6a8aa97474(i);
        } else if (((Double) i[2]).doubleValue() > 70.0) {
            p = GitClassifier.N561bc77d75(i);
        }
        return p;
    }
    static double N6a8aa97474(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 2.25) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 2.25) {
            p = 1;
        }
        return p;
    }
    static double N561bc77d75(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 2;
        } else if (i[4].equals("false")) {
            p = GitClassifier.N77246ad176(i);
        } else if (i[4].equals("true")) {
            p = GitClassifier.N777a53ee77(i);
        }
        return p;
    }
    static double N77246ad176(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 5.53) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 5.53) {
            p = 2;
        }
        return p;
    }
    static double N777a53ee77(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() <= 183.0) {
            p = 2;
        } else if (((Double) i[2]).doubleValue() > 183.0) {
            p = 3;
        }
        return p;
    }
    static double N4804957c78(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 2;
        } else if (i[4].equals("false")) {
            p = GitClassifier.N40ac832e79(i);
        } else if (i[4].equals("true")) {
            p = GitClassifier.N314acf4481(i);
        }
        return p;
    }
    static double N40ac832e79(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 2;
        } else if (((Double) i[5]).doubleValue() <= 95.0) {
            p = GitClassifier.N140a1b0880(i);
        } else if (((Double) i[5]).doubleValue() > 95.0) {
            p = 3;
        }
        return p;
    }
    static double N140a1b0880(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 3.88) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 3.88) {
            p = 1;
        }
        return p;
    }
    static double N314acf4481(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 3;
        } else if (((Double) i[2]).doubleValue() <= 1289.0) {
            p = GitClassifier.N5d09136482(i);
        } else if (((Double) i[2]).doubleValue() > 1289.0) {
            p = GitClassifier.N4fdeaa6e86(i);
        }
        return p;
    }
    static double N5d09136482(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 3;
        } else if (((Double) i[5]).doubleValue() <= 78.0) {
            p = GitClassifier.N79984e1883(i);
        } else if (((Double) i[5]).doubleValue() > 78.0) {
            p = 4;
        }
        return p;
    }
    static double N79984e1883(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 3;
        } else if (((Double) i[5]).doubleValue() <= 5.0) {
            p = GitClassifier.N3d8ed87884(i);
        } else if (((Double) i[5]).doubleValue() > 5.0) {
            p = 3;
        }
        return p;
    }
    static double N3d8ed87884(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 4;
        } else if (((Double) i[0]).doubleValue() <= 3.5) {
            p = GitClassifier.N5ad9006285(i);
        } else if (((Double) i[0]).doubleValue() > 3.5) {
            p = 3;
        }
        return p;
    }
    static double N5ad9006285(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 4;
        } else if (((Double) i[2]).doubleValue() <= 717.0) {
            p = 4;
        } else if (((Double) i[2]).doubleValue() > 717.0) {
            p = 3;
        }
        return p;
    }
    static double N4fdeaa6e86(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 4;
        } else if (((Double) i[0]).doubleValue() <= 5.66) {
            p = 4;
        } else if (((Double) i[0]).doubleValue() > 5.66) {
            p = GitClassifier.N740dd50387(i);
        }
        return p;
    }
    static double N740dd50387(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 3;
        } else if (((Double) i[2]).doubleValue() <= 12657.0) {
            p = GitClassifier.N3f0b9ee288(i);
        } else if (((Double) i[2]).doubleValue() > 12657.0) {
            p = 4;
        }
        return p;
    }
    static double N3f0b9ee288(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 3;
        } else if (((Double) i[5]).doubleValue() <= 47.0) {
            p = 3;
        } else if (((Double) i[5]).doubleValue() > 47.0) {
            p = GitClassifier.N2d43437189(i);
        }
        return p;
    }
    static double N2d43437189(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 3;
        } else if (((Double) i[2]).doubleValue() <= 1719.0) {
            p = 3;
        } else if (((Double) i[2]).doubleValue() > 1719.0) {
            p = 4;
        }
        return p;
    }
}
