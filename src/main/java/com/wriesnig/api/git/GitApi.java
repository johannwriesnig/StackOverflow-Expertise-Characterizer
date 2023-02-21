package com.wriesnig.api.git;

import com.wriesnig.utils.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class GitApi {
    private final static String apiUrl = "https://api.github.com/";
    private final static GitApiResponseParser responseParser = new GitApiResponseParser();
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

        return responseParser.parseUserByLoginResponse(userAsJson);
    }

    public static ArrayList<String> getUsersByFullName(String fullName) {
        fullName = fullName.replace(" ", "+");
        String path = "search/users?q=fullname:" + fullName;
        InputStream apiStream = getStreamFromAPICall(path);
        JSONObject usersAsJson = new JSONObject(getStringFromStream(apiStream));

        return responseParser.parseUsersByFullName(usersAsJson);
    }

    public static ArrayList<Repo> getReposByLogin(String login) {
        String path = "users/" + login + "/repos?type=all";
        InputStream apiStream = getStreamFromAPICall(path);
        JSONArray repos = new JSONArray(getStringFromStream(apiStream));

        return responseParser.parseReposByLogin(repos);
    }

    public static InputStream getStreamFromAPICall(String path) {
        String url = apiUrl + path;
        try {
            URL getUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            return getInputStream(connection);
        } catch (MalformedURLException e) {
            Logger.error("Url for requesting git-api is malformed -> " + url, e);
        } catch (IOException e) {
            Logger.error("Processing git-api input stream failed.", e);
        }
        return InputStream.nullInputStream();
    }

    //Since cannot mock UrlConnection
    public static InputStream getInputStream(HttpURLConnection connection) throws IOException {
        return connection.getInputStream();
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
            Logger.error("Reading from bufferedReader that contains git-api request failed.", e);
        }
        return stringBuilder.toString();
    }

    public static void downloadRepos(ArrayList<Repo> repos, String path, BlockingQueue<Repo> downloadedRepos) {
        try {
            tryDownloadRepos(repos, path, downloadedRepos);
        } catch (IOException e) {
            Logger.error("Error while processing repo download stream.", e);
        } catch (InterruptedException e) {
            Logger.error("Error in repo download queue.", e);
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
        downloadedRepos.put(new FinishRepo());
    }

    public static void processZipEntry(String path, ZipEntry entry, ZipInputStream zipIn) throws IOException {
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

    public static ZipInputStream getZipStreamFromRepo(String repo) {
        String apiPath = "repos/" + repo + "/zipball";
        InputStream in = getStreamFromAPICall(apiPath);
        return new ZipInputStream(in);
    }

    public static String getToken(){return token;}

}
