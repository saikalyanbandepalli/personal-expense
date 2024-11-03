package com.personalexpense.project.Jwt;

import com.personalexpense.project.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");
        logger.debug("Incoming request URI: {}", request.getRequestURI());
        logger.debug("Authorization header: {}", authorizationHeader);


        String username = null;
        String jwt = null;

        // Bypass JWT validation for the login and registration endpoints
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/api/users/login") || requestURI.equals("/api/users/register") || requestURI.equals("/error") || requestURI.equals("/api/roles/getroles")) {
            logger.debug("Bypassing security for URI: " + requestURI);
            chain.doFilter(request, response); // Proceed with the request
            return;
        }

        // Check for JWT in the Authorization header (e.g., "Bearer <token>")
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (ExpiredJwtException e) {
                logger.warn("JWT token has expired");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired");
                return; // Stop further processing
            } catch (Exception e) {
                logger.error("Error parsing JWT token: {}", e.getMessage());
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token");
                return; // Stop further processing
            }

        } else {
            logger.warn("Missing or invalid Authorization header");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return; // Stop further processing
        }

        logger.debug("JWT Token: " + jwt);
        logger.debug("Username from token: " + username);

        // If the token is valid, set the security context with user details
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userService.loadUserByUsername(username);  // Fetch UserDetails
            logger.debug("User roles: {}", userDetails.getAuthorities());
            if (userDetails != null && jwtUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()  // Pass authorities directly
                );
//                if (authentication != null) {
//                    System.out.println("Authenticated user: " + authentication.getName());
//                    System.out.println("User authorities: " + authentication.getAuthorities());
//                }

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token or user not found");
                return;
            }
        }



        chain.doFilter(request, response); // Proceed with the request
    }
}
