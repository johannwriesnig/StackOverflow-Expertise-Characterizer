package com.wriesnig.expertise.git.badges;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatusBadgesAnalyser {
    private final String link = "https://([\\w_-]+(?:(?:\\.[\\w_-]+)+))([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])";
    private ArrayList<Document> badgesHtml;


    public StatusBadgesAnalyser(File readMe){
        String readMeContent = "";
        try {
            readMeContent = Files.readString(readMe.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        badgesHtml = new ArrayList<>();
        initBadges(readMeContent);

    }
    private void initBadges(String content){
        Pattern badge = Pattern.compile("\\[!\\[[^\\]]+]\\(("+link+")\\)\\]\\(" + link + "\\)");
        Matcher badges = badge.matcher(content);
        while(badges.find()){
            String html="";

            try (InputStream in = new URL(badges.group(1)).openStream()) {
                html = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e){
                e.printStackTrace();
            }

            badgesHtml.add(Jsoup.parse(html));
        }
    }

    public BuildStatus getBuildStatus(){
        BuildStatus buildStatus=BuildStatus.NOT_GIVEN;
        for(Document doc: badgesHtml){
            if((buildStatus = getBuildStatusFromDoc(doc)) != BuildStatus.NOT_GIVEN)break;
        }

        return buildStatus;
    }

    private BuildStatus getBuildStatusFromDoc(Document doc){
        BuildStatus buildStatus = BuildStatus.NOT_GIVEN;
        boolean isContainsBuildStatus=false;
        for(Element text: doc.select("svg g text")) {
            String textContent = text.text().toLowerCase();
            if (textContent.matches("build")) {
                isContainsBuildStatus=true;
            }
            if(isContainsBuildStatus){
                if(textContent.matches("passing|passed")) buildStatus = BuildStatus.PASSING;
                if(textContent.matches("failing|failed")) buildStatus = BuildStatus.FAILING;
            }
        }
        return buildStatus;
    }

    public double getCoverage(){
        double coverage=-1.0;
        for(Document doc: badgesHtml){
            if((coverage = getCoverageFromDoc(doc)) != -1.0)break;
        }

        return coverage;
    }

    private double getCoverageFromDoc(Document doc){
        double coverage = -1.0;
        boolean isContainsTestCoverage=false;
        for(Element text: doc.select("svg g text")) {
            String textContent = text.text().toLowerCase();
            if (textContent.matches("coverage|codecov")) {
                isContainsTestCoverage=true;
            }
            if(isContainsTestCoverage && textContent.matches("\\d+(?:\\.\\d+)?%")){
                coverage = Double.parseDouble(textContent.replace("%",""));
                break;
            }
        }
        return coverage;
    }

}
