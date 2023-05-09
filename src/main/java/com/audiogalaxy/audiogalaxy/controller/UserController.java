package com.audiogalaxy.audiogalaxy.controller;

import com.audiogalaxy.audiogalaxy.model.User;
import com.audiogalaxy.audiogalaxy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(path = "/api")
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * This is the method that will create user, validates email, password and password length.
     * Checks to make sure the name, email & password can not be blank.
     * @param user contain require data.  Is used for creating user.
     * @return returns responseEntity status 200 & 400.
     */
    @PostMapping(path = "/users/")
    public User registerUser(@RequestBody User user) {
      return userService.createUser(user);
    }




}
