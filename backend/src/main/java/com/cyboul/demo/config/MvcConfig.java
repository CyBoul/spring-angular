package com.cyboul.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {

        // Thymeleaf (/resources/templates)
        // Views: hello.html - home.html - login.html

//        registry.addViewController("/").setViewName("home");
//        registry.addViewController("/home").setViewName("home");
//        registry.addViewController("/hello").setViewName("hello");
//        registry.addViewController("/login").setViewName("login");

        // Angular SPA — forward all non-API, non-asset paths to index.html
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/{path:[^\\.]*}").setViewName("forward:/index.html");
        registry.addViewController("/{path1:[^\\.]*}/{path2:[^\\.]*}").setViewName("forward:/index.html");

    }

}