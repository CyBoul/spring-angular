package com.cyboul.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

//    @Bean
//    public UserDetailsService userDetailsService() {
//        // in-memory temp user for web UI
//        UserDetails user = org.springframework.security.core.userdetails.User
//                .withDefaultPasswordEncoder()
//                .username("admin")
//                .password("admin")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
//
//    /**
//     * Register the Custom HttpClient interface as a Bean
//     * so Spring can manage the RestClient methods for us
//     * by only defining URLS in the contract (interface methods)
//     *
//     * @return an HttpClient for external API users consumption
//     */
//    @Bean
//    UserHttpClient userHttpClient(){
//        HttpServiceProxyFactory factory = HttpServiceProxyFactory
//                .builderFor(RestClientAdapter
//                        .create(RestClient
//                            .create("https://jsonplaceholder.typicode.com")))
//                .build();
//
//        return factory.createClient(UserHttpClient.class);
//    }
//
//    @Bean
//    CommandLineRunner runner(...){
//        return args -> {
//            ...
//        };
//    }

}
