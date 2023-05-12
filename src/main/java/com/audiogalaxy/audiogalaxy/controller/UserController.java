package com.audiogalaxy.audiogalaxy.controller;

import com.audiogalaxy.audiogalaxy.model.User;
import com.audiogalaxy.audiogalaxy.model.request.LoginRequest;
import com.audiogalaxy.audiogalaxy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
     *
     * @param user contain require data.  Is used for creating user.
     * @return returns responseEntity status 200 & 400.
     */
    @PostMapping(path = "/users/")
    public User registerUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    /**
     * Method calls loginUser method to validate user.
     * @param loginRequest
     * @return 200 and object which contains the token
     * @return 400
     * @throws Exception
     */
    @PostMapping(path="/users/login/")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) throws Exception {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    @PutMapping(path="/users/deactivate/")
    public ResponseEntity<?> setUserToInactive() throws Exception {
        return ResponseEntity.ok(userService.setUserToInactive());
    }


}