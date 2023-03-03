package com.wriesnig.expertise.git;

import com.hankcs.hanlp.summary.TextRankKeyword;
import com.wriesnig.api.git.FinishRepo;
import com.wriesnig.api.git.GitApi;
import com.wriesnig.api.git.Repo;
import com.wriesnig.expertise.Expertise;
import com.wriesnig.expertise.Tags;
import com.wriesnig.expertise.User;
import com.wriesnig.expertise.git.badges.BuildStatus;
import com.wriesnig.expertise.git.badges.StatusBadgesAnalyser;
import com.wriesnig.expertise.git.metrics.JavaMetrics;
import com.wriesnig.expertise.git.metrics.PythonMetrics;
import com.wriesnig.utils.GitClassifierBuilder;
import com.wriesnig.utils.Logger;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class GitExpertiseJob implements Runnable {
    private static final String reposWorkspace = "src/main/resources/src/workspace/repos";

    private final User user;

    public GitExpertiseJob(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        String userReposPath = reposWorkspace + "/" + user.getGitLogin() + "/";

        ArrayList<Repo> repos = GitApi.getReposByLogin(user.getGitLogin());
        cleanseRepos(repos);
        //repos = reposForClassifier();
        BlockingQueue<Repo> downloadedRepos = new LinkedBlockingQueue<>();
        downloadReposInNewThread(repos, userReposPath, downloadedRepos);
        determineReposExpertise(downloadedRepos, userReposPath);
        deleteUsersReposWorkSpace(new File(userReposPath));
        repos.removeIf(repo-> repo.getCyclomaticComplexity()==-1 || repo.getSourceLinesOfCode()==0);
        HashMap<String, ArrayList<Double>> expertisePerTag = getExpertisePerTag(repos);
        storeExpertise(expertisePerTag, user);
    }

    public ArrayList<Repo> reposForClassifier(){
        ArrayList<Repo> repos = new ArrayList<>();
        /*repos.add(new Repo("ariahendrawan713/python", "python", 0));
        repos.add(new Repo("mujacm/Python", "python", 0));
        repos.add(new Repo("csse120-201920/01-IntroductionToPython", "python", 0));
        repos.add(new Repo("AnTi-anti/damai_ticket", "python", 0));
        repos.add(new Repo("donnemartin/system-design-primer", "python", 0));
        repos.add(new Repo("openai/openai-python", "python", 0));
        repos.add(new Repo("521xueweihan/HelloGitHub", "python", 0));
        repos.add(new Repo("tiangolo/fastapi", "python", 0));
        repos.add(new Repo("ridgerchu/SpikeGPT", "python", 0));
        repos.add(new Repo("mukulpatnaik/researchgpt", "python", 0));
        repos.add(new Repo("pre-commit/pre-commit", "python", 0));
        repos.add(new Repo("TychoBrouwer/ac-controller-api-socket-python", "python", 0));
        repos.add(new Repo("PauloPSAS/Exercicios-de-Python", "python", 0));
        repos.add(new Repo("NoriahM/Python-Codes", "python", 0));
        repos.add(new Repo("rodrigoazs/python-pokemon-firered-battle-simulator", "python", 0));
        repos.add(new Repo("davidfantasy/mybatis-plus-generator-ui", "java", 0));
        repos.add(new Repo("seata/seata", "java", 0));
        repos.add(new Repo("dbeaver/dbeaver", "java", 0));
        repos.add(new Repo("prestodb/presto", "java", 0));
        repos.add(new Repo("apache/doris", "java", 0));
        repos.add(new Repo("Snailclimb/JavaGuide", "java", 0));
        repos.add(new Repo("micrometer-metrics/micrometer", "java", 0));
        repos.add(new Repo("prometheus/jmx_exporter", "java", 0));
        repos.add(new Repo("SPLWare/esProc", "java", 0));
        repos.add(new Repo("ZhongFuCheng3y/austin", "java", 0));
        repos.add(new Repo("xuxueli/xxl-job", "java", 0));*/
        repos.add(new Repo("Azure/azure-sdk-for-java", "java", 0));


        return repos;
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
        repos.removeIf(repo -> repo.isForked() || !Arrays.asList(Tags.tagsToCharacterize).contains(repo.getMainLanguage()));
    }

    public void downloadReposInNewThread(ArrayList<Repo> repos, String userReposPath, BlockingQueue<Repo> downloadedRepos){
        Thread reposDownloadJob = new Thread(() -> {
            GitApi.downloadRepos(repos, userReposPath, downloadedRepos);
        });
        reposDownloadJob.start();
    }

    public void determineReposExpertise(BlockingQueue<Repo> downloadedRepos, String userReposPath){
        try {
            tryDetermineReposExpertise(downloadedRepos, userReposPath);
        } catch (InterruptedException e) {
            Logger.error("Repo download queue was interrupted.", e);
        } catch (IOException e) {
            Logger.error("Failed to delete repo directory.", e);
        }
    }

    public void tryDetermineReposExpertise(BlockingQueue<Repo> downloadedRepos, String userReposPath) throws IOException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        Repo currentRepo;
        while (!((currentRepo = downloadedRepos.take()) instanceof FinishRepo)) {
            currentRepo.setFileName(userReposPath + currentRepo.getFileName());
            executorService.execute(new RepoExpertiseJob(currentRepo));
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(24, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            Logger.error("Expertise thread was interrupted.", e);
        }
    }


    public void determineExpertise(Repo repo) {
        if (repo.getMainLanguage().equals("")) return;
        File readMe = getReadMeFile(repo);
        repo.addTag(repo.getMainLanguage());
        repo.addTags(getTagsFromFile(readMe));

        switch (repo.getMainLanguage()) {
            case "java" -> {
                findTagsForJavaProject(repo);
                setJavaMetrics(repo);
            }
            case "python" -> {
                findTagsInImportLines(repo);
                setPythonMetrics(repo);
            }
        }
        StatusBadgesAnalyser badgesAnalyser = new StatusBadgesAnalyser(readMe);
        badgesAnalyser.initBadges();
        repo.setBuildStatus(badgesAnalyser.getBuildStatus());
        repo.setCoverage(badgesAnalyser.getCoverage());
        String readMeContent="";
        try {
            if(readMe.exists())
                readMeContent = new String(Files.readAllBytes(readMe.toPath()));
        } catch (IOException e) {
            Logger.error("Readme content for -> "+ repo.getFileName() + " couldnt be read.",e);
        }
        boolean isReadMeExists = readMeContent.length()>repo.getName().length()+20;

        if(repo.getCyclomaticComplexity()!=-1)
            GitClassifierBuilder.writeLine(repo.getCyclomaticComplexity() + "," + repo.isHasTests() + "," + repo.getSourceLinesOfCode() + "," + isReadMeExists + "," + (repo.getBuildStatus() != BuildStatus.FAILING) + "," + repo.getCoverage() + ",");

        Object[] classificationData = {repo.getCyclomaticComplexity(), String.valueOf(repo.isHasTests()), repo.getSourceLinesOfCode(), String.valueOf(isReadMeExists), String.valueOf(repo.getBuildStatus() != BuildStatus.FAILING), repo.getCoverage()};

        double quality = Expertise.classifierOutput[(int) GitClassifier.classify(classificationData)];
        Logger.info(repo.getFileName() + " contains " + repo.getPresentTags() +
                " with following stats: Expertise: " + quality + ";complexity-> " + repo.getCyclomaticComplexity() + "; hasReadMe-> "+readMe.exists() +"; BuildStatus-> " + repo.getBuildStatus() + "; hasTests-> " + repo.isHasTests() + "; Coverage: " + repo.getCoverage() +"; Sloc: " + repo.getSourceLinesOfCode());


        repo.setQuality(quality);
    }

    public void deleteUsersReposWorkSpace(File workSpace){
        try {
            FileUtils.deleteDirectory(workSpace);
        } catch (IOException e) {
            Logger.error("Deleting repo workspace failed -> " + workSpace.getPath(), e);
        }
    }

    public void storeExpertise(HashMap<String, ArrayList<Double>> scoresPerTag, User user){
        scoresPerTag.forEach((key, value) -> {
            double score = value.size()!=0?value.stream().mapToDouble(Double::doubleValue).sum()/value.size():1.0;
            score = (double)((int)(score*100))/100.0;
            user.getExpertise().getGitExpertise().put(key, score);
        });
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

    public void setJavaMetrics(Repo repo) {
        JavaMetrics javaMetrics = new JavaMetrics(repo);
        javaMetrics.setMetrics();
    }

    public void setPythonMetrics(Repo repo) {
        PythonMetrics pythonMetrics = new PythonMetrics(repo);
        pythonMetrics.setMetrics();
    }


    public static ArrayList<String> getTagsFromFile(File file) {
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

    public void findTagsInImportLines(Repo repo) {
        HashSet<String> tags = new HashSet<>();
        try (Stream<Path> stream = Files.walk(Paths.get(repo.getFileName()))){
            stream.filter(f -> f.getFileName().toString().matches(".*\\.py|.*\\.java"))
                    .forEach(f -> {
                        tags.addAll(getTagsFromFilesImportLines(f));
                    });

        } catch (IOException e) {
            Logger.error("Traversing project files to find tags in imports failed.", e);
        }
        repo.addTags(new ArrayList<>(tags));
    }

    private ArrayList<String> getTagsFromFilesImportLines(Path f){
        ArrayList<String> tags = new ArrayList<>();
        HashSet<String> importElements = new HashSet<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(f.toFile()))) {
            String line = bufferedReader.readLine();
            while (line != null && isInImportSection(line)) {
                if (line.startsWith("import") || line.startsWith("from")) {
                    String[] lineElements = line.split(" ");
                    importElements.addAll(Arrays.asList(lineElements));
                }
                line = bufferedReader.readLine();
            }
            List<String> tagsList = importElements.stream()
                    .filter(l -> {
                        for (String tag : Tags.tagsToCharacterize) {
                            if (l.contains(tag)) return true;
                        }
                        return false;
                    })
                    .map(l -> {
                        for (String tag : Tags.tagsToCharacterize) {
                            if (l.contains(tag)) return tag;
                        }
                        return "";
                    })
                    .toList();
            tags.addAll(tagsList);
            return tags;
        } catch (IOException e) {
            Logger.error("Failed to read imports.", e);
        }

        return tags;
    }

    private static synchronized void addTagsFromImportsFile(Repo repo, String imports){
        String importsFilePath = "src/main/resources/src/workspace/imports.txt";
        File importsFile = new File("src/main/resources/src/workspace/imports.txt");

        try {
            Files.writeString(Path.of(importsFilePath),imports);
        } catch (IOException e) {
            Logger.error("Writing to imports.txt failed.", e);
        }

        repo.addTags(getTagsFromFile(importsFile));
    }


    public boolean isInImportSection(String line){
        return line.startsWith("import") || line.startsWith("from") || line.startsWith(" ") || line.startsWith("package")
                || line.startsWith("#") || line.startsWith("//") || line.startsWith("/*") || line.startsWith("*/");
    }



    public void findTagsForJavaProject(Repo repo) {
        File pomXML = new File(repo.getFileName() + "/pom.xml");
        File gradleBuild = new File(repo.getFileName()+ "/build.gradle");

        if(pomXML.exists())repo.addTags(getTagsFromFile(pomXML));
        else if(gradleBuild.exists())findTagsForGradle(repo);
        else findTagsInImportLines(repo);
    }


    public void findTagsForGradle(Repo repo) {
        try (Stream<Path> stream = Files.walk(Paths.get(repo.getFileName()))) {
            stream.filter(f -> f.getFileName().toString().equals("build.gradle"))
                    .forEach(f -> repo.addTags(getTagsFromFile(f.toFile())));
        } catch (Exception e) {
            Logger.error("Traversing gradle project failed.", e);
        }
    }

    public static ArrayList<String> getTextRankKeywords(File file) {
        String document = "";
        try {
            document = Files.readString(file.toPath());
        } catch (IOException e) {
            Logger.error("Reading file failed.", e);
        }
        return (ArrayList<String>) TextRankKeyword.getKeywordList(document, 1000);
    }

    class RepoExpertiseJob extends Thread{
        private Repo repo;

        public RepoExpertiseJob(Repo repo){
            this.repo=repo;
        }

        @Override
        public void run() {
            determineExpertise(repo);

            try {
                FileUtils.deleteDirectory(new File(repo.getFileName()));
            } catch (IOException e) {
                Logger.error("Could not delete repo directory -> "+ repo.getFileName(), e);
            }
        }
    }
}

