package com.wriesnig.stackoverflowapi;


import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class StackOverFlowApi {
    private final String api_url = "https://api.stackexchange.com/2.3";
    private final WebClient client;


    public StackOverFlowApi() {
        client = WebClient.builder()
                .baseUrl(api_url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public ArrayList<SOUser> getUsers(ArrayList<Integer> ids) {
        String ids_list = ids.stream().map(Object::toString)
                .collect(Collectors.joining(";"));

        WebClient.ResponseSpec response = client.get().uri("/users/" + ids_list + "?site=stackoverflow").retrieve();
        JSONObject users_as_json = new JSONObject(Objects.requireNonNull(response.bodyToMono(String.class).block()));
        return SOUserDataParser.parseUsersResponse(users_as_json);
    }

}
