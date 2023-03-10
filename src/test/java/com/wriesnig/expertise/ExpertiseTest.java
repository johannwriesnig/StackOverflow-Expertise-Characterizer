package com.wriesnig.expertise;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpertiseTest {

    @Test
    public void getOverAllExpertise(){
        Tags.tagsToCharacterize = new String[]{"java", "python"};
        Expertise expertise = new Expertise();
        expertise.getStackExpertise().put("java", 4.5);
        expertise.getGitExpertise().put("java", 3.2);
        expertise.getStackExpertise().put("python", 3.1);
        expertise.getGitExpertise().put("python", 3.5);

        HashMap<String, Double> overallExpertise = expertise.getCombinedExpertise();
        assertEquals(2, overallExpertise.size());
        assertEquals(4.06, overallExpertise.get("java"));
        assertEquals(3.23, overallExpertise.get("python"));
    }

}
