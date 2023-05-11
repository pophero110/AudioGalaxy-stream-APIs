package com.audiogalaxy.audiogalaxy.service;

import com.audiogalaxy.audiogalaxy.exception.InformationInvalidException;
import com.audiogalaxy.audiogalaxy.model.User;
import com.audiogalaxy.audiogalaxy.model.request.LoginRequest;
import com.audiogalaxy.audiogalaxy.model.response.LoginResponse;
import com.audiogalaxy.audiogalaxy.repository.UserRepository;
import com.audiogalaxy.audiogalaxy.security.JWTUtils;
import com.audiogalaxy.audiogalaxy.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

     private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private JWTUtils jwtUtils;
    private AuthenticationManager authenticationManager;
    private MyUserDetails myUserDetails;

    @Autowired
    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder,
                       JWTUtils jwtUtils, @Lazy AuthenticationManager authenticationManager, @Lazy MyUserDetails myUserDetails) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.myUserDetails = myUserDetails;
    }
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
        if(userObject.getPassword().length() < 5) {
            throw new InformationInvalidException("The password must contain 6 characters");
        }

        //It converts the password to a jwt token
        userObject.setPassword(passwordEncoder.encode(userObject.getPassword()));

        return userRepository.save(userObject);
    }

    public User findUserByEmailAddress(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Method handles login user authentication.
     * @param loginRequest Of type LoginRequest class/object
     * @return A LoginResponse object which contains a token.
     * @throws InformationInvalidException if user's email and password is not valid
     */
    public LoginResponse loginUser(LoginRequest loginRequest) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            myUserDetails = (MyUserDetails) authentication.getPrincipal();
            // generate token
            final String JWT = jwtUtils.generateJwtToken(myUserDetails);
            return new LoginResponse(JWT);
        } catch (Exception e) {
            throw new InformationInvalidException("Error:  user email or password is incorrect.");
        }
    }

    /**
     * Method handles setting a user's account to inactive.
     * @param loginRequest Logged in user
     * @return If user is found or not, if os inactive user's account
     * @throws Exception Return 404 or 200
     */
    public ResponseEntity<?> setUserToInactive(LoginRequest loginRequest) throws Exception {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(loginRequest.getEmail()));
        if (user.isPresent() && user.get().getActive()){
            user.get().setActive(false);
            userRepository.save(user.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
