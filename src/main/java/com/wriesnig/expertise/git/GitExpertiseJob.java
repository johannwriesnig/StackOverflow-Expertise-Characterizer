package com.wriesnig.expertise.git;

import com.hankcs.hanlp.summary.TextRankKeyword;
import com.wriesnig.expertise.Tags;
import com.wriesnig.expertise.User;
import com.wriesnig.expertise.git.badges.StatusBadgesAnalyser;
import com.wriesnig.expertise.git.metrics.java.JavaCyclomaticComplexity;
import com.wriesnig.expertise.git.metrics.python.PythonCyclomaticComplexity;
import com.wriesnig.githubapi.GitApi;
import com.wriesnig.githubapi.Repo;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
                currentRepo.setFileName(userReposPath + "/" + currentRepo.getFileName());
                if (currentRepo.getName().equals("")) break;
                computeExpertise(currentRepo);
                deleteDirectory(new File(currentRepo.getFileName()));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        boolean isDeleted = deleteDirectory(userReposDir);
    }

    public void cleanseRepos(ArrayList<Repo> repos) {
        Iterator<Repo> iterator = repos.iterator();
        while (iterator.hasNext()) {
            Repo repo = iterator.next();
            if (!Arrays.asList(Tags.tagsToCharacterize).contains(repo.getMainLanguage())) iterator.remove();
        }
    }

    public void computeExpertise(Repo repo) {
        if (repo.getMainLanguage().equals("")) return;
        File readMe = new File(repo.getFileName() + "/README.md");
        repo.addTag(repo.getMainLanguage());
        repo.addTags(getTagsFromFile(readMe));
        switch(repo.getMainLanguage()){
            case "java":
                computeTagsForJavaProject(repo);
                computeJavaMetrics(repo);
                break;
            case "python":
                computeTagsForPythonProject(repo);
                computePythonMetrics(repo);
                break;
        }
        StatusBadgesAnalyser badgesAnalyser = new StatusBadgesAnalyser(readMe);
        repo.setBuildStatus(badgesAnalyser.getBuildStatus());
        repo.setCoverage(badgesAnalyser.getCoverage());

        //classifier to add
        repo.setQuality(3);
    }

    public void computeJavaMetrics(Repo repo){
        if (repo.getPresentTags().isEmpty()) return;
        JavaCyclomaticComplexity javaCyclomaticComplexity = new JavaCyclomaticComplexity(new File(repo.getFileName()));
        double complexity = javaCyclomaticComplexity.getProjectComplexity();
    }

    public void computePythonMetrics(Repo repo){
        if (repo.getPresentTags().isEmpty()) return;
        PythonCyclomaticComplexity pythonCyclomaticComplexity = new PythonCyclomaticComplexity(new File(repo.getFileName()));
        double complexity = pythonCyclomaticComplexity.getProjectComplexity();
    }



    public ArrayList<String> getTagsFromFile(File file) {
        if (!file.exists() && !file.isFile()) return new ArrayList<>();
        ArrayList<String> fileKeywords = getTextRankKeywords(file);
        ArrayList<String> tags = new ArrayList<>();
        for (String tag : fileKeywords) {
            if (Arrays.asList(Tags.tagsToCharacterize).contains(tag)) tags.add(tag);
        }
        return tags;
    }

    //refactoren
    public void computeTagsForPythonProject(Repo repo) {
        try {
            FileWriter fileWriter = new FileWriter("repos/" + user.getGitLogin()+"/keywords.info");
            try (Stream<Path> stream = Files.walk(Paths.get(repo.getFileName()))) {
                stream.filter(f -> f.getFileName().toString().matches(".*\\.py"))
                        .forEach(f -> {
                            try {
                                BufferedReader bufferedReader = new BufferedReader(new FileReader(f.toFile()));
                                String line = bufferedReader.readLine();
                                while(line != null && (line.startsWith("import") || line.startsWith("from") || line.startsWith(" ") || line.startsWith("#"))){
                                    if(line.startsWith("import") || line.startsWith("from")){
                                        fileWriter.write(line);
                                        System.out.println(line);
                                    }
                                    line = bufferedReader.readLine();
                                }
                            } catch (FileNotFoundException e) {
                                System.out.println("issues");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            } catch (IOException e) {
                System.out.println("traversing issues");
            }
        } catch (Exception e) {
            System.out.println("Error fiel writing dir");
        }

        repo.addTags(getTagsFromFile(new File("repos/" + user.getGitLogin()+"/keywords.info")));
    }

    public void computeTagsForJavaProject(Repo repo) {
        computeTagsForPom(repo);
        computeTagsForGradle(repo);
    }

    public void computeTagsForPom(Repo repo) {
        File pomXML = new File(repo.getFileName() + "/pom.xml");
        repo.addTags(getTagsFromFile(pomXML));
    }

    public void computeTagsForGradle(Repo repo) {
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

    public boolean deleteDirectory(File file) {
        if (file.isDirectory()) {
            File[] entries = file.listFiles();
            if (entries != null) {
                for (File entry : entries) {
                    deleteDirectory(entry);
                }
            }
        }
        return file.delete();
    }
}
