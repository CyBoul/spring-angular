package com.cyboul.demo.logic.disabled;

import com.cyboul.demo.model.externals.UserAPI;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

/**
 * Write even less boilerplate
 */
public interface UserHttpClient {

    @GetExchange("/users")
    public List<UserAPI> findAll();

    @GetExchange("/users/{id}")
    public UserAPI findById(@PathVariable Long id);

}
