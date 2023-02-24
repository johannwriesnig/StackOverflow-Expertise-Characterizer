package com.wriesnig.expertise.git.badges;

import com.wriesnig.utils.Logger;
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
    private File readMe;


    public StatusBadgesAnalyser(File readMe){
        this.readMe = readMe;
    }

    public void initBadges(){
        String readMeContent="";
        try {
            if(readMe.exists())readMeContent = Files.readString(readMe.toPath());
        } catch (IOException e) {
            Logger.error("", e);
        }
        badgesHtml = new ArrayList<>();
        Pattern badge = Pattern.compile("\\[!\\[[^\\]]+]\\(("+link+")\\)\\]\\(" + link + "\\)");
        Matcher badges = badge.matcher(readMeContent);
        while(badges.find()){
            String html="";
            try (InputStream in = getInputStreamFromBadge(badges.group(1))) {
                html = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            } catch (IOException e){
                Logger.error("Reading status badge from url failed -> " + badges.group(1), e);
            }

            badgesHtml.add(Jsoup.parse(html));
        }
    }

    public InputStream getInputStreamFromBadge(String link) throws IOException {
        return new URL(link).openStream();
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
            if (textContent.contains("build")) {
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
            if (textContent.contains("coverage") || textContent.contains("codecove")) {
                isContainsTestCoverage=true;
            }
            if(isContainsTestCoverage && textContent.matches("\\d+(?:\\.\\d+)?%")){;
                coverage = Double.parseDouble(textContent.replace("%",""));
                break;
            }
        }
        return coverage;
    }

}
