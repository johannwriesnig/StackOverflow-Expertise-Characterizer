package com.wriesnig.expertise.git.metrics;

import com.wriesnig.api.git.Repo;
import com.wriesnig.utils.Logger;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaMetrics extends MetricsSetter {
    public JavaMetrics(Repo repo) {
        super(repo);
    }
    @Override
    public void setMetrics(){
        PMDConfiguration configuration = new PMDConfiguration();
        configuration.setInputPaths(root.getPath());
        configuration.addRuleSet("src/main/resources/src/pmdTool/rules.xml");
        configuration.setReportFormat("json");
        configuration.setReportProperties(new Properties());
        File report = new File(root.getPath() + "/output.json");
        configuration.setReportFile(report.getPath());
        PMD.runPmd(configuration);

        String content="";
        try {
            content = new String(Files.readAllBytes(report.toPath()));
        } catch (IOException e) {
            Logger.error("Reading report for java project failed.", e);
            repo.setComplexity(-1);
            return;
        }

        ArrayList<String> filesInTestDir = getFilesInTestDir();

        int cyclomaticComplexityCounter=0;
        int cyclomaticComplexitySum=0;
        int sLoc=0;
        int testSloc=0;
        JSONObject jsonObject = new JSONObject(content);
        JSONArray files = jsonObject.getJSONArray("files");
        for(int i=0; i<files.length(); i++){
            JSONObject file = files.getJSONObject(i);
            JSONArray violations = file.getJSONArray("violations");
            String fileName = file.getString("filename");
            boolean inTestDir = filesInTestDir.contains(fileName);
            for(int j=0; j<violations.length(); j++){
                JSONObject violation = violations.getJSONObject(j);
                String description = violation.getString("description");
                String rule = violation.getString("rule");
                String[] numbers = description.split("[^0-9]+");
                int parsedVal = Integer.parseInt(numbers[numbers.length-1]);
                switch (rule){
                    case "NcssCount":
                        if(description.startsWith("The class"))
                            sLoc+= parsedVal;
                        if(inTestDir)
                            testSloc+=parsedVal;
                        break;
                    case "CyclomaticComplexity":
                        cyclomaticComplexitySum += parsedVal;
                        cyclomaticComplexityCounter++;
                        break;
                }

            }
        }
        double returnVal = cyclomaticComplexityCounter==0?-1:(double)cyclomaticComplexitySum/cyclomaticComplexityCounter;
        repo.setComplexity((int)(returnVal*100)/100.0);
        repo.setsLoc(sLoc);
        repo.setTestsSloc(testSloc);
    }

    private ArrayList<String> getFilesInTestDir(){
        File testRoot = getTestRoot();
        ArrayList<String> files = new ArrayList<>();
        try (Stream<Path> stream = Files.walk(testRoot.toPath())) {
            List<String> collect = stream
                    .map(Path::toAbsolutePath)
                    .map(String::valueOf)
                    .toList();


            files.addAll(collect);
        } catch (IOException e){
            Logger.error("Traversing test dir failed.",e);
        }

        return files;
    }

    @Override
    public void setCC() {
    }

    @Override
    public void setSloc() {
    }
}
