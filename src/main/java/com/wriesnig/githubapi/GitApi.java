package com.wriesnig.githubapi;

import com.wriesnig.utils.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Objects;



/**
 * Class is the Interface to the Github-RestApi. It offers different GET-Methods to retrieve specific data.
 */
public class GitApi {
    private final String apiUrl = "https://api.github.com/";
    private final WebClient client;
    private static String token;


    public GitApi() {
        client = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", "Bearer " + token)
                .build();
    }

    public static void setToken(String token){
        GitApi.token = token;
    }

    /**
     * Get GithubUser by his login(unique).
     * @param login
     * @return
     */
    public GitUser getUserByLogin(String login) {
        if(login.contains(" "))return null;
        WebClient.ResponseSpec response = client.get().uri("/users/" + login).retrieve();
        JSONObject userAsJson = new JSONObject(Objects.requireNonNull(response.bodyToMono(String.class).block()));

        return GitApiDataParser.parseUserByLoginResponse(userAsJson);
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

        return GitApiDataParser.parseUsersByFullName(usersAsJson);
    }

    public ArrayList<String> getReposByLogin(String login){
        WebClient.ResponseSpec response = client.get().uri("users/"+login+"/repos").retrieve();
        JSONArray repos = new JSONArray(Objects.requireNonNull(response.bodyToMono(String.class).block()));

        return GitApiDataParser.parseReposByLogin(repos);
    }

}
