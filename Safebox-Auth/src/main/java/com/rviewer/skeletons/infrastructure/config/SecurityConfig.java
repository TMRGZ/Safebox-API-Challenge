package com.rviewer.skeletons.infrastructure.config;

import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import com.rviewer.skeletons.infrastructure.provider.BasicAuthenticationProvider;
import com.rviewer.skeletons.infrastructure.provider.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BasicAuthenticationProvider basicAuthProvider, JwtAuthenticationProvider jwtAuthenticationProvider) throws Exception {
        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);

        return builder.authenticationProvider(basicAuthProvider)
                .authenticationProvider(jwtAuthenticationProvider)
                .build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return new NimbusJwtDecoder(new DefaultJWTProcessor<>());
    }


    @Bean
    public SecurityFilterChain basicFilterChain(HttpSecurity httpSecurity, AuthenticationEntryPoint entryPoint) throws Exception {

        httpSecurity.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST, "/login").authenticated()
                .requestMatchers(HttpMethod.POST, "/user").permitAll()
                .and()
                .httpBasic().authenticationEntryPoint(entryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        return httpSecurity.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain jwtFilterChain(HttpSecurity httpSecurity, AuthenticationEntryPoint entryPoint) throws Exception {
        httpSecurity.securityMatcher("/token/decode")
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(entryPoint)
                .and()
                .authorizeHttpRequests()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .oauth2ResourceServer()
                .jwt();

        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/actuator", "/error",
                "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**"
        );
    }
}
