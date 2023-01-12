package com.wriesnig.expertise;

import com.wriesnig.githubapi.GitApi;


import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

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

        ArrayList<String> repos = GitApi.getReposByLogin(user.getGitLogin());

        BlockingQueue<String> downloadedRepos = new LinkedBlockingQueue<>();
        Thread reposDownloadJob = new Thread(()->{
            GitApi.downloadRepos(user.getGitLogin(),repos, userReposPath, downloadedRepos);
        });
        reposDownloadJob.start();

       try{
           while(true){
               String currentRepoFileName = userReposPath + downloadedRepos.take();
               System.out.println("Repo taken from Queue: " + currentRepoFileName);
               if(currentRepoFileName.equals(userReposPath + "finished")) break;

               File currentRepo = new File(currentRepoFileName);
               //ComputationOfExpertise...
               deleteDirectory(currentRepo);
           }
       } catch(InterruptedException e){
           throw new RuntimeException();
       }
        boolean isDeleted = deleteDirectory(userReposDir);
    }

    boolean deleteDirectory(File file){
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
