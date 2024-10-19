package com.epam.gym.security.jwt;

import com.epam.gym.model.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtUtil {
    @Value("${security.jwt.secret-key}")
    private String secret;

    @Value("${security.jwt.issuer}")
    private String issuer;

    @Value("${security.jwt.expiration-ms}")
    private long expirationTime = 1000 * 60 * 60 * 10L;

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey(secret)).build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public SecretKey getSecretKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);  // BASE64 decoding used to ensure the correct key length
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String generateToken(UserDetails userDetails, UserRole role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", role.name());
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .claims()
                    .issuer(issuer)
                    .subject(subject)
                    .add("roles", claims.get("roles"))
                    .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSecretKey(secret))
                .compact();
    }

}
