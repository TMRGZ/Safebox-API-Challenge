package com.rviewer.skeletons.infrastructure.config;

import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.rviewer.skeletons.infrastructure.provider.impl.BasicAuthProviderImpl;
import com.rviewer.skeletons.infrastructure.provider.impl.JwtProviderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, JwtProviderImpl jwtProvider, BasicAuthProviderImpl basicAuthProvider) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        return builder.authenticationProvider(basicAuthProvider)
                .authenticationProvider(jwtProvider)
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return new NimbusJwtDecoder(new DefaultJWTProcessor<>());
    }

    @Bean
    public SecurityFilterChain basicFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable()
                .authorizeHttpRequests()
                .regexMatchers(HttpMethod.GET, "/safebox/.+/open").authenticated()
                .and()
                .httpBasic()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        return httpSecurity.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain jwtFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.regexMatcher("/safebox/.+/items")
                .csrf().disable()
                .authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .oauth2ResourceServer()
                .jwt();

        return httpSecurity.build();
    }
}
