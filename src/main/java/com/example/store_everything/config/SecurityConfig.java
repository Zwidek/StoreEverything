package com.example.store_everything.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig {


    private final UserDetailsServiceImpl userDetailsService;


    public SecurityConfig(UserDetailsServiceImpl userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        PathRequest.H2ConsoleRequestMatcher h2ConsoleRequestMatcher = PathRequest.toH2Console();
        http.cors().and().csrf().disable()
                .authorizeHttpRequests((authorize) ->
                        authorize.requestMatchers("/user/sign_up").permitAll()
                                .requestMatchers(h2ConsoleRequestMatcher).permitAll()
                                .requestMatchers("/user/register").permitAll()
                                .requestMatchers("/user/sign_in").permitAll()
                                .requestMatchers("/user/login").permitAll()
                                .requestMatchers("/user/logout").permitAll()
                                .requestMatchers("/index").permitAll()
                                .requestMatchers("/*").permitAll()
                                .requestMatchers("/styles/*").permitAll()
                                .requestMatchers("/images/*").permitAll()
                                .requestMatchers("/users/**").permitAll()
                                .requestMatchers("/categories/**").permitAll()
                                .requestMatchers("/store/**").permitAll()
                                .requestMatchers("/shared/**").permitAll()
                                .requestMatchers("/shared_elements/**").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                ).logout(
                        logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .permitAll()
                );
        http.csrf(csrf -> csrf.ignoringRequestMatchers(h2ConsoleRequestMatcher));
        http.headers().frameOptions().disable();
        http.authenticationProvider(authenticationProvider());

        return http.build();
    }

}
