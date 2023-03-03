package com.wriesnig.expertise.git.metrics;

import com.wriesnig.api.git.Repo;
import com.wriesnig.utils.Logger;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PythonMetrics extends MetricsSetter {
    private static final String RADON_CYCLOMATIC_COMPLEXITY = "cc";
    private static final String RADON_SOURCE_LINES_OF_CODE = "raw";
    private static final int INDEX_RADON_TOOL = 3;
    private static final int INDEX_RADON_COMMAND = 4;
    private static final int INDEX_FILE_INPUT = 5;
    private static final int INDEX_FILE_OUTPUT = 8;
    private final String[] radonProcess = new String[]{"cmd", "/c", "python", "", "cc", "", "--json", ">", "", "&&", "echo", "done"};


    public PythonMetrics(Repo repo) {
        super(repo);
        String radonAbsolutePath = new File("tools/Python311/radon-master/radon").getAbsolutePath();
        radonProcess[INDEX_RADON_TOOL] = radonAbsolutePath;
    }

    public String getToolReport(String command, File input){
        String report="{}";
        if(output.exists())
            output.delete();
        try{
            callRadonProcess(command, input);
            report = FileUtils.readLines(output, Charsets.UTF_8).stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining());
        } catch (IOException e) {
            Logger.error("Radon report for " + command + " command could not be created for " + input.getPath());
        } catch (InterruptedException e) {
            Logger.error("Radon " + command + " process for " + input.getPath() + " timed out.");
        }
        return report;
    }

    public void callRadonProcess(String command, File input) throws InterruptedException, IOException {
        setProcessProperties(command, input.getAbsolutePath(), output.getAbsolutePath());
        ProcessBuilder builder = new ProcessBuilder(radonProcess);
        Process process = builder.start();
        boolean processPassed = process.waitFor(1, TimeUnit.MINUTES);
        process.children().forEach(ProcessHandle::destroy);
        process.destroy();
        if (!processPassed) {
            throw new InterruptedException();
        }
    }

    @Override
    public void setAvgCyclomaticComplexity() {
        String content= getToolReport(RADON_CYCLOMATIC_COMPLEXITY, root);
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
        setProjectSourceLinesOfCode();
        setTestDirectoriesSourceLinesOfCode();
    }

    public void setProjectSourceLinesOfCode() {
        String reportContent = getSourceLinesOfCodeReport(root);
        int sourceLinesOfCode = parseReportForSourceLinesOfCode(reportContent);
        repo.setSourceLinesOfCode(sourceLinesOfCode);
    }

    public void setTestDirectoriesSourceLinesOfCode() {
        ArrayList<File> testDirectories = getTestDirectories();
        int testFilesSourceLinesOfCode = 0;
        for (File testFile : testDirectories) {
            String testReport = getSourceLinesOfCodeReport(testFile);
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
            sourceLinesOfCode += file.has("sloc") ? file.getInt("sloc") : 0;
        }
        return sourceLinesOfCode;
    }

    public String getSourceLinesOfCodeReport(File root) {
        return getToolReport(RADON_SOURCE_LINES_OF_CODE, root);
    }

    public void setProcessProperties(String command, String inputFile, String outputFile) {
        radonProcess[INDEX_RADON_COMMAND] = command;
        radonProcess[INDEX_FILE_INPUT] = inputFile;
        radonProcess[INDEX_FILE_OUTPUT] = outputFile;
    }

}
