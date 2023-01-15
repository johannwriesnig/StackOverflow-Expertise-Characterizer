package com.wriesnig.expertise;

import com.hankcs.hanlp.summary.TextRankKeyword;
import com.wriesnig.githubapi.GitApi;
import com.wriesnig.githubapi.Repo;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

public class GitExpertiseJob implements Runnable{
    private User user;

    public GitExpertiseJob(User user){
        this.user = user;
    }
    @Override
    public void run() {
        String userReposPath = "repos/"+user.getGitLogin()+"/";
        File userReposDir = new File(userReposPath);
        userReposDir.mkdirs();

        ArrayList<Repo> repos = GitApi.getReposByLogin(user.getGitLogin());

        BlockingQueue<Repo> downloadedRepos = new LinkedBlockingQueue<>();
        Thread reposDownloadJob = new Thread(()->{
            GitApi.downloadRepos(repos, userReposPath, downloadedRepos);
        });
        reposDownloadJob.start();

       try{
           while(true){
               Repo currentRepo = downloadedRepos.take();
               currentRepo.setFileName(userReposPath + currentRepo.getFileName());
               String currentRepoFileName = userReposPath + downloadedRepos.take();
               if(currentRepoFileName.equals(userReposPath + "")) break;

               computeExpertise(currentRepo);
               deleteDirectory(new File(currentRepo.getFileName()));
           }
       } catch(InterruptedException e){
           throw new RuntimeException();
       }
        boolean isDeleted = deleteDirectory(userReposDir);
    }

    public void computeExpertise(Repo repo){
        if(repo.getMainLanguage().equals(""))return;
        computeTags(repo);
        if(repo.getPresentTags().isEmpty()) return;
        System.out.println("Following repo: " + repo.getName() + " includes following tags: " + repo.getPresentTags());
        //compute metrics
    }

    public void computeTags(Repo repo){
        String mainLanguage = repo.getMainLanguage();
        if(mainLanguage.equals("")) return;
        repo.addTag(mainLanguage);
        repo.addTags(getTagsFromFile(new File(repo.getFileName()+"/README.md")));
        switch(mainLanguage){
            case "python":
                computeTagsForPythonProject(repo);
                break;
            case "java":
                computeTagsForJavaProject(repo);
                break;
        }
    }


    public ArrayList<String> getTagsFromFile(File file){
        if(!file.exists() && !file.isFile()) return new ArrayList<>();
        ArrayList<String> fileKeywords = getTextRankKeywords(file);
        ArrayList<String> tags = new ArrayList<>();
        for(String tag:fileKeywords){
            if(Arrays.asList(Tags.tagsToCharacterize).contains(tag))tags.add(tag);
        }
        return tags;
    }

    public void computeTagsForPythonProject(Repo repo){

    }

    public void computeTagsForJavaProject(Repo repo){
        computeTagsForPom(repo);
        computeTagsForGradle(repo);
    }

    public void computeTagsForPom(Repo repo){
        File pomXML = new File(repo.getFileName() + "/pom.xml");
        repo.addTags(getTagsFromFile(pomXML));
    }

    public void computeTagsForGradle(Repo repo){
        try (Stream<Path> stream = Files.walk(Paths.get(repo.getFileName()))) {
            stream.filter(f -> f.getFileName().toString().equals("build.gradle"))
                    .forEach(f -> repo.addTags(getTagsFromFile(f.toFile())));
        } catch (Exception e){
            System.out.println("Error traversing dir");
        }
    }

    public ArrayList<String> getTextRankKeywords(File file){
        String document = "";
        try {
            document = Files.readString(file.toPath());
        } catch (IOException e) {
            System.out.println("Reading failed");
        }
        return (ArrayList<String>) TextRankKeyword.getKeywordList(document, 1000);
    }

    public boolean deleteDirectory(File file){
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
