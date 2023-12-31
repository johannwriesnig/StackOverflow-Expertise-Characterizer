package com.wriesnig.api.git;

import com.wriesnig.utils.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class GitApi {
    public final static String API_URL = "https://api.github.com/";
    public static final int CODE_RESOURCE_NOT_FOUND = 404;
    public static final int CODE_BAD_CREDENTIALS=401;
    private static String token;
    private static int reposMaxSizeInKB;
    private static GitApi gitApi;

    public static GitApi getInstance(){
        if(gitApi==null)
            gitApi=new GitApi();
        return gitApi;
    }

    private GitApi() {
    }

    public GitUser getUserByLogin(String login) {
        if (login.contains(" ")) return new DefaultGitUser();
        String path = "users/" + login;
        InputStream apiStream = getStreamFromAPICall(path);
        JSONObject userAsJson = new JSONObject(getStringFromStream(apiStream));

        return new GitApiResponseParser().parseUserByLoginResponse(userAsJson);
    }

    public ArrayList<String> getUsersByFullName(String fullName) {
        fullName = fullName.replace(" ", "+");
        String path = "search/users?q=fullname:" + fullName;
        InputStream apiStream = getStreamFromAPICall(path);
        JSONObject usersAsJson = new JSONObject(getStringFromStream(apiStream));

        return new GitApiResponseParser().parseUsersByFullName(usersAsJson);
    }

    //Git-Api would return 100 repos max for one request, so we iterate over pages to get all
    public ArrayList<Repo> getReposByLogin(String login) {
        int reposPerPage=100;
        int pageCounter=1;

        ArrayList<Repo> repos = new ArrayList<>();
        ArrayList<Repo> reposLimitedByPageSize;

        do{
            String path = "users/" + login + "/repos?type=all&per_page="+reposPerPage+"&page="+ pageCounter++;
            InputStream apiStream = getStreamFromAPICall(path);
            JSONArray reposJson = new JSONArray(getStringFromStream(apiStream));
            reposLimitedByPageSize = new GitApiResponseParser().parseReposByLogin(reposJson);
            repos.addAll(reposLimitedByPageSize);
        } while(reposLimitedByPageSize.size()==reposPerPage);

        return repos;
    }

    public InputStream getStreamFromAPICall(String path) {
        String urlString = API_URL + path;
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = getConnectionFromUrl(url);
            connection.setRequestMethod("GET");
            if(token != null && !token.isEmpty())
                connection.setRequestProperty("Authorization", "Bearer " + token);
            if(connection.getResponseCode() == CODE_RESOURCE_NOT_FOUND)
                return connection.getErrorStream();
            else if (connection.getResponseCode() == CODE_BAD_CREDENTIALS) {
                Logger.error("Git api returned code 401. Probably your api key is wrong or expired.");
                throw new RuntimeException();
            }

            return connection.getInputStream();
        }catch (IOException e) {
            Logger.error("Processing git-api input stream failed for '" + urlString + "'.", e);
        }
        return InputStream.nullInputStream();
    }

    //Since having issues to mock url for tests, this method is mocked instead
    public HttpURLConnection getConnectionFromUrl(URL url) throws IOException {
        Logger.info("Connecting to '" + url + "'.");
        return (HttpURLConnection) url.openConnection();
    }


    public String getStringFromStream(InputStream inputStream) {
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

    public void downloadRepos(ArrayList<Repo> repos, String path, BlockingQueue<Repo> downloadedRepos) {
        try {
            tryDownloadRepos(repos, path, downloadedRepos);
        } catch (IOException e) {
            Logger.error("Error while processing repo download stream.", e);
        } catch (InterruptedException e) {
            Logger.error("Error in repo download queue.", e);
        }
    }

    public void tryDownloadRepos(ArrayList<Repo> repos, String path, BlockingQueue<Repo> downloadedRepos) throws IOException, InterruptedException {
        for (Repo repo : repos) {
            if(repo.getSizeInKB()>reposMaxSizeInKB)continue;
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

    public void processZipEntry(String path, ZipEntry entry, ZipInputStream zipIn) throws IOException {
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

    public ZipInputStream getZipStreamFromRepo(String repo) {
        String apiPath = "repos/" + repo + "/zipball";
        InputStream in = getStreamFromAPICall(apiPath);
        return new ZipInputStream(in);
    }

    public static String getToken(){return token;}

    public static void setToken(String token) {
        Logger.info("Setting Git-Api token to '" + token + "'.");
        GitApi.token = token;
    }

    public static void setReposMaxSizeInMB(int reposMaxSizeInMB) {
        Logger.info("Setting maximal size of repos to '"+reposMaxSizeInMB+"'MB.");
        GitApi.reposMaxSizeInKB = reposMaxSizeInMB*1000;
    }
}
