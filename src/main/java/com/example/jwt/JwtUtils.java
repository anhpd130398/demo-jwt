package com.example.jwt;

import com.example.service.CustomUserDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
public class JwtUtils {
    private final static String SECRET = "pda123@9898";

    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Map<String,Object> claim = new HashMap<>();
        claim.put("roles",userDetails.getAuthorities());
        Date expiryDate = new Date(now.getTime() + 60 * 1000L);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setClaims(claim)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
        Claims claims =Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
        List<SimpleGrantedAuthority> listRole = new ArrayList<>();

        List<LinkedHashMap> roles = claims.get("roles", List.class);
        roles.forEach(e -> {
            listRole.add(new SimpleGrantedAuthority(e.toString().replace("{authority=", "").replace("}", "")));
        });
        return listRole;
    }


    public String getUserNameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }



    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }


}
