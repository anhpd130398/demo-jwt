package com.example.jwt;

import com.example.service.CustomUserDetails;
import io.jsonwebtoken.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class JwtUtils {
    private final static String SECRET = "pda123";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().
                setSigningKey(SECRET).
                parseClaimsJws(token).
                getBody();
    }


    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String doGenerateRefreshToken(CustomUserDetails user) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + (24 * 60 * 60 * 1000L)))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
    }

    public String generateToken(CustomUserDetails user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getUsername());
        claims.put("password",user.getPassword());
        claims.put("token",user.getAuthorities());
        return accessToken(claims, user);
    }

    private String accessToken(Map<String, Object> claims, CustomUserDetails user) {
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + (6 * 1000L)))
                .signWith(SignatureAlgorithm.HS512, SECRET).
                compact();
    }

    public Boolean validateToken(String token, CustomUserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}
