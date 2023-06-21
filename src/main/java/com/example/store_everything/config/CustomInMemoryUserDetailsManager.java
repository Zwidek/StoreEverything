package com.example.store_everything.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class CustomInMemoryUserDetailsManager extends InMemoryUserDetailsManager {

    public CustomInMemoryUserDetailsManager() {
        User.UserBuilder userBuilder = User.builder();
        UserDetails admin = userBuilder.username("admin").password("{noop}admin").roles("ADMIN").build();
        UserDetails user1 = userBuilder.username("user").password("{noop}user").roles("USER").build();
        createUser(admin);
        createUser(user1);
    }
}
