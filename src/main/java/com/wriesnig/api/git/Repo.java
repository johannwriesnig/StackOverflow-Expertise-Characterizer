package com.wriesnig.api.git;

import com.wriesnig.expertise.git.badges.BuildStatus;
import java.util.ArrayList;

public class Repo {
    private final String mainLanguage;
    private final String name;
    private final boolean isForked;
    private final int sizeInKB;
    private final ArrayList<String> presentTags;
    private BuildStatus buildStatus;
    private String fileName;
    private double coverage;
    private double cyclomaticComplexity;
    private double quality;
    private int sourceLinesOfCode;
    private int testFilesSourceLinesOfCode;


    public Repo(String name, String mainLanguage, boolean isForked, int sizeInKB) {
        this.name = name;
        this.mainLanguage = mainLanguage;
        this.isForked = isForked;
        this.sizeInKB = sizeInKB;
        this.presentTags = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getMainLanguage() {
        return this.mainLanguage;
    }

    public void addTags(ArrayList<String> tags) {
        for (String tag : tags)
            if (!presentTags.contains(tag)) presentTags.add(tag);
    }

    public ArrayList<String> getPresentTags(){
        return this.presentTags;
    }

    public void addTag(String tag){
        if(!presentTags.contains(tag)) presentTags.add(tag);
    }

    public double getCoverage() {
        return coverage;
    }

    public void setCoverage(double coverage) {
        this.coverage = coverage;
    }

    public BuildStatus getBuildStatus() {
        return buildStatus;
    }

    public void setBuildStatus(BuildStatus buildStatus) {
        this.buildStatus = buildStatus;
    }

    public double getCyclomaticComplexity() {
        return cyclomaticComplexity;
    }

    public void setCyclomaticComplexity(double cyclomaticComplexity) {
        this.cyclomaticComplexity = cyclomaticComplexity;
    }

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }


    public boolean isHasTests(){
        double ratio = testFilesSourceLinesOfCode / ((double)(sourceLinesOfCode - testFilesSourceLinesOfCode));
        return ratio>=2/3.0;
    }

    public int getSourceLinesOfCode() {
        return sourceLinesOfCode;
    }

    public void setSourceLinesOfCode(int sourceLinesOfCode) {
        this.sourceLinesOfCode = sourceLinesOfCode;
    }

    public void setTestFilesSourceLinesOfCode(int testFilesSourceLinesOfCode) {
        this.testFilesSourceLinesOfCode = testFilesSourceLinesOfCode;
    }

    public boolean isForked() {
        return isForked;
    }

    public int getTestFilesSourceLinesOfCode() {
        return testFilesSourceLinesOfCode;
    }

    public int getSizeInKB(){return sizeInKB;}
}
