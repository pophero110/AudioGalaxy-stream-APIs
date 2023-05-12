package com.audiogalaxy.audiogalaxy.service;

import com.audiogalaxy.audiogalaxy.exception.InformationInvalidException;
import com.audiogalaxy.audiogalaxy.exception.InformationNotFoundException;
import com.audiogalaxy.audiogalaxy.model.User;
import com.audiogalaxy.audiogalaxy.model.request.LoginRequest;
import com.audiogalaxy.audiogalaxy.model.response.LoginResponse;
import com.audiogalaxy.audiogalaxy.repository.UserRepository;
import com.audiogalaxy.audiogalaxy.security.JWTUtils;
import com.audiogalaxy.audiogalaxy.security.MyUserDetails;
import com.audiogalaxy.audiogalaxy.security.UserContext;
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
    private final JWTUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private MyUserDetails myUserDetails;

    private UserContext userContext;

    @Autowired
    public UserService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder,
                       JWTUtils jwtUtils, @Lazy AuthenticationManager authenticationManager, @Lazy MyUserDetails myUserDetails, UserContext userContext) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.myUserDetails = myUserDetails;
        this.userContext = userContext;
    }

    /**
     * This is the method that will create user, validates email, password and password length.
     * Checks to make sure the name, email & password can not be blank.
     *
     * @param userObject contain require data.  Is used for creating user.
     * @return returns responseEntity status 200 & 400.
     */
    public User createUser(User userObject) {
        if (userObject.getName().isBlank()) {
            throw new InformationInvalidException("The username can not be empty or contain spaces");
        }
        if (userObject.getEmail().isBlank()) {
            throw new InformationInvalidException("The email can not be empty or contain spaces");
        }
        if (userObject.getPassword().isBlank()) {
            throw new InformationInvalidException("The password can not be empty or contain spaces");
        }
        if (userObject.getPassword().length() < 5) {
            throw new InformationInvalidException("The password must contain 6 characters");
        }

        //It converts the password to a jwt token
        userObject.setPassword(passwordEncoder.encode(userObject.getPassword()));

        return userRepository.save(userObject);
    }

    /**
     * Authenticates a user based on the provided login credentials and generates a JWT token upon successful authentication.
     *
     * The method first retrieves the user from the repository based on the email provided in the login request. If the user exists
     * and is inactive, it throws an InformationNotFoundException indicating that the user account is inactive.
     *
     * The method then attempts to authenticate the user by calling the authenticationManager's authenticate method with the provided
     * email and password. If authentication is successful, the user's authentication details are set in the SecurityContextHolder,
     * and a JWT token is generated using the jwtUtils.
     *
     * @param loginRequest The login request object containing the user's email and password.
     * @return A LoginResponse object containing the generated JWT token upon successful authentication.
     * @throws InformationNotFoundException If the user account is inactive.
     * @throws InformationInvalidException If the provided email or password is incorrect, or an error occurs during authentication.
     */
    public LoginResponse loginUser(LoginRequest loginRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
        if (user.isPresent() && !user.get().getActive()) {
            throw new InformationNotFoundException("The user account is inactive");
        }
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            myUserDetails = (MyUserDetails) authentication.getPrincipal();
            final String JWT = jwtUtils.generateJwtToken(myUserDetails);
            return new LoginResponse(JWT);
        } catch (Exception e) {
            throw new InformationInvalidException("Error:  user email or password is incorrect.");
        }
    }

    /**
     * Set the currently logged-in user to inactive status.
     *
     * If the user account is currently active, it will be set to inactive by updating the "active" field in the user object.
     * The updated user object is then saved to the user repository.
     *
     * @return ResponseEntity with HTTP status OK (200) if the user account is successfully set to inactive.
     * @throws InformationNotFoundException if the user account is already inactive.
     */
    public ResponseEntity<?> setUserToInactive() {
        User currentlyLoggedInUser = userContext.getCurrentLoggedInUser();
        if (currentlyLoggedInUser.getActive()) {
            currentlyLoggedInUser.setActive(false);
            userRepository.save(currentlyLoggedInUser);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new InformationNotFoundException("The user account is inactive");
        }
    }

    /**
     * Method updates username from current name to new name
     * @param userObject User data which contain updated name
     * @return Update User object when successful or error message
     */
    public User updateUsername(User userObject) {
        // get name user to update
        String updatedName = userObject.getName();
        if (!updatedName.isBlank()) {
            User currentlyLoggedInUser = userContext.getCurrentLoggedInUser();
            currentlyLoggedInUser.setName(updatedName);
            userRepository.save(currentlyLoggedInUser);
            return currentlyLoggedInUser;
        }
        throw new InformationInvalidException("User name cannot be blank.");
    }

}
