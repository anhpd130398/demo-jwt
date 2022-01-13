package com.example.jwt;

import com.example.service.CustomUserDetails;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.introspect.AnnotationCollector;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userServices;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String headerToken=request.getHeader("Authorization");
        try {
            if (headerToken !=null&& headerToken.startsWith("Bearer")){
                String token=headerToken.substring(7);
                String userName=jwtUtils.extractUsername(token);
                if (userName!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                  UserDetails userDetails=userServices.loadUserByUsername(token);
                  if (jwtUtils.validateToken(token, (CustomUserDetails) userDetails)){
                      UsernamePasswordAuthenticationToken authenticationToken = new
                              UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                      authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                  }
                }
            }
        }
        catch (ExpiredJwtException ex) {
//            String isRefreshToken = request.getHeader("isRefreshToken");
//            String requestURL = request.getRequestURL().toString();
//            if (isRefreshToken != null && isRefreshToken.equals("true") && requestURL.contains("refreshToken")) {
//                allowForRefreshToken(ex, request);
//            } else
                request.setAttribute("exception", ex);
        } catch (BadCredentialsException ex) {
            request.setAttribute("exception", ex);
        } catch (SignatureException ex) {
            request.setAttribute("exceptions", ex);
        }
        filterChain.doFilter(request, response);
    }
}
