package com.audiogalaxy.audiogalaxy.controller;

import com.audiogalaxy.audiogalaxy.model.User;
import com.audiogalaxy.audiogalaxy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/auth/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping(path="/hello/")
    public String helloTeam() {
        return "Hello Team!";
    }


    /**
     * POSt /auth/users/register/
     * Calls method to create a registered user
     * @param user
     * @return
     */
    @PostMapping(path="/register/")
    public User createUser(User user) {
        return userRepository.save(user);
    }
}
