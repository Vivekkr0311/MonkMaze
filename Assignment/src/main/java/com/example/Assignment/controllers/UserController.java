package com.example.Assignment.controllers;

import com.example.Assignment.models.User;
import com.example.Assignment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

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
}
