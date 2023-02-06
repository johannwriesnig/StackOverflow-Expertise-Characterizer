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
    private double complexity;
    private double quality;
    private int stars;


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

    public double getComplexity() {
        return complexity;
    }

    public void setComplexity(double complexity) {
        this.complexity = complexity;
    }

    public double getQuality() {
        return quality;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }

    public void setStars(int stars){this.stars=stars;}

    public int getStars(){return this.stars;}
}
