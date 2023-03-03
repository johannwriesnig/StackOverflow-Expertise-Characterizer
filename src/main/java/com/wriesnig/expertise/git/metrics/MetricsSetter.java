package com.wriesnig.expertise.git.metrics;

import com.wriesnig.api.git.Repo;
import com.wriesnig.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class MetricsSetter {
    protected Repo repo;
    protected File root;
    protected File output;

    public MetricsSetter(Repo repo) {
        this.repo = repo;
        root = new File(repo.getFileName()).getAbsoluteFile();
        output = new File(repo.getFileName()+"\\output.json").getAbsoluteFile();
    }

    public void setMetrics() {
        setSourceLinesOfCode();
        setAvgCyclomaticComplexity();
    }

    public abstract void setAvgCyclomaticComplexity();

    public abstract void setSourceLinesOfCode();

    public ArrayList<File> getTestDirectories() {
        ArrayList<File> testDirectories = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(Paths.get(repo.getFileName()))) {
            List<File> foundTestDirectories = stream.filter(f -> f.toFile().isDirectory())
                    .filter(f -> f.getFileName().toString().matches("test(s)*"))
                    .filter(this::containsTestDirectory)
                    .map(Path::toFile)
                    .map(File::getAbsoluteFile)
                    .toList();
            testDirectories.addAll(foundTestDirectories);
        } catch (Exception e) {
            Logger.error("Traversing " + repo.getFileName() + " to find test directories failed.", e);
        }

        return testDirectories;
    }

    public boolean containsTestDirectory(Path testDirectory) {
        try (Stream<Path> walk = Files.walk(testDirectory)) {
            return walk.anyMatch(path -> path.getFileName().toString().matches("(.*\\.py)|(.*\\.java)"));
        } catch (IOException e) {
            Logger.error("Traversing " + repo.getFileName() + " test directory failed.", e);
        }
        return false;
    }
}
