package com.cyboul.demo.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import static com.cyboul.demo.model.user.Role.ROLE_USER;

@Getter
@Setter
@Entity(name = "USR")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = ROLE_USER;

    public User(){}

    @JsonCreator
    public User(@NotNull Long id, @NotEmpty String username, @NotEmpty String password,
                @NotEmpty String email, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role != null ? role : ROLE_USER;
    }

}
