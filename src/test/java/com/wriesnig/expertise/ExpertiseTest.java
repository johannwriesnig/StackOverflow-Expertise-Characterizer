package com.wriesnig.expertise;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExpertiseTest {
    private Expertise expertise;

    @BeforeEach
    public void setUp(){
        expertise = new Expertise();
    }

    @Test
    public void getOverAllExpertise(){
        assertNotNull(expertise.getOverAllExpertise());
    }

    @AfterEach
    public void tearDown(){
        expertise = null;
    }
}
