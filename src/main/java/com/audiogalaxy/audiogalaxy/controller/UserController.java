package com.audiogalaxy.audiogalaxy.controller;

import com.audiogalaxy.audiogalaxy.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class UserController {

    @PostMapping(path = "/users")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if(user.getName().isBlank()){
            return new ResponseEntity<>("The username can not be empty or contain spaces", HttpStatus.BAD_REQUEST);
        }
        if(user.getEmail().isBlank()){
            return new ResponseEntity<>("The email can not be empty or contain spaces", HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(null, HttpStatus.OK);
    }




}
