package com.wriesnig.api.git;

import com.wriesnig.expertise.git.badges.BuildStatus;
import java.util.ArrayList;

public class Repo {
    private String fileName;
    private String mainLanguage;
    private String name;
    private ArrayList<String> presentTags;
    private double coverage;
    private BuildStatus buildStatus;
    private double cyclomaticComplexity;
    private double quality;
    private int stars;
    private int sourceLinesOfCode;
    private int testFilesSourceLinesOfCode;


    public Repo(String name, String mainLanguage, int stars) {
        this.name = name;
        this.mainLanguage = mainLanguage;
        this.presentTags = new ArrayList<>();
        this.stars=stars;
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



    public int getStars(){return this.stars;}

    public boolean isHasTests(){
        double ratio = testFilesSourceLinesOfCode / (double)(sourceLinesOfCode - testFilesSourceLinesOfCode);
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
}
