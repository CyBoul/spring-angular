package com.cyboul.demo.model.externals;

/**
 * Retrieving Users from https://jsonplaceholder.typicode.com/users
 */
public record UserAPI(
        long id,
        String username,
        String name,
        Address address,
        String email,
        String phone,
        String website,
        Company company
){}
