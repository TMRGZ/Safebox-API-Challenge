package com.rviewer.skeletons.infrastructure.service.impl;

import com.rviewer.skeletons.domain.service.TokenService;
import com.rviewer.skeletons.infrastructure.config.AppConfig;
import com.rviewer.skeletons.infrastructure.utils.AuthenticationUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String generate() {
        return createToken(new HashMap<>(), AuthenticationUtils.getLoggedInUsername());
    }

    @Override
    public String decode(String token) {
        String subject;

        try {
            JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(appConfig.getTokenSecret().getBytes())
                    .build();

            Claims claimsJws = jwtParser.parseClaimsJws(token).getBody();
            subject = claimsJws.getSubject();

        } catch (JwtException e) {
            subject = null;
        }

        return subject;
    }

    @Override
    public String getDecodedUsername() {
        return AuthenticationUtils.getTokenUsername();
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
