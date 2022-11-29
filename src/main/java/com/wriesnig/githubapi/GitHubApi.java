package com.wriesnig.githubapi;

import com.wriesnig.stackoverflowapi.SOUser;
import com.wriesnig.stackoverflowapi.SOUserDataParser;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class GitHubApi {
    private final String api_url = "https://api.github.com/";
    private WebClient client;
    private WebClient.ResponseSpec response_spec;


    public GitHubApi() {
        client = WebClient.builder()
                .baseUrl(api_url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public GHUser getUserByLogin(String login) {
        if(login.contains(" "))return null;
        WebClient.ResponseSpec response = client.get().uri("/users/" + login).retrieve();
        JSONObject user_as_json = new JSONObject(Objects.requireNonNull(response.bodyToMono(String.class).block()));

        return GHUserDataParser.parseUserByLoginResponse(user_as_json);
    }

    public ArrayList<String> getUsersByFullName(String full_name){
        full_name = full_name.replace(" ", "+");
        WebClient.ResponseSpec response = client.get().uri("/search/users?q=fullname:" + full_name).retrieve();
        JSONObject users_as_json = new JSONObject(Objects.requireNonNull(response.bodyToMono(String.class).block()));

        return GHUserDataParser.parseUsersByFullName(users_as_json);
    }
}
