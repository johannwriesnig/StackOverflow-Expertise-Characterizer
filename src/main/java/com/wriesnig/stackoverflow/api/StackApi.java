package com.wriesnig.stackoverflow.api;


import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Interface to the StackOverflowApi that is needed since some data are missing in the dumps
 */
public class StackApi {
    private final String apiUrl = "https://api.stackexchange.com/2.3";
    private final WebClient client;


    public StackApi() {
        client = WebClient.builder()
                .baseUrl(apiUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Get Users based on their Ids
     * @param ids
     * @return
     */
    public ArrayList<StackUser> getUsers(ArrayList<Integer> ids) {
        String idsList = ids.stream().map(Object::toString)
                .collect(Collectors.joining(";"));

        WebClient.ResponseSpec response = client.get().uri("/users/" + idsList + "?site=stackoverflow").retrieve();
        JSONObject usersAsJson = new JSONObject(Objects.requireNonNull(response.bodyToMono(String.class).block()));
        return StackApiDataParser.parseUsersResponse(usersAsJson);
    }

}
