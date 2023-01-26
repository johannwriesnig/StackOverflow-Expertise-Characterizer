package com.wriesnig.api.stack;

import com.wriesnig.utils.Logger;
import org.json.JSONObject;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class StackApi {
    private static final String apiUrl = "https://api.stackexchange.com/2.3/";

    private StackApi() {
    }

    public static ArrayList<StackUser> getUsers(ArrayList<Integer> ids) {
        String idsList = ids.stream().map(Object::toString)
                .collect(Collectors.joining(";"));

        String path = "users/" + idsList + "?site=stackoverflow";
        InputStream apiStream = getStreamFromAPICall(path);
        String stream = getStringFromStream(apiStream);
        JSONObject usersAsJson = new JSONObject(stream);

        return StackApiResponseParser.parseUsersResponse(usersAsJson);
    }

    public static ArrayList<String> getMainTags(int id) {
        String path = "users/" + id + "/top-answer-tags?site=stackoverflow";
        InputStream apiStream = getStreamFromAPICall(path);
        String stream = getStringFromStream(apiStream);
        JSONObject tags = new JSONObject(stream);

        return StackApiResponseParser.parseTagsResponse(tags);
    }

    private static InputStream getStreamFromAPICall(String path) {
        try {
            URL url = new URL(apiUrl + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            return connection.getInputStream();
        } catch (IOException e) {
            Logger.error("Issues while requesting stack api", e);
        }
        return InputStream.nullInputStream();
    }

    public static String getStringFromStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Reader reader = new InputStreamReader(new GZIPInputStream(inputStream));
            int ch;
            while ((ch = reader.read()) != -1) {
                stringBuilder.append((char) ch);
            }
        } catch (IOException e) {
            Logger.error("Issues while creating string from stack api stream", e);
        }
        return stringBuilder.toString();
    }

}