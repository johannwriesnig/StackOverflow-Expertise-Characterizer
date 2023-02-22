package com.wriesnig.expertise.git.metrics.python;

import com.wriesnig.expertise.git.metrics.CyclomaticComplexity;
import com.wriesnig.utils.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Iterator;

public class PythonCyclomaticComplexity extends CyclomaticComplexity {
    private static final String pythonAbsolute;
    static{
        File pythonRelative = new File("tools/Python311");
        pythonAbsolute = pythonRelative.getAbsolutePath();
    }
    public PythonCyclomaticComplexity(File root) {
        super(root);
    }

    @Override
    public double getProjectComplexity() {
        String content="";
        File output = new File(root.getAbsolutePath()+"\\output.json");
        ProcessBuilder builder = new ProcessBuilder("cmd", "/c", pythonAbsolute+"\\python", pythonAbsolute+"\\radon-master\\radon", "cc", root.getAbsolutePath(),"--json", "--average",">", output.getPath(), "&&", "echo done");
        try {
            Process process = builder.start();
            process.waitFor();
            content = new String(Files.readAllBytes(output.toPath()));
        } catch (IOException | InterruptedException e) {
            Logger.error("Process to compute python cc failed.", e);
            return -1;
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
                        cyclomaticComplexitySum+=classMethod.getInt("complexity");
                        counter++;
                    }
                }else{
                    cyclomaticComplexitySum+=object.getInt("complexity");
                    counter++;
                }
            }
        }

        double returnVal = counter==0?-1:(double)cyclomaticComplexitySum/counter;
        return (int)(returnVal*100)/100.0;
    }
}
