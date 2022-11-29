package wriesnig;


import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class StackOverflowAPI {
    private final String api_url = "https://api.stackexchange.com/2.3";
    private WebClient client;
    private WebClient.ResponseSpec response_spec;


    public StackOverflowAPI() {
        client = WebClient.builder()
                .baseUrl(api_url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public JSONObject getUserInfo(ArrayList<Integer> ids) {
        String ids_list = ids.stream().map(Object::toString)
                .collect(Collectors.joining(";"));

        response_spec = client.get().uri("/users/" + ids_list + "?site=stackoverflow").retrieve();
        JSONObject response_body = new JSONObject(Objects.requireNonNull(response_spec.bodyToMono(String.class).block()));
        return response_body;
    }

}
