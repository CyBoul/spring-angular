package com.cyboul.demo.web;

import com.cyboul.demo.logic.service.UserService;
import com.cyboul.demo.model.AuthRequest;
import com.cyboul.demo.logic.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(
            AuthenticationManager authManager,
            JwtService jwtService,
            UserService userService
    ){
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String,String>> login(@RequestBody AuthRequest request){
        log.info("Login attempt '{}'", request.email()); // toRmv
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(), request.password()));
        } catch (BadCredentialsException e){
            log.error("Login attempt failed for '{}' : {}", request.email(), e.getMessage());
            return ResponseEntity.badRequest().build();
        }
        UserDetails userDetails = userService.loadUserByEmail(request.email());
        String jwt = jwtService.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(Collections.singletonMap("token", jwt));
    }

    @GetMapping("/gugu")
    @ResponseBody
    public Map<String, Object> gugu(){
        Map<String, Object> model = new HashMap<>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", "Hello World");
        return model;
    }

}
