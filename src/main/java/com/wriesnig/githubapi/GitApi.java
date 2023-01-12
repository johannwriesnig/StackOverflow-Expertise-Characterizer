package com.wriesnig.githubapi;

import com.wriesnig.utils.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Class is the Interface to the Github-RestApi. It offers different GET-Methods to retrieve specific data.
 */
public class GitApi {
    private final static String apiUrl = "https://api.github.com/";
    private static String token;


    private GitApi() {
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
    public static GitUser getUserByLogin(String login) {
        if (login.contains(" ")) return null;

        String path = "users/" + login;
        InputStream apiStream = getStreamFromAPICall(path);
        JSONObject userAsJson = new JSONObject(getStringFromStream(apiStream));

        return GitApiDataParser.parseUserByLoginResponse(userAsJson);
    }

    /**
     * Get GithubUsers based on full_name which is not unique.
     *
     * @param fullName
     * @return
     */
    public static ArrayList<String> getUsersByFullName(String fullName) {
        fullName = fullName.replace(" ", "+");
        String path = "search/users?q=fullname:" + fullName;
        InputStream apiStream = getStreamFromAPICall(path);
        JSONObject usersAsJson = new JSONObject(getStringFromStream(apiStream));

        return GitApiDataParser.parseUsersByFullName(usersAsJson);
    }

    public static ArrayList<String> getReposByLogin(String login) {
        String path = "users/" + login + "/repos";
        InputStream apiStream = getStreamFromAPICall(path);
        JSONArray repos = new JSONArray(getStringFromStream(apiStream));

        return GitApiDataParser.parseReposByLogin(repos);
    }

    public static InputStream getStreamFromAPICall(String path){
        try {
            URL url = new URL(apiUrl + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            return connection.getInputStream();
        } catch (IOException e){
            e.printStackTrace();
        }
        return InputStream.nullInputStream();
    }

    public static String getStringFromStream(InputStream inputStream){
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        int currentChar;
        try {
            while ((currentChar = bufferedReader.read()) != -1) {
                stringBuilder.append((char) currentChar);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
    //refactorn
    public static void downloadRepos(String login, ArrayList<String> repos, String path, BlockingQueue<String> downloadedRepos) {
        try {
            for (String repo : repos) {
                String apiPath = "repos/" + login + "/" + repo + "/zipball";
                InputStream in = getStreamFromAPICall(apiPath);
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
