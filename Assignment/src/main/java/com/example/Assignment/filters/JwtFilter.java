package com.example.Assignment.filters;

import com.example.Assignment.services.UserDetailsServiceImpl;
import com.example.Assignment.utils.CachedBodyHttpServletRequest;
import com.example.Assignment.utils.JwtUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Wrap the request to cache the body
        if(request.getRequestURI().contains("/products")){
            headerTokenFilter(request);
            filterChain.doFilter(request, response);
            return;
        }
        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);

        // Proceed with the wrapped request
        String body = getRequestBody(cachedRequest);
        String token = null;

        if (body != null) {
            // Extract token from JSON body
            if (body.contains("access_token")) {
                token = extractTokenFromBody(body, "access_token");
            } else if (body.contains("refresh_token")) {
                token = extractTokenFromBody(body, "refresh_token");
            }
        }

        if (token != null) {
            authenticateWithToken(token);
        }

        filterChain.doFilter(cachedRequest, response);
    }

    private void authenticateWithToken(String token) {
        if (token != null) {
            String userEmail = jwtUtils.getEmail(token);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                // Validate the token
                if (jwtUtils.isTokenValid(token, userEmail)) {
                    // Set the authentication in the security context
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
    }

    // Helper method to extract the body from the request
    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder body = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            body.append(line);
        }
        return body.toString();
    }

    // Helper method to extract the token from the request body
    private String extractTokenFromBody(String body, String tokenType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(body);

            // Log the full parsed JSON for debugging
            System.out.println("Parsed JSON: " + jsonNode.toString());

            // Extract the token based on the token type (access_token or refresh_token)
            JsonNode tokenNode = jsonNode.get(tokenType);
            if (tokenNode != null) {
                String token = tokenNode.asText();
                // Log the extracted token
                System.out.println(tokenType + ": " + token);
                return token;
            } else {
                System.out.println(tokenType + " not found in body.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void headerTokenFilter(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null && !authorizationHeader.isEmpty() && authorizationHeader.startsWith("Bearer ")){
            String token = authorizationHeader.substring(7);
            String userEmail = jwtUtils.getEmail(token);

            if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                if(jwtUtils.isTokenValid(token, userEmail)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
    }
}
