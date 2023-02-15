package com.wriesnig.expertise.git;

import com.hankcs.hanlp.summary.TextRankKeyword;
import com.wriesnig.expertise.Tags;
import com.wriesnig.expertise.User;
import com.wriesnig.expertise.git.badges.BuildStatus;
import com.wriesnig.expertise.git.badges.StatusBadgesAnalyser;
import com.wriesnig.expertise.git.metrics.java.JavaCyclomaticComplexity;
import com.wriesnig.expertise.git.metrics.python.PythonCyclomaticComplexity;
import com.wriesnig.api.git.GitApi;
import com.wriesnig.api.git.Repo;
import com.wriesnig.utils.GitClassifierBuilder;
import com.wriesnig.utils.Logger;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

public class GitExpertiseJob implements Runnable {
    private User user;

    public GitExpertiseJob(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        String userReposPath = "repos/" + user.getGitLogin() + "/";
        File userReposDir = new File(userReposPath);
        userReposDir.mkdirs();

        ArrayList<Repo> repos = GitApi.getReposByLogin(user.getGitLogin());
        cleanseRepos(repos); //not sure if needed

        BlockingQueue<Repo> downloadedRepos = new LinkedBlockingQueue<>();
        Thread reposDownloadJob = new Thread(() -> {
            GitApi.downloadRepos(repos, userReposPath, downloadedRepos);
        });
        reposDownloadJob.start();

        try {
            while (true) {
                Repo currentRepo = downloadedRepos.take();
                currentRepo.setFileName(userReposPath + currentRepo.getFileName());
                if (currentRepo.getName().equals("")) break;
                computeExpertise(currentRepo);
                FileUtils.deleteDirectory(new File(currentRepo.getFileName()));
            }
        } catch (InterruptedException e) {
            //
        } catch (IOException e) {
            //
        }
        try {
            FileUtils.deleteDirectory(userReposDir);
        } catch (IOException e) {
            //throw new RuntimeException(e);
        }
        HashMap<String, ArrayList<Double>> scoresPerTag = getExpertisePerTag(repos);
        scoresPerTag.forEach((key, value) -> {
            double score = value.stream().mapToDouble(Double::doubleValue).sum() / value.size();
            user.getExpertise().getGitExpertise().put(key, score);
        });

    }

    public HashMap<String, ArrayList<Double>> getExpertisePerTag(ArrayList<Repo> repos) {
        HashMap<String, ArrayList<Double>> scoresPerTag = new HashMap<>();
        for (String tag : Tags.tagsToCharacterize) {
            scoresPerTag.put(tag, new ArrayList<>());
            for (Repo repo : repos) {
                if (repo.getPresentTags().contains(tag)) scoresPerTag.get(tag).add(repo.getQuality());
            }
        }

        return scoresPerTag;
    }

    public void cleanseRepos(ArrayList<Repo> repos) {
        repos.removeIf(repo -> !Arrays.asList(Tags.tagsToCharacterize).contains(repo.getMainLanguage()));
    }


    public void computeExpertise(Repo repo) {
        if (repo.getMainLanguage().equals("")) return;
        File readMe = getReadMeFile(repo);
        repo.addTag(repo.getMainLanguage());
        repo.addTags(getTagsFromFile(readMe));
        switch (repo.getMainLanguage()) {
            case "java":
                findTagsForJavaProject(repo);
                computeJavaMetrics(repo);
                break;
            case "python":
                findTagsForPythonProject(repo);
                computePythonMetrics(repo);
                break;
        }
        StatusBadgesAnalyser badgesAnalyser = new StatusBadgesAnalyser(readMe);
        badgesAnalyser.initBadges();
        repo.setBuildStatus(badgesAnalyser.getBuildStatus());
        repo.setCoverage(badgesAnalyser.getCoverage());

        Logger.info("Repo: " + repo.getName() + " has following tags " + repo.getPresentTags() + " " +
                "and following stats... Build: " + repo.getBuildStatus() + " Coverage: " + repo.getCoverage() + " Complexity: " + repo.getComplexity() + " ReadMe exists: " + readMe.exists());
        if (repo.getMainLanguage().equals("java"))
            GitClassifierBuilder.writeLine(repo.getComplexity() + "," + readMe.exists() + "," + (repo.getBuildStatus() != BuildStatus.FAILING) + "," + repo.getCoverage() + "," + repo.getStars());
        //classifier to add
        Object[] classificationData = {repo.getComplexity(), readMe.exists() ? "1" : "0", "0", repo.getCoverage(), "1"};
        double quality = 0;
        try {
            quality = GitClassifier.classify(classificationData);
        } catch (Exception e) {
            Logger.error("Error while classifying git repo", e);
        }

        repo.setQuality(quality);
    }

    public File getReadMeFile(Repo repo) {
        String[] possibleFileExtensions = new String[]{".md", ".rst", ".adoc", ".markdown", ".mdown", ".mkdn", ".textile", "rdoc", ".org", ".creole", ".mediawiki", ".wiki",
                ".asciidoc", ".asc", ".pod"};
        File readMe = new File("");
        for (String fileExtension : possibleFileExtensions) {
            readMe = new File(repo.getFileName() + "/readme" + fileExtension);
            if (readMe.exists()) break;
        }
        return readMe;
    }

    public void computeJavaMetrics(Repo repo) {
        if (repo.getPresentTags().isEmpty()) return;
        JavaCyclomaticComplexity javaCyclomaticComplexity = new JavaCyclomaticComplexity(new File(repo.getFileName()));
        double complexity = javaCyclomaticComplexity.getProjectComplexity();
        complexity = ((int) (complexity * 10)) / 10.0;
        repo.setComplexity(complexity);
    }

    public void computePythonMetrics(Repo repo) {
        if (repo.getPresentTags().isEmpty()) return;
        PythonCyclomaticComplexity pythonCyclomaticComplexity = new PythonCyclomaticComplexity(new File(repo.getFileName()));
        double complexity = pythonCyclomaticComplexity.getProjectComplexity();
        repo.setComplexity(complexity);
    }


    public ArrayList<String> getTagsFromFile(File file) {
        if (!file.exists() && !file.isFile()) return new ArrayList<>();
        ArrayList<String> fileKeywords = getTextRankKeywords(file);
        ArrayList<String> tags = new ArrayList<>();
        ArrayList<String> tagsToCharacterize = new ArrayList<>(Arrays.asList(Tags.tagsToCharacterize));
        for (String tag : fileKeywords) {
            if (tagsToCharacterize.contains(tag)) tags.add(tag);
            else if (tag.contains("framework") && tagsToCharacterize.contains(tag.replace("framework", "")))
                tags.add(tag.replace("framework", ""));
        }
        return tags;
    }

    public void findTagsForPythonProject(Repo repo) {
        File importsFile = new File("src/main/resources/src/workspace" + "/" + repo.getName());
        try (Stream<Path> stream = Files.walk(Paths.get(repo.getFileName())); FileWriter fileWriter = new FileWriter(importsFile.getPath())) {
            stream.filter(f -> f.getFileName().toString().matches(".*\\.py"))
                    .forEach(f -> {
                        storePythonImports(fileWriter, f);
                    });
        } catch (IOException e) {
            Logger.error("Issues traversing files", e);
        }
        repo.addTags(getTagsFromFile(importsFile));
        if(!importsFile.delete()) Logger.error("Could not delete file -> " + importsFile.getPath());
    }

    private void storePythonImports(FileWriter fileWriter, Path f){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(f.toFile()));
            String line = bufferedReader.readLine();
            while (line != null && (line.startsWith("import") || line.startsWith("from") || line.startsWith(" ") || line.startsWith("#"))) {
                if (line.startsWith("import") || line.startsWith("from")) {
                    fileWriter.write(line + "\n");
                }
                line = bufferedReader.readLine();
            }
        } catch (IOException e){
            Logger.error("...", e);
        }
    }



    public void findTagsForJavaProject(Repo repo) {
        findTagsForPom(repo);
        findTagsForGradle(repo);
    }

    public void findTagsForPom(Repo repo) {
        File pomXML = new File(repo.getFileName() + "/pom.xml");
        repo.addTags(getTagsFromFile(pomXML));
    }

    public void findTagsForGradle(Repo repo) {
        try (Stream<Path> stream = Files.walk(Paths.get(repo.getFileName()))) {
            stream.filter(f -> f.getFileName().toString().equals("build.gradle"))
                    .forEach(f -> repo.addTags(getTagsFromFile(f.toFile())));
        } catch (Exception e) {
            System.out.println("Error traversing dir");
        }
    }

    public ArrayList<String> getTextRankKeywords(File file) {
        String document = "";
        try {
            document = Files.readString(file.toPath());
        } catch (IOException e) {
            System.out.println("Reading failed");
        }
        return (ArrayList<String>) TextRankKeyword.getKeywordList(document, 1000);
    }

}
