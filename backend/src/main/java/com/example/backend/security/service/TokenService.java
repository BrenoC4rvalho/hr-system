package com.example.backend.security.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.backend.enums.UserRole;
import com.example.backend.security.provider.TokenProvider;

public class TokenService {

    private final TokenProvider jwtProvider;
    
    public TokenService(TokenProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    public DecodedJWT validateToken(String token) {
        try {

            return JWT.require(jwtProvider.getAlgorithm())
                .build()
                .verify(token);
        
        } catch (Exception e) {
            throw new JWTVerificationException("Expired token or Invalid.");
        }
    }

    public String generateToken(Long id, UserRole role) {
        try {

            if (id == null || role == null) {
                throw new IllegalArgumentException("Invalid token payload.");
            }

            return JWT.create()
                .withIssuer(jwtProvider.getIssuer())
                .withIssuedAt(jwtProvider.getCreationDate())
                .withExpiresAt(jwtProvider.getExpirationDate())
                .withSubject(id.toString())
                .withClaim("role", role.toString())
                .sign(jwtProvider.getAlgorithm());    

        } catch (JWTCreationException e) {
            throw new JWTVerificationException("Error to generate token.");
        }
        
    }

}
