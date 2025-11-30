package com.financialProject.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "kashuamr912233qwertyuiopasdfghjklzxcvbnm"; // 32+ chars
    private final long EXPIRATION = 1000 * 60 * 60 * 10; // 10 hours

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // ✔ Generate token using EMAIL
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)   // store email as subject
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ✔ Extract email (subject)
    public String extractEmail(String token) {
        try {
            return extractAllClaims(token).getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        }
    }

    // ✔ Validate using email
    public boolean validateToken(String token, String email) {
        try {
            final String extractedEmail = extractEmail(token);
            return (extractedEmail.equals(email) && !isTokenExpired(token));
        } catch (JwtException e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractAllClaims(token)
                    .getExpiration()
                    .before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .setAllowedClockSkewSeconds(60)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
