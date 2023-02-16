package com.rviewer.skeletons.infrastructure.config;

import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.rviewer.skeletons.infrastructure.provider.BasicAuthenticationProvider;
import com.rviewer.skeletons.infrastructure.provider.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BasicAuthenticationProvider basicAuthProvider, JwtAuthenticationProvider jwtAuthenticationProvider) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        return builder.authenticationProvider(basicAuthProvider)
                .authenticationProvider(jwtAuthenticationProvider)
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests()
                .antMatchers("/safebox-auth/user").permitAll()
                .antMatchers("/error").permitAll()
                .anyRequest().authenticated().and()
                .httpBasic().and()
                .csrf().disable()
                .oauth2ResourceServer().jwt();


        return httpSecurity.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return new NimbusJwtDecoder(new DefaultJWTProcessor<>());
    }
}
