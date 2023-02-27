package com.wriesnig.expertise.git.metrics;

import com.wriesnig.api.git.Repo;
import com.wriesnig.utils.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class PythonMetrics extends MetricsSetter {
    private static final int INDEX_RADON_TOOL = 3;
    private static final int INDEX_RADON_COMMAND = 4;
    private static final int INDEX_FILE_INPUT = 5;
    private static final int INDEX_FILE_OUTPUT = 8;
    private final String[] radonProcess = new String[]{"cmd", "/c", "python", "", "cc", "", "--json", ">", "", "&&", "echo","done"};


    public PythonMetrics(Repo repo) {
        super(repo);
        String radonAbsolutePath = new File("tools/Python311/radon-master/radon").getAbsolutePath();
        radonProcess[INDEX_RADON_TOOL] = radonAbsolutePath;
    }

    @Override
    public void setAvgCyclomaticComplexity() {
        String content = "";
        File output = new File(root.getAbsolutePath() + "\\output.json");
        setProcessProperties("cc", root.getAbsolutePath(), output.getAbsolutePath());
        ProcessBuilder builder = new ProcessBuilder(radonProcess);
        try {
            Process process = builder.start();
            Logger.info("Process started " + root.getAbsolutePath());
            boolean processPassed = process.waitFor(1, TimeUnit.MINUTES);
            if(!processPassed){
                repo.setCyclomaticComplexity(-1);
                process.destroy();
                return;
            }
            content = new String(Files.readAllBytes(output.toPath()));
        } catch (IOException | InterruptedException e) {
            Logger.error("Process to compute python cc failed.", e);
            repo.setCyclomaticComplexity(-1);
            return;
        }

        int counter = 0;
        int cyclomaticComplexitySum = 0;
        JSONObject jsonObject = new JSONObject(content);
        Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            if (!(jsonObject.get(key) instanceof JSONArray)) continue;
            JSONArray fileObjects = jsonObject.getJSONArray(key);
            for (int i = 0; i < fileObjects.length(); i++) {
                JSONObject object = fileObjects.getJSONObject(i);
                if (object.getString("type").equals("class")) {
                    JSONArray classMethods = object.getJSONArray("methods");
                    for (int j = 0; j < classMethods.length(); j++) {
                        JSONObject classMethod = classMethods.getJSONObject(j);
                        int complexity = classMethod.getInt("complexity");
                        if (complexity < 2) continue;
                        cyclomaticComplexitySum += complexity;
                        counter++;
                    }
                } else {
                    int complexity = object.getInt("complexity");
                    if (complexity < 2) continue;
                    cyclomaticComplexitySum += complexity;
                    counter++;
                }
            }
        }

        double returnVal = counter == 0 ? -1 : (double) cyclomaticComplexitySum / counter;
        repo.setCyclomaticComplexity((int) (returnVal * 100) / 100.0);
    }

    @Override
    public void setSourceLinesOfCode() {
        File output = new File(root.getAbsolutePath() + "\\output.json");
        setProjectSourceLinesOfCode(output);
        setTestDirectoriesSourceLinesOfCode(output);
    }

    public void setProjectSourceLinesOfCode(File output) {
        String reportContent = getSourceLinesOfCodeReport(root, output);
        int sourceLinesOfCode = parseReportForSourceLinesOfCode(reportContent);
        repo.setSourceLinesOfCode(sourceLinesOfCode);
    }

    public void setTestDirectoriesSourceLinesOfCode(File output) {
        ArrayList<File> testDirectories = getTestDirectories();
        int testFilesSourceLinesOfCode = 0;
        for (File testFile : testDirectories) {
            String testReport = getSourceLinesOfCodeReport(testFile, output);
            testFilesSourceLinesOfCode += parseReportForSourceLinesOfCode(testReport);
        }

        repo.setTestFilesSourceLinesOfCode(testFilesSourceLinesOfCode);
    }

    public int parseReportForSourceLinesOfCode(String content) {
        int sourceLinesOfCode = 0;
        JSONObject reportContent = new JSONObject(content);
        Iterator<String> keys = reportContent.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            JSONObject file = reportContent.getJSONObject(key);
            sourceLinesOfCode += file.has("sloc")?file.getInt("sloc"):0;
        }
        return sourceLinesOfCode;
    }

    public String getSourceLinesOfCodeReport(File root, File output) {
        String content = "";
        setProcessProperties("raw", root.getAbsolutePath(), output.getAbsolutePath());
        ProcessBuilder builder = new ProcessBuilder(radonProcess);
        try {
            Process process = builder.start();
            boolean processPassed = process.waitFor(1, TimeUnit.MINUTES);
            if(!processPassed){
                process.destroy();
                return "";
            }
            content = new String(Files.readAllBytes(output.toPath()));
        } catch (IOException | InterruptedException e) {
            Logger.error("Process to compute source lines of code for python project " + repo.getFileName() + " failed.", e);
        }
        return content;
    }

    public void setProcessProperties(String command, String inputFile, String outputFile){
        radonProcess[INDEX_RADON_COMMAND] = command;
        radonProcess[INDEX_FILE_INPUT] = inputFile;
        radonProcess[INDEX_FILE_OUTPUT] = outputFile;
    }

}
