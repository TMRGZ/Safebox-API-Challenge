package com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.model.user.User;
import com.rviewer.skeletons.domain.service.TokenService;
import com.rviewer.skeletons.infrastructure.config.AppConfig;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private AppConfig appConfig;

    @Override
    public String generateToken(User user) {
        return createToken(new HashMap<>(), user.getUsername());
    }

    @Override
    public boolean validateToken(String token) {
        boolean valid = true;

        try {
            JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(appConfig.getTokenSecret().getBytes())
                    .build();

            jwtParser.parseClaimsJwt(token);

        } catch (JwtException e) {
            valid = false;
        }

        return valid;
    }

    private String createToken(Map<String, Objects> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(DateUtils.addMinutes(new Date(), appConfig.getTokenExpirationMinutes()))
                .signWith(Keys.hmacShaKeyFor(appConfig.getTokenSecret().getBytes()))
                .compact();
    }
}
