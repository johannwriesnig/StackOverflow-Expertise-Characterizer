package com.wriesnig.api.git;

import com.wriesnig.utils.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class GitApi {
    private final static String apiUrl = "https://api.github.com/";
    private static String token;


    private GitApi() {
    }

    public static void setToken(String token) {
        GitApi.token = token;
    }

    public static GitUser getUserByLogin(String login) {
        if (login.contains(" ")) return null;

        String path = "users/" + login;
        InputStream apiStream = getStreamFromAPICall(path);
        JSONObject userAsJson = new JSONObject(getStringFromStream(apiStream));

        return GitApiResponseParser.parseUserByLoginResponse(userAsJson);
    }

    public static ArrayList<String> getUsersByFullName(String fullName) {
        fullName = fullName.replace(" ", "+");
        String path = "search/users?q=fullname:" + fullName;
        InputStream apiStream = getStreamFromAPICall(path);
        JSONObject usersAsJson = new JSONObject(getStringFromStream(apiStream));

        return GitApiResponseParser.parseUsersByFullName(usersAsJson);
    }

    public static ArrayList<Repo> getReposByLogin(String login) {
        String path = "users/" + login + "/repos?type=all";
        InputStream apiStream = getStreamFromAPICall(path);
        JSONArray repos = new JSONArray(getStringFromStream(apiStream));

        return GitApiResponseParser.parseReposByLogin(repos);
    }

    public static InputStream getStreamFromAPICall(String path) {
        try {
            URL url = new URL(apiUrl + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            return connection.getInputStream();
        } catch (MalformedURLException e) {
            Logger.error("URL issues while using git api", e);
        } catch (IOException e) {
            Logger.error("Issues while requesting git api", e);
        }
        return InputStream.nullInputStream();
    }

    public static String getStringFromStream(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        int currentChar;
        try {
            while ((currentChar = bufferedReader.read()) != -1) {
                stringBuilder.append((char) currentChar);
            }
        } catch (IOException e) {
            Logger.error("Issues while creating string from buffered stream", e);
        }
        return stringBuilder.toString();
    }

    public static void downloadRepos(ArrayList<Repo> repos, String path, BlockingQueue<Repo> downloadedRepos) {
        try {
            tryDownloadRepos(repos, path, downloadedRepos);
        } catch (IOException e) {
            Logger.error("Issues while working with repo download stream", e);
        } catch (InterruptedException e) {
            Logger.error("Issues when queuing downloaded repos", e);
        }
    }

    public static void tryDownloadRepos(ArrayList<Repo> repos, String path, BlockingQueue<Repo> downloadedRepos) throws IOException, InterruptedException {
        for (Repo repo : repos) {
            ZipInputStream zipIn = getZipStreamFromRepo(repo.getName());
            ZipEntry entry = zipIn.getNextEntry();
            String root = entry == null ? "" : entry.getName();

            while (entry != null) {
                processZipEntry(path, entry, zipIn);
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }

            if (!root.equals("")) {
                repo.setFileName(root);
                downloadedRepos.put(repo);
            }
        }
        downloadedRepos.put(new Repo("", ""));
    }

    private static void processZipEntry(String path, ZipEntry entry, ZipInputStream zipIn) throws IOException {
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
    }

    private static ZipInputStream getZipStreamFromRepo(String repo) {
        String apiPath = "repos/" + repo + "/zipball";
        InputStream in = getStreamFromAPICall(apiPath);
        return new ZipInputStream(in);
    }


}
