package com.rviewer.skeletons.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests()
                .antMatchers("/safebox-auth/user").permitAll()
                .antMatchers("/error").permitAll()
                .anyRequest().authenticated().and()
                .httpBasic().and()
                .csrf().disable()
                .build();
    }
}
