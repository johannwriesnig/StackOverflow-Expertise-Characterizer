package com.wriesnig.githubapi;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Objects;



/**
 * Class is the Interface to the Github-RestApi. It offers different GET-Methods to retrieve specific data.
 */
public class GitHubApi {
    private final String apiUrl = "https://api.github.com/";
    private WebClient client;
    private WebClient.ResponseSpec response_spec;


    public GitHubApi() {
        client = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Get GithubUser by his login(unique).
     * @param login
     * @return
     */
    public GHUser getUserByLogin(String login) {
        if(login.contains(" "))return null;
        WebClient.ResponseSpec response = client.get().uri("/users/" + login).retrieve();
        JSONObject userAsJson = new JSONObject(Objects.requireNonNull(response.bodyToMono(String.class).block()));

        return GHUserDataParser.parseUserByLoginResponse(userAsJson);
    }

    /**
     * Get GithubUsers based on full_name which is not unique.
     * @param fullName
     * @return
     */
    public ArrayList<String> getUsersByFullName(String fullName){
        fullName = fullName.replace(" ", "+");
        WebClient.ResponseSpec response = client.get().uri("/search/users?q=fullname:" + fullName).retrieve();
        JSONObject usersAsJson = new JSONObject(Objects.requireNonNull(response.bodyToMono(String.class).block()));

        return GHUserDataParser.parseUsersByFullName(usersAsJson);
    }
}
