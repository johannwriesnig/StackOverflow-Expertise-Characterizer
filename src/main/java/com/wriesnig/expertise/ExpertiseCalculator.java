package com.wriesnig.expertise;

import com.wriesnig.expertise.git.GitExpertiseJob;
import com.wriesnig.expertise.stack.StackExpertiseJob;
import com.wriesnig.db.stack.StackDatabase;
import com.wriesnig.utils.Logger;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExpertiseCalculator {
    private ExpertiseCalculator() {
    }

    public static void computeExpertise(ArrayList<User> users) {
        //computeSOExpertise(users);
        computeGHExpertise(users);
    }

    private static void computeSOExpertise(ArrayList<User> users) {
        startThreadedComputation(users, StackExpertiseJob.class, StackDatabase.getConnectionSize());
    }

    private static void computeGHExpertise(ArrayList<User> users) {
        File reposDir = new File("repos");
        reposDir.mkdirs();
        startThreadedComputation(users, GitExpertiseJob.class, 5);
        reposDir.delete();
    }

    private static void startThreadedComputation(ArrayList<User> users, Class<?> clazz, int threadPoolSize) {
        if (!(clazz == StackExpertiseJob.class || clazz == GitExpertiseJob.class)) return;
        Logger.info("Starting expertise computation");
        double start = System.nanoTime();
        ExecutorService executorService = Executors.newFixedThreadPool(threadPoolSize);
        for (User user : users) {
            if (clazz == StackExpertiseJob.class) executorService.execute(new StackExpertiseJob(user));
            else executorService.execute(new GitExpertiseJob(user));
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        double stop = System.nanoTime() - start;
        stop /= 1000000000.0;
        Logger.info("Computation took " + stop + " secs");
    }

}
