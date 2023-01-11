package com.wriesnig.githubapi;

import com.wriesnig.utils.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Class is the Interface to the Github-RestApi. It offers different GET-Methods to retrieve specific data.
 */
public class GitApi {
    private final String apiUrl = "https://api.github.com/";
    private final WebClient client;
    private static String token;


    public GitApi() {
        client = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
    }

    public static void setToken(String token) {
        GitApi.token = token;
    }

    /**
     * Get GithubUser by his login(unique).
     *
     * @param login
     * @return
     */
    public GitUser getUserByLogin(String login) {
        if (login.contains(" ")) return null;
        WebClient.ResponseSpec response = client.get().uri("/users/" + login).retrieve();
        JSONObject userAsJson = new JSONObject(Objects.requireNonNull(response.bodyToMono(String.class).block()));

        return GitApiDataParser.parseUserByLoginResponse(userAsJson);
    }

    /**
     * Get GithubUsers based on full_name which is not unique.
     *
     * @param fullName
     * @return
     */
    public ArrayList<String> getUsersByFullName(String fullName) {
        fullName = fullName.replace(" ", "+");
        WebClient.ResponseSpec response = client.get().uri("/search/users?q=fullname:" + fullName).retrieve();
        JSONObject usersAsJson = new JSONObject(Objects.requireNonNull(response.bodyToMono(String.class).block()));

        return GitApiDataParser.parseUsersByFullName(usersAsJson);
    }

    public ArrayList<String> getReposByLogin(String login) {
        WebClient.ResponseSpec response = client.get().uri("users/" + login + "/repos").retrieve();
        JSONArray repos = new JSONArray(Objects.requireNonNull(response.bodyToMono(String.class).block()));

        return GitApiDataParser.parseReposByLogin(repos);
    }

    //refactorn
    public void downloadRepos(String login, ArrayList<String> repos, String path, BlockingQueue<String> downloadedRepos) {
        try {
            for (String repo : repos) {
                URL url = new URL(apiUrl + "repos/" + login + "/" + repo + "/zipball");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream in = connection.getInputStream();
                ZipInputStream zipIn = new ZipInputStream(in);

                ZipEntry entry = zipIn.getNextEntry();
                String root = entry==null?"":entry.getName();

                while (entry != null) {
                    String filePath = path + entry.getName();
                    if (!entry.isDirectory()) {

                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
                        byte[] bytesIn = new byte[4096];
                        int read = 0;
                        while ((read = zipIn.read(bytesIn)) != -1) {
                            bos.write(bytesIn, 0, read);
                        }
                        bos.close();
                    } else {
                        File dir = new File(filePath);
                        dir.mkdirs();
                    }
                    zipIn.closeEntry();
                    entry = zipIn.getNextEntry();
                }
                if(!root.equals(""))downloadedRepos.put(root);
            }
            downloadedRepos.put("finished");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
