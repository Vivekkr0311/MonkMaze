package com.example.Assignment.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    private static final String SECRET_KEY = "randomsecretforjwttouseanditmustbe256bitslong";
    private static final long ACCESS_TOKEN_EXPIRATION = 1000 * 60 * 5; // 5 minutes
    private static final long REFRESH_TOKEN_EXPIRATION = 1000 * 60 * 60 * 5; // 5 hours

    public String generateAccessToken(String email){
        return Jwts
                .builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(String email){
        return Jwts
                .builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public Key getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    private Claims getClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Date getExpiration(String token){
        return getClaims(token).getExpiration();
    }

    public String getEmail(String token){
        return getClaims(token).getSubject();
    }

    private boolean isTokenExpired(String token){
        return getExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, String email){
        return getEmail(token).equals(email) && !isTokenExpired(token);
    }
}
