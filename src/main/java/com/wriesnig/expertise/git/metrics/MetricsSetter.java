package com.wriesnig.expertise.git.metrics;
import com.wriesnig.api.git.Repo;
import com.wriesnig.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;


public abstract class MetricsSetter {
    protected Repo repo;
    protected File root;

    public MetricsSetter(Repo repo){
        this.repo = repo;
        root = new File(repo.getFileName());
    }

    public void setMetrics(){
        setSloc();
        setCC();
    }

    public abstract void setCC();

    public abstract void setSloc();

    public File getTestRoot(){
        Path testDir = Path.of("");
        try (Stream<Path> stream = Files.walk(Paths.get(repo.getFileName()).toAbsolutePath())) {
            Optional<Path> path = stream.filter(f -> f.toFile().isDirectory())
                    .filter(f->f.getFileName().toString().matches("test(s)*"))
                    .filter(this::containsTestFile)
                    .findFirst();
            if(path.isPresent())testDir = path.get();
        } catch (Exception e) {
            Logger.error("Traversing project to find test files failed.", e);
        }

        return testDir.toFile();
    }

    public boolean containsTestFile(Path testDir){
        try (Stream<Path> walk = Files.walk(testDir)) {
            return walk.anyMatch(path -> path.getFileName().toString().matches("(.*\\.py)|(.*\\.java)"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
