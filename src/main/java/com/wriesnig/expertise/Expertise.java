package com.wriesnig.expertise;

import java.util.HashMap;

public class Expertise {
    private final static double STACK_WEIGHT = 2/3.0;
    private final static double GIT_WEIGHT = 1/3.0;

    private HashMap<String, Double> gitExpertise;
    private HashMap<String, Double> stackExpertise;

    public Expertise(){
        gitExpertise = new HashMap<>();
        stackExpertise = new HashMap<>();

        for(String tag: Tags.tagsToCharacterize){
            gitExpertise.put(tag, 1.0);
            stackExpertise.put(tag, 1.0);
        }
    }

    public HashMap<String, Double> getGitExpertise() {
        return gitExpertise;
    }

    public HashMap<String, Double> getStackExpertise() {
        return stackExpertise;
    }

    public HashMap<String, Double> getOverAllExpertise(){
        HashMap<String, Double> overAllExpertise = new HashMap<>();
        for(String tag: Tags.tagsToCharacterize){
            double stackTagExpertise = stackExpertise.get(tag);
            double gitTagExpertise = gitExpertise.get(tag);
            double avgExpertise = (Expertise.STACK_WEIGHT * stackTagExpertise+gitTagExpertise * Expertise.GIT_WEIGHT);
            int cutOffExpertise = (int)(avgExpertise * 100);
            avgExpertise = cutOffExpertise/100.0;
            overAllExpertise.put(tag, avgExpertise);
        }
        return overAllExpertise;
    }

}
