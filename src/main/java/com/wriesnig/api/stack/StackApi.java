package com.wriesnig.api.stack;

import com.wriesnig.utils.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class StackApi {
    public static final String API_URL = "https://api.stackexchange.com/2.3/";
    public static final int CODE_OK = 200;
    public static final int CODE_BAD_REQUEST = 400;
    public static final int CODE_INTERNAL_ERROR = 500;
    public static final int CODE_THROTTLE_VIOLATION = 502;
    public static final int CODE_TEMPORARILY_UNAVAILABLE = 503;
    private static final StackApiResponseParser responseParser = new StackApiResponseParser();
    public static String key = "";
    public static int backOffParameterInSecs;




    public static ArrayList<StackUser> getUsers(ArrayList<Integer> ids) {
        ArrayList<StackUser> users = new ArrayList<>();
        int maxIdsPerRequest = 100;
        //Request only allows 100 ids at once, so we split the input if size()>100
        for(int i=1; i<=(ids.size()/maxIdsPerRequest)+1;i++){
            ArrayList<Integer> partOfIds = new ArrayList<>();
            Iterator<Integer> iterator = ids.iterator();
            int counter=1;
            while(iterator.hasNext() && counter++ <= maxIdsPerRequest){
                partOfIds.add(iterator.next());
                iterator.remove();
            }
            String idsList = partOfIds.stream().map(Object::toString)
                    .collect(Collectors.joining(";"));
            String path = "users/" + idsList + "?site=stackoverflow&pagesize="+maxIdsPerRequest;
            GZIPInputStream apiStream = getStreamFromAPICall(path);
            String stream = getStringFromStream(apiStream);
            JSONObject usersAsJson = new JSONObject(stream);
            users.addAll(responseParser.parseUsersResponse(usersAsJson));
        }
        return users;
    }

    public static ArrayList<String> getMainTags(int id) {
        String path = "users/" + id + "/top-answer-tags?pagesize=3&site=stackoverflow";
        GZIPInputStream apiStream = getStreamFromAPICall(path);
        String stream = getStringFromStream(apiStream);
        JSONObject tags = new JSONObject(stream);
        backOffParameterInSecs = tags.has("backoff")?tags.getInt("backoff"):0;
        return responseParser.parseTagsResponse(tags);
    }

    public static GZIPInputStream getStreamFromAPICall(String path) {
        String url = API_URL + path + "&key="+key;
        try {
            URL getUrl = getUrl(url);
            if(backOffParameterInSecs !=0)
                waitBackOffTime();
            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
            connection.setRequestMethod("GET");
            int status = connection.getResponseCode();
            if(status==CODE_OK)return new GZIPInputStream(connection.getInputStream());
            String errorMessage = getStringFromStream(new GZIPInputStream(connection.getErrorStream()));
            JSONObject errorMessageJson = new JSONObject(errorMessage);
            status = errorMessageJson.getInt("error_id");
            if(status == CODE_THROTTLE_VIOLATION)
                Logger.error("Stack api throttle violation occurred. Maybe the rate limit per day is reached.");
            else if(status == CODE_INTERNAL_ERROR)
                Logger.error("Stack api had an internal error.");
            else if(status == CODE_TEMPORARILY_UNAVAILABLE)
                Logger.error("Stack api is currently unavailable.");
            throw new RuntimeException(errorMessage);
        } catch (MalformedURLException e) {
            Logger.error("Url for requesting stack-api is malformed -> " + url, e);
        } catch (IOException e) {
            Logger.error("Processing stack-api input stream failed.", e);
        }
        return null;
    }

    private static void waitBackOffTime(){
        try{
            Thread.sleep(backOffParameterInSecs* 1000L);
        } catch (InterruptedException e) {
            Logger.error("Waiting backoff time for Stack-API failed.", e);
            throw new RuntimeException();
        }
    }

    public static URL getUrl(String url) throws MalformedURLException {
        return new URL(url);
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

    public static void setKey(String key){
        StackApi.key = key;
    }

}
