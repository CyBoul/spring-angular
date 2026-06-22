package com.cyboul.demo.model.externals;

public record Address (
    String street,
    String suite,
    String zipcode,
    String city,
    Geo geo
){}
