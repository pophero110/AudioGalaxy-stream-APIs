package com.audiogalaxy.audiogalaxy.service;

import com.audiogalaxy.audiogalaxy.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    /**
     * This is the method that will create user, validates email, password and password length.
     * Checks to make sure the name, email & password can not be blank.
     * @param object contain require data.  Is used for creating user.
     * @return returns responseEntity status 200 & 400.
     */

    public ResponseEntity<?> createUser (User userObject){
        if(userObject.getName().isBlank()){
            return new ResponseEntity<>("The username can not be empty or contain spaces", HttpStatus.BAD_REQUEST);
        }
        if(userObject.getEmail().isBlank()){
            return new ResponseEntity<>("The email can not be empty or contain spaces", HttpStatus.BAD_REQUEST);
        }
        if(userObject.getPassword().isBlank()) {
            return new ResponseEntity<>("The password can not be empty or contain spaces", HttpStatus.BAD_REQUEST);
        }
        if(userObject.getPassword().length() > 5) {
            return new ResponseEntity<>("The password must contain 6 characters", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
