package com.wriesnig.expertise.git.metrics.java;

import com.wriesnig.expertise.git.metrics.CyclomaticComplexity;
import com.wriesnig.utils.Logger;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public class JavaCyclomaticComplexity extends CyclomaticComplexity {
    public JavaCyclomaticComplexity(File root) {
        super(root);
    }

    @Override
    public double getProjectComplexity() {
        PMDConfiguration configuration = new PMDConfiguration();
        configuration.setInputPaths(root.getPath());
        configuration.addRuleSet("rules.xml");
        configuration.setReportFormat("json");
        configuration.setReportProperties(new Properties());
        File report = new File(root.getPath() + "/output.json");
        configuration.setReportFile(report.getPath());
        PMD.runPmd(configuration);

        String content="";
        try {
            content = new String(Files.readAllBytes(report.toPath()));
        } catch (IOException e) {
            Logger.error("Reading json output failed.", e);
            return -1;
        }

        int counter=0;
        int cyclomaticComplexitySum=0;
        JSONObject jsonObject = new JSONObject(content);
        JSONArray files = jsonObject.getJSONArray("files");
        for(int i=0; i<files.length(); i++){
            JSONObject file = files.getJSONObject(i);
            JSONArray violations = file.getJSONArray("violations");
            for(int j=0; j<violations.length(); j++){
                JSONObject violation = violations.getJSONObject(j);
                String description = violation.getString("description");
                String[] numbers = description.split("[^0-9]+");
                cyclomaticComplexitySum += Integer.parseInt(numbers[numbers.length-1]);
                counter++;
            }
        }

        return counter==0?-1:(double)cyclomaticComplexitySum/counter;
    }

}
