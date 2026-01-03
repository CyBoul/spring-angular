package com.cyboul.demo.logic.disabled;

import com.cyboul.demo.model.externals.UserAPI;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

/**
 * Retrieving Users from https://jsonplaceholder.typicode.com/users
 * through RestClient
 */
// DISABLED
//@Component
public class UserRestClient {

    private final RestClient restClient;

    public UserRestClient(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("https://jsonplaceholder.typicode.com")
                //.defaultHeader(...)
                .build();
    }

    public List<UserAPI> findAll(){
        return restClient.get()
                .uri("/users")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public UserAPI findById(Long id){
        return restClient.get()
                .uri("/users/{id}", id)
                .retrieve()
                .body(UserAPI.class);
    }
}
