package com.wriesnig.stackoverflowapi;


import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class SOAPI {
    private final String api_url = "https://api.stackexchange.com/2.3";
    private WebClient client;
    private WebClient.ResponseSpec response_spec;


    public SOAPI() {
        client = WebClient.builder()
                .baseUrl(api_url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public ArrayList<SOUser> getSOUsers(ArrayList<Integer> ids) {
        String ids_list = ids.stream().map(Object::toString)
                .collect(Collectors.joining(";"));

        response_spec = client.get().uri("/users/" + ids_list + "?site=stackoverflow").retrieve();
        JSONObject users_as_json = new JSONObject(Objects.requireNonNull(response_spec.bodyToMono(String.class).block()));
        ArrayList<SOUser> users = SOUserDataParser.parseUsersResponse(users_as_json);
        return users;
    }

}
