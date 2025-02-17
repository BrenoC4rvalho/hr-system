package com.example.backend.security.provider;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.algorithms.Algorithm;

@Component
public class TokenProvider {
    
    @Value("${security.token.secret.key}")
    private String SECRET_KEY;

    @Value("${security.token.issuer}")
    private String ISSUER;

    @Value("${security.token.expire.in}")
    private int EXPIRES_IN; 

    public Algorithm getAlgorithm() {
        return Algorithm.HMAC256(SECRET_KEY);
    }

    public String getIssuer() {
        return ISSUER;
    }

    public Instant getExpirationDate() {
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).plusHours(EXPIRES_IN).toInstant();
    }

    public Instant getCreationDate() {
        return ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant();
    }

}
