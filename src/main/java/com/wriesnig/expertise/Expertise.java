package com.wriesnig.expertise;

import java.util.HashMap;

public class Expertise {
    private HashMap<String, Double> gitExpertise;
    private HashMap<String, Double> stackExpertise;

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
            double avgExpertise = (stackTagExpertise+gitTagExpertise)/2;
            overAllExpertise.put(tag, avgExpertise);
        }
        return overAllExpertise;
    }

}
