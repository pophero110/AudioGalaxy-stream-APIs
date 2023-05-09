package com.audiogalaxy.audiogalaxy.service;

import com.audiogalaxy.audiogalaxy.exception.InformationInvalidException;
import com.audiogalaxy.audiogalaxy.model.User;
import com.audiogalaxy.audiogalaxy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    /**
     * This is the method that will create user, validates email, password and password length.
     * Checks to make sure the name, email & password can not be blank.
     * @param userObject contain require data.  Is used for creating user.
     * @return returns responseEntity status 200 & 400.
     */

    public User createUser (User userObject){
        if(userObject.getName().isBlank()){
            throw new InformationInvalidException("The username can not be empty or contain spaces");
        }
        if(userObject.getEmail().isBlank()){
            throw new InformationInvalidException("The email can not be empty or contain spaces");
        }
        if(userObject.getPassword().isBlank()) {
            throw new InformationInvalidException("The password can not be empty or contain spaces");
        }
        if(userObject.getPassword().length() > 5) {
            throw new InformationInvalidException("The password must contain 6 characters");
        }
        return userRepository.save(userObject);
    }

}
