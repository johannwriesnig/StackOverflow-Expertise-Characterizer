package com.wriesnig.api.stack;

import com.wriesnig.utils.Logger;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class StackApi {
    private static final String apiUrl = "https://api.stackexchange.com/2.3/";
    private static final StackApiResponseParser responseParser = new StackApiResponseParser();

    private StackApi(){}

    public static ArrayList<StackUser> getUsers(ArrayList<Integer> ids) {
        String idsList = ids.stream().map(Object::toString)
                .collect(Collectors.joining(";"));

        String path = "users/" + idsList + "?site=stackoverflow";
        GZIPInputStream apiStream = getStreamFromAPICall(path);
        String stream = getStringFromStream(apiStream);
        JSONObject usersAsJson = new JSONObject(stream);

        return responseParser.parseUsersResponse(usersAsJson);
    }

    public static ArrayList<String> getMainTags(int id) {
        String path = "users/" + id + "/top-answer-tags?pagesize=3&site=stackoverflow";
        GZIPInputStream apiStream = getStreamFromAPICall(path);
        String stream = getStringFromStream(apiStream);
        JSONObject tags = new JSONObject(stream);

        return responseParser.parseTagsResponse(tags);
    }

    public static GZIPInputStream getStreamFromAPICall(String path) {
        String url = apiUrl + path;
        try {
            URL getUrl = new URL(apiUrl + path);
            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
            connection.setRequestMethod("GET");
            return getGzipInputStream(connection);
        } catch (MalformedURLException e) {
            Logger.error("Url for requesting stack-api is malformed -> " + url, e);
        } catch (IOException e) {
            Logger.error("Processing stack-api input stream failed.", e);
        }
        return null;
    }

    public static GZIPInputStream getGzipInputStream(HttpURLConnection connection) throws IOException {
        System.out.println("hallo");
        return new GZIPInputStream(connection.getInputStream());
    }

    public static String getStringFromStream(GZIPInputStream inputStream) {
        if(inputStream==null)return "";
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Reader reader = new InputStreamReader(inputStream);
            int ch;
            while ((ch = reader.read()) != -1) {
                stringBuilder.append((char) ch);
            }
        } catch (IOException e) {
            Logger.error("Reading from stack-api input stream failed.", e);
        }
        return stringBuilder.toString();
    }



}
