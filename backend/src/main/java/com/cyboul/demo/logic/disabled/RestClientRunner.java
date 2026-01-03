package com.cyboul.demo.logic.disabled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;

/**
 * Consume an external and public API
 * https://jsonplaceholder.typicode.com/users
 *
 */
//@Component // Disabled
public class RestClientRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(RestClientRunner.class);

    private final UserHttpClient client;

    public RestClientRunner(UserHttpClient userHttpClient){
        //this.client = userRestClient;
        this.client = userHttpClient;
    }

    @Override
    public void run(String... args) {
        client.findAll()
              .stream()
              .limit(2)
              .forEach(u -> {
                  log.info(u.toString());
              });
    }
}
