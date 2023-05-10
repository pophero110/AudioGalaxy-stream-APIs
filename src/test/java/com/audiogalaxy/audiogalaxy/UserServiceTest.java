package com.audiogalaxy.audiogalaxy;

import com.audiogalaxy.audiogalaxy.model.User;
import com.audiogalaxy.audiogalaxy.repository.UserRepository;
import com.audiogalaxy.audiogalaxy.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    public void passwordMustBeConvertedToJwtTokenBeforeSave() {
        User user = new User("username", "pam@gmail.com", "123456");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        User createUser = userService.createUser(user);
        Assert.assertNotEquals(createUser.getPassword(), "123456");

    }
}
