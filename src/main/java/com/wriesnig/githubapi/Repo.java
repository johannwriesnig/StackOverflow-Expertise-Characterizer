package com.wriesnig.githubapi;

import java.util.ArrayList;

public class Repo {
    private String fileName;
    private String mainLanguage;
    private String name;
    private ArrayList<String> presentTags;

    public Repo(String name, String mainLanguage) {
        this.name = name;
        this.mainLanguage = mainLanguage;
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

}
