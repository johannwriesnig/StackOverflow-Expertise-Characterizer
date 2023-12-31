package com.wriesnig.expertise;

import java.util.HashMap;

public class Expertise {
    enum ExpertiseDescription {
        BEGINNER("Beginner"),
        INTERMEDIATE("Intermediate"),
        PROFICIENT("Proficient"),
        EXPERT("Expert");

        private final String description;

        ExpertiseDescription(String description) {
            this.description = description;
        }

        String getValue(){
            return description;
        }
    }
    public final static int[] CLASSIFIER_OUTPUT = new int[]{1,2,3,4,5};
    private final static double STACK_WEIGHT = 2/3.0;
    private final static double GIT_WEIGHT = 1/3.0;

    private final HashMap<String, Double> gitExpertise;
    private final HashMap<String, Double> stackExpertise;

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

    public HashMap<String, Double> getCombinedExpertise(){
        HashMap<String, Double> overAllExpertise = new HashMap<>();
        for(String tag: Tags.tagsToCharacterize){
            double stackTagExpertise = stackExpertise.get(tag);
            double gitTagExpertise = gitExpertise.get(tag);
            double avgExpertise = (Expertise.STACK_WEIGHT * stackTagExpertise+gitTagExpertise * Expertise.GIT_WEIGHT);
            avgExpertise = (double)((int)(avgExpertise*100))/100.0;
            overAllExpertise.put(tag, avgExpertise);
        }
        return overAllExpertise;
    }

    public static HashMap<String, String> getExpertiseAsDescriptions(HashMap<String, Double> expertiseAsNumbers){
        HashMap<String, String> expertise = new HashMap<>();

        for(String key :expertiseAsNumbers.keySet()){
            int score = (int)((double)expertiseAsNumbers.get(key));
            ExpertiseDescription expertiseDescription = ExpertiseDescription.BEGINNER;
            switch (score) {
                case 2 -> expertiseDescription = ExpertiseDescription.INTERMEDIATE;
                case 3 -> expertiseDescription = ExpertiseDescription.PROFICIENT;
                case 4, 5 -> expertiseDescription = ExpertiseDescription.EXPERT;
            }
            expertise.put(key, expertiseDescription.getValue());
        }

        return expertise;
    }

}
