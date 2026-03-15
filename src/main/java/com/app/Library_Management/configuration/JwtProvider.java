package com.app.Library_Management.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;

@Service
public class JwtProvider {
    private final SecretKey secretKey = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    /**
     * Generates a JWT token from the authentication object
     * @param authentication the Spring Security Authentication object
     * @return the generated JWT token
     * @throws IllegalArgumentException if authentication is null or has no authorities
     */
    public String generateToken(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("Authentication cannot be null");
        }
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roles = populatedAuthorities(authorities);
        
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Token valid for 1 day
                .claim("email", authentication.getName())
                .claim("authorities", roles)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Extracts the email from a JWT token
     * @param token the JWT token string
     * @return the email claim from the token
     * @throws IllegalArgumentException if token is null or empty
     * @throws JwtException if token is invalid or expired
     */
    public String getEmailFromJwtToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("email", String.class);
        } catch (ExpiredJwtException e) {
            throw new JwtException("Token has expired", e);
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token", e);
        }
    }

    /**
     * Validates if a JWT token is valid
     * @param token the JWT token string
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Extracts claims from a JWT token
     * @param token the JWT token string
     * @return the Claims object from the token
     * @throws JwtException if token is invalid
     */
    public Claims getClaimsFromToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new JwtException("Invalid JWT token", e);
        }
    }

    /**
     * Formats authorities into a comma-separated string
     * @param authorities the collection of granted authorities
     * @return comma-separated authorities string
     */
    private String populatedAuthorities(Collection<? extends GrantedAuthority> authorities) {
        StringBuilder stringBuilder = new StringBuilder();
        for (GrantedAuthority authority : authorities) {
            stringBuilder.append(authority.getAuthority()).append(",");
        }
        if (!stringBuilder.isEmpty()) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1); // Remove the trailing comma
        }
        return stringBuilder.toString();
    }
}
