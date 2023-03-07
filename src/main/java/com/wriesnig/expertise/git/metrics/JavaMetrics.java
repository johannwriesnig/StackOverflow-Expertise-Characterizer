package com.wriesnig.expertise.git.metrics;

import com.wriesnig.api.git.Repo;
import com.wriesnig.utils.Logger;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class JavaMetrics extends MetricsSetter {
    public JavaMetrics(Repo repo) {
        super(repo);
    }

    @Override
    public void setMetrics() {
        File report = new File(root.getPath() + "/output.json");
        JSONObject reportContent;

        try {
            runPMDTool(report);
            reportContent = getReportAsJson(report);
        } catch (InterruptedException e) {
            Logger.error("PMD tool for " + repo.getFileName() + " timed out.");
            repo.setCyclomaticComplexity(-1);
            repo.setSourceLinesOfCode(0);
            repo.setTestFilesSourceLinesOfCode(0);
            return;
        }


        ArrayList<String> filesInTestDirectory = getNamesOfFilesLocatedInTestDirectories();

        int cyclomaticComplexityCounter = 0;
        int cyclomaticComplexitySum = 0;
        int sourceLinesOfCode = 0;
        int testFilesSourceLinesOfCode = 0;

        JSONArray files = reportContent.getJSONArray("files");
        for (int i = 0; i < files.length(); i++) {
            JSONObject file = files.getJSONObject(i);
            JSONArray violations = file.getJSONArray("violations");
            String fileName = file.getString("filename");
            boolean inTestDir = filesInTestDirectory.contains(fileName);
            for (int j = 0; j < violations.length(); j++) {
                JSONObject violation = violations.getJSONObject(j);
                String description = violation.getString("description");
                String rule = violation.getString("rule");
                String[] numbers = description.split("[^0-9]+");
                int parsedVal = Integer.parseInt(numbers[numbers.length - 1]);
                switch (rule) {
                    case "NcssCount":
                        if (!description.startsWith("The class"))
                            sourceLinesOfCode += parsedVal;
                        if (inTestDir)
                            testFilesSourceLinesOfCode += parsedVal;
                        break;
                    case "CyclomaticComplexity":
                        if(parsedVal<2)continue;
                        cyclomaticComplexitySum += parsedVal;
                        cyclomaticComplexityCounter++;
                        break;
                }

            }
        }
        double avgCyclomaticComplexity = cyclomaticComplexityCounter == 0 ? 1 : (double) cyclomaticComplexitySum / cyclomaticComplexityCounter;
        repo.setCyclomaticComplexity((int) (avgCyclomaticComplexity * 100) / 100.0);
        repo.setSourceLinesOfCode(sourceLinesOfCode);
        repo.setTestFilesSourceLinesOfCode(testFilesSourceLinesOfCode);
    }

    public void runPMDTool(File report) throws InterruptedException {
        PMDConfiguration configuration = new PMDConfiguration();
        configuration.setInputPaths(root.getPath());
        configuration.addRuleSet("src/main/resources/src/pmdTool/rules.xml");
        configuration.setReportFormat("json");
        configuration.setReportProperties(new Properties());
        configuration.setReportFile(report.getPath());

        ExecutorService executor = Executors.newCachedThreadPool();
        Callable<Object> task = () -> PMD.runPmd(configuration);
        Future<Object> future = executor.submit(task);
        try {
            Object result = future.get(1, TimeUnit.MINUTES);
        } catch (Exception e){
            future.cancel(true);
            throw new InterruptedException();
        }
        future.cancel(true);
    }

    public JSONObject getReportAsJson(File report) throws JSONException {
        String reportContent = "";
        try {
            reportContent = new String(Files.readAllBytes(report.toPath()));
        } catch (IOException e) {
            Logger.error("Reading metrics report for " + repo.getFileName() + " failed.", e);
        }

        if(reportContent.isEmpty())reportContent="{}";
        return new JSONObject(reportContent);
    }

    public ArrayList<String> getNamesOfFilesLocatedInTestDirectories() {
        ArrayList<File> testDirectories = getTestDirectories();
        ArrayList<String> fileNames = new ArrayList<>();
        for (File testDirectory : testDirectories) {
            try (Stream<Path> stream = Files.walk(testDirectory.toPath())) {
                List<String> foundFileNames = stream
                        .map(Path::toAbsolutePath)
                        .map(String::valueOf)
                        .toList();
                fileNames.addAll(foundFileNames);
            } catch (IOException e) {
                Logger.error("Traversing test directories of " + repo.getFileName() + " to get file names failed.", e);
            }
        }


        return fileNames;
    }

    @Override
    public void setAvgCyclomaticComplexity() {
    }

    @Override
    public void setSourceLinesOfCode() {
    }
}
