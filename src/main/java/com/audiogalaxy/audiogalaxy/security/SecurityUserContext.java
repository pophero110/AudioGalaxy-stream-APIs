package com.audiogalaxy.audiogalaxy.security;

import com.audiogalaxy.audiogalaxy.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUserContext implements UserContext {
    @Override
    public User getCurrentLoggedInUser() {
        MyUserDetails myUserDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return myUserDetails.getUser();
    }
}
