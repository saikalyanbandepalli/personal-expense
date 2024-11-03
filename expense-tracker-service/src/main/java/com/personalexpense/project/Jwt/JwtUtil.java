package com.personalexpense.project.Jwt;

import com.personalexpense.project.model.Role;
import com.personalexpense.project.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private String SECRET_KEY = "thisisasecureandlongsecretkeytrustmemanthisissecureman123456";

    // Extract username from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract expiration date from token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // General method to extract any claim from token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            System.err.println("Failed to extract claims from token: " + e.getMessage());
            return null; // Handle appropriately
        }
    }

    // Check if token is expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Generate token for user with roles
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        // Add roles as a claim without prefix
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(role -> role.getAuthority()) // No prefix for roles
                .collect(Collectors.toList()));

        return createToken(claims, userDetails.getUsername());
    }

    // Create the token
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        List<String> tokenRoles = extractRoles(token);

        // Log for debugging
        System.out.println("Validating token for user: " + username + " with roles: " + tokenRoles);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Extract roles from token
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        List<String> roles = claims.get("roles", List.class);
        System.out.println("roles from jwt util"+roles);
        if (roles != null) {
            System.out.println("Extracted roles from token: " + roles);
            return roles.stream()
                    .map(String::valueOf) // Ensure casting to String
                    .collect(Collectors.toList());
        }
        return List.of(); // Return empty list if no roles found
    }
}
