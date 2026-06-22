package com.cyboul.demo.dto.externals;

public record Address (
    String street,
    String suite,
    String zipcode,
    String city,
    Geo geo
){}
