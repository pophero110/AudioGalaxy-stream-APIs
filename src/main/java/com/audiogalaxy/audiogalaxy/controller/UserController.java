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
     * Authenticates a user with the provided login credentials and generates a JWT token upon successful authentication.
     *
     * Accepts a POST request with a JSON payload containing the user's login credentials (email and password). The method delegates
     * the authentication process to the UserService's loginUser method, passing the LoginRequest object.
     *
     * If the authentication is successful, the method returns a ResponseEntity with the generated JWT token and an HTTP status of OK (200).
     * The JWT token can be used for subsequent authorized requests.
     *
     * @param loginRequest The LoginRequest object containing the user's email and password.
     * @return ResponseEntity with the generated JWT token upon successful authentication.
     * @throws Exception if an error occurs during the authentication process.
     */
    @PostMapping(path="/users/login/")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) throws Exception {
        return ResponseEntity.ok(userService.loginUser(loginRequest));
    }

    /**
     * Deactivates the currently logged-in user.
     *
     * Sets the active status of the currently logged-in user to inactive. The user's account will be marked as inactive in the system.
     * The method delegates the deactivation process to the UserService's setUserToInactive method and returns the result as a ResponseEntity.
     *
     * @return ResponseEntity with HTTP status OK (200) if the user is successfully set to inactive.
     * @throws Exception if an error occurs during the deactivation process.
     */
    @PutMapping(path="/users/deactivate/")
    public ResponseEntity<?> setUserToInactive() throws Exception {
        return ResponseEntity.ok(userService.setUserToInactive());
    }

    @PutMapping(path="/users/profile/")
    public ResponseEntity<?> updateUserName(@RequestBody User userObject) throws Exception {
        return ResponseEntity.ok(userService.updateUsername(userObject));
    }
}