//package com.cyboul.demo.exec;
//
//
//import com.cyboul.demo.logic.data.disabled.UserRestClient;
//import com.cyboul.demo.model.externals.Address;
//import com.cyboul.demo.model.externals.Company;
//import com.cyboul.demo.model.externals.Geo;
//import com.cyboul.demo.model.externals.UserAPI;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.client.MockRestServiceServer;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
//import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
//
//@RestClientTest(UserRestClient.class)
//public class UserRestClientTests {
//
//    @Autowired MockRestServiceServer server;
//    @Autowired ObjectMapper mapper;
//
//    UserRestClient client;
//
//    @Test
//    void shouldFindAll() throws JsonProcessingException {
//
//        UserAPI usr1 = new UserAPI(
//                111L,
//                "CoolGuy",
//                "CoolName",
//                new Address("Jl Lombok",
//                        "RT.4/RW.5",
//                        "10350",
//                        "Jakarta",
//                        new Geo("-6.1754049",
//                                "106.827168")),
//                "email@fake.com",
//                "",
//                "",
//                new Company("BigCompany",
//                        "we're the best",
//                        "Technologies")
//        );
//
//        List<UserAPI> users = List.of(usr1);
//
//        // Mocking: When...
//        this.server.expect(requestTo("https://jsonplaceholder.typicode.com/users"))
//                .andRespond(withSuccess(mapper.writeValueAsString(users), MediaType.APPLICATION_JSON));
//
//        assertEquals(users, client.findAll());
//    }
//
//    @Test
//    void shouldFindById() throws JsonProcessingException {
//
//        UserAPI usr1 = new UserAPI(
//                111L,
//                "CoolGuy",
//                "CoolName",
//                new Address("Jl Lombok",
//                        "RT.4/RW.5",
//                        "10350",
//                        "Jakarta",
//                        new Geo("-6.1754049",
//                                "106.827168")),
//                "email@fake.com",
//                "",
//                "",
//                new Company("BigCompany",
//                        "We're the best",
//                        "Technologies")
//        );
//
//        // Mocking: When...
//        this.server.expect(requestTo("https://jsonplaceholder.typicode.com/users/111"))
//                .andRespond(withSuccess(mapper.writeValueAsString(usr1), MediaType.APPLICATION_JSON));
//
//        UserAPI coolGuy = client.findById(111L);
//        assertEquals("CoolName", coolGuy.name(),"The name must be 'CoolName'");
//        assertEquals("CoolGuy", coolGuy.username(),"The username must be 'CoolGuy'");
//        assertEquals("email@fake.com", coolGuy.email(),"The email must be 'email@fake.com'");
//        assertAll("Address",
//                () -> assertEquals("Jl Lombok", coolGuy.address().street()),
//                () -> assertEquals("RT.4/RW.5", coolGuy.address().suite()),
//                () -> assertEquals("Jakarta", coolGuy.address().city()),
//                () -> assertEquals("10350", coolGuy.address().zipcode()),
//                () -> assertEquals("-6.1754049", coolGuy.address().geo().lat()),
//                () -> assertEquals("106.827168", coolGuy.address().geo().lng())
//        );
//        assertEquals("", coolGuy.phone(),"The phone must be empty");
//        assertEquals("", coolGuy.website(),"The website must be empty");
//        assertAll("Company",
//                () -> assertEquals("BigCompany", coolGuy.company().name(),"The company name must be 'BigCompany"),
//                () -> assertEquals("We're the best", coolGuy.company().catchPhrase(),"The catchPhrase must be 'We're the best'"),
//                () -> assertEquals("Technologies", coolGuy.company().bs(),"The bs must be 'Technologies'")
//        );
//    }
//}
