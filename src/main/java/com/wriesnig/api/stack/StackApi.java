package com.wriesnig.api.stack;

import com.wriesnig.utils.Logger;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class StackApi {
    public static final String API_URL = "https://api.stackexchange.com/2.3/";
    public static final int CODE_OK = 200;
    public static final int CODE_BAD_REQUEST = 400;
    public static String key = "";
    public static int backOffParameterInSecs;

    private static StackApi stackApi;

    public static StackApi getInstance(){
        if(stackApi==null)
            stackApi=new StackApi();
        return stackApi;
    }

    public ArrayList<StackUser> getUsers(ArrayList<Integer> ids) {
        StackApiResponseParser responseParser = new StackApiResponseParser();
        ArrayList<StackUser> users = new ArrayList<>();
        int maxIdsPerRequest = 100;
        int loopCount = (ids.size()/maxIdsPerRequest)+1;
        //Request only allows 100 ids at once, so we split the input if size()>100
        for(int i=1; i<= loopCount;i++){
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
            JSONObject usersAsJson = getResponse(path);
            backOffParameterInSecs = usersAsJson.has("backoff")?usersAsJson.getInt("backoff"):0;
            users.addAll(responseParser.parseUsersResponse(usersAsJson));
        }
        return users;
    }

    public ArrayList<String> getMainTags(int id) {
        String path = "users/" + id + "/top-answer-tags?pagesize=3&site=stackoverflow";
        JSONObject tags = getResponse(path);
        backOffParameterInSecs = tags.has("backoff")?tags.getInt("backoff"):0;
        StackApiResponseParser responseParser = new StackApiResponseParser();
        return responseParser.parseTagsResponse(tags);
    }

    public JSONObject getResponse(String path){
        GZIPInputStream apiStream = getStreamFromAPICall(path);
        String content = getStringFromStream(apiStream);
        return new JSONObject(content);
    }

    public GZIPInputStream getStreamFromAPICall(String path) {
        String url = API_URL + path + "&key="+key;
        try {
            URL getUrl = new URL(url);
            if(backOffParameterInSecs !=0)
                waitBackOffTime();
            HttpURLConnection connection = getConnectionFromUrl(getUrl);
            connection.setRequestMethod("GET");
            int status = connection.getResponseCode();
            if(status==CODE_OK)return new GZIPInputStream(connection.getInputStream());
            String errorMessage = getStringFromStream(new GZIPInputStream(connection.getErrorStream()));
            JSONObject errorMessageJson = new JSONObject(errorMessage);
            String errorName = errorMessageJson.getString("error_name");
            Logger.error("Stack-Api error: " + errorName);
            throw new RuntimeException(errorMessage);
        } catch (IOException e) {
            Logger.error("Processing stack-api input stream failed.", e);
        }
        return null;
    }

    //Since having issues to mock url for tests, this method is mocked instead
    public HttpURLConnection getConnectionFromUrl(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    public void waitBackOffTime(){
        try{
            Thread.sleep(backOffParameterInSecs* 1000L);
        } catch (InterruptedException e) {
            Logger.error("Waiting backoff time for Stack-API failed.", e);
            throw new RuntimeException();
        }
    }


    public String getStringFromStream(GZIPInputStream inputStream) {
        if(inputStream==null)return "{}";
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
