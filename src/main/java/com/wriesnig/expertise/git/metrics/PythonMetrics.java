package com.wriesnig.expertise.git.metrics;

import com.wriesnig.api.git.Repo;
import com.wriesnig.utils.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;

public class PythonMetrics extends MetricsSetter{
    private static final String pythonAbsolute;
    static{
        File pythonRelative = new File("tools/Python311");
        pythonAbsolute = pythonRelative.getAbsolutePath();
    }

    public PythonMetrics(Repo repo) {
        super(repo);
    }

    @Override
    public void setCC() {
        String content="";
        File output = new File(root.getAbsolutePath()+"\\output.json");
        ProcessBuilder builder = new ProcessBuilder("cmd", "/c", pythonAbsolute+"\\python", pythonAbsolute+"\\radon-master\\radon", "cc", root.getAbsolutePath(),"--json" ,">", output.getPath(), "&&", "echo done");
        try {
            Process process = builder.start();
            process.waitFor();
            content = new String(Files.readAllBytes(output.toPath()));
        } catch (IOException | InterruptedException e) {
            Logger.error("Process to compute python cc failed.", e);
            repo.setComplexity(-1);
            return;
        }

        int counter=0;
        int cyclomaticComplexitySum=0;
        JSONObject jsonObject = new JSONObject(content);
        Iterator<String> keys = jsonObject.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            if(!(jsonObject.get(key) instanceof JSONArray))continue;
            JSONArray fileObjects= jsonObject.getJSONArray(key);
            for(int i=0; i< fileObjects.length();i++){
                JSONObject object = fileObjects.getJSONObject(i);
                if (object.getString("type").equals("class")){
                    JSONArray classMethods = object.getJSONArray("methods");
                    for(int j=0; j<classMethods.length(); j++){
                        JSONObject classMethod = classMethods.getJSONObject(j);
                        int complexity = classMethod.getInt("complexity");
                        if (complexity<2) continue;
                        cyclomaticComplexitySum+=complexity;
                        counter++;
                    }
                }else{
                    int complexity = object.getInt("complexity");
                    if (complexity<2) continue;
                    cyclomaticComplexitySum+=complexity;
                    counter++;
                }
            }
        }

        double returnVal = counter==0?-1:(double)cyclomaticComplexitySum/counter;
        repo.setComplexity((int)(returnVal*100)/100.0);
    }

    @Override
    public void setSloc() {
        File output = new File(root.getAbsolutePath()+"\\output.json");
        setProjectSloc(output);
        setTestsSloc(output);
    }

    public void setProjectSloc(File output){
        String contentProject = getSlocReport(root, output);
        int sLoc= parseReportForSloc(contentProject);
        repo.setsLoc(sLoc);
    }

    public void setTestsSloc(File output){
        ArrayList<File> testRoots = getTestRoot();
        int testsSloc=0;
        for(File file: testRoots){
            String contentTests = getSlocReport(file, output);
           testsSloc += parseReportForSloc(contentTests);
        }

        repo.setTestsSloc(testsSloc);
    }

    public int parseReportForSloc(String content){
        int sLoc=0;
        JSONObject jsonObject = new JSONObject(content);
        Iterator<String> keys = jsonObject.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            JSONObject file = jsonObject.getJSONObject(key);
            sLoc+=file.getInt("sloc");
        }
        return sLoc;
    }

    public String getSlocReport(File root, File output){
        String content="";
        ProcessBuilder builder = new ProcessBuilder("cmd", "/c", pythonAbsolute+"\\python", pythonAbsolute+"\\radon-master\\radon", "raw", root.getAbsolutePath(),"--json",">", output.getPath(), "&&", "echo done");
        try {
            Process process = builder.start();
            process.waitFor();
            process.destroy();
            content = new String(Files.readAllBytes(output.toPath()));
        } catch (IOException | InterruptedException e) {
            Logger.error("Process to compute python sloc failed.", e);
        }
        return content;
    }


}
