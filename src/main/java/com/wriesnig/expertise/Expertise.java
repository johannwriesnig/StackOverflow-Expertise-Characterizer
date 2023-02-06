package com.wriesnig.expertise;

import java.util.HashMap;

public class Expertise {
    private HashMap<String, Double> gitExpertise;
    private HashMap<String, Double> stackExpertise;
    private final static double STACK_WEIGHT = 2/3.0;
    private final static double GIT_WEIGHT = 1/3.0;

    public Expertise(){
        gitExpertise = new HashMap<>();
        stackExpertise = new HashMap<>();

        for(String tag: Tags.tagsToCharacterize){
            gitExpertise.put(tag, 0.0);
            stackExpertise.put(tag, 0.0);
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
            overAllExpertise.put(tag, avgExpertise);
        }
        return overAllExpertise;
    }

}
