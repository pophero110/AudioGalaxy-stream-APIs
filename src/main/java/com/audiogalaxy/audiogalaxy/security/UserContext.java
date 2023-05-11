package com.audiogalaxy.audiogalaxy.security;

import com.audiogalaxy.audiogalaxy.model.User;

public interface UserContext {
    User getCurrentLoggedInUser();
}
