package com.example.Assignment.controllers;

import com.example.Assignment.models.Access_Token;
import com.example.Assignment.models.RefreshToken;
import com.example.Assignment.models.TokenClass;
import com.example.Assignment.models.User;
import com.example.Assignment.services.UserService;
import com.example.Assignment.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody User user){
        try{
            userService.createUser(user);
            return new ResponseEntity<>(
                    "User registered successfully",
                    HttpStatus.CREATED
            );
        }catch(Exception e){
            return new ResponseEntity<>(
                    "Could not register user",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody User user){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = jwtUtils.generateAccessToken(user.getEmail());
            String refreshToken = jwtUtils.generateRefreshToken(user.getEmail());
            TokenClass tokenClass = new TokenClass();
            tokenClass.setAccess_token(accessToken);
            tokenClass.setRefresh_token(refreshToken);
            return new ResponseEntity<>(
                    tokenClass,
                    HttpStatus.OK
            );
        }catch (Exception e){
            return new ResponseEntity<>(
                    "No user found",
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshToken refreshToken){
        try {
            String email = jwtUtils.getEmail(refreshToken.getRefresh_token());
            if (email == null || !jwtUtils.isTokenValid(refreshToken.getRefresh_token(), email)) {
                return new ResponseEntity<>("Invalid or expired refresh token", HttpStatus.UNAUTHORIZED);
            }

            // Step 3: Generate new access token
            String newAccessToken = jwtUtils.generateAccessToken(email);

            // Step 4: Return new access token in response
            Access_Token accessToken = new Access_Token();
            accessToken.setAccess_token(newAccessToken);

            return new ResponseEntity<>(accessToken, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing refresh token", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
