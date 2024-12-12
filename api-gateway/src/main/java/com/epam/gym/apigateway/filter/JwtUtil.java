package com.epam.gym.apigateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
@Slf4j
public class JwtUtil {
    @Value("${security.jwt.secret-key}")
    private String secret;

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey(secret)).build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSecretKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);  // BASE64 decoding used to ensure the correct key length
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (JwtException e) {
            log.error("Invalid JWT: {}", e.getMessage());
            return false;
        }
    }
}
