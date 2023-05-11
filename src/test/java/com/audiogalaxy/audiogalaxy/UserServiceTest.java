package com.audiogalaxy.audiogalaxy;

import com.audiogalaxy.audiogalaxy.model.User;
import com.audiogalaxy.audiogalaxy.repository.UserRepository;
import com.audiogalaxy.audiogalaxy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    @DisplayName("Password must be converted to jwt token before saving")
    public void passwordMustBeConvertedToJwtTokenBeforeSave() {
        User user = new User("username", "pam@gmail.com", "123456");
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        User createUser = userService.createUser(user);
        Assertions.assertNotEquals(createUser.getPassword(), "123456");
        verify(userRepository, times(1)).save(Mockito.any(User.class));

    }

    @Test
    public void findUserByEmailAddress() {
        User user = new User("username", "pam@gmail.com", "123456");
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        User foundUser = userService.findUserByEmailAddress(user.getEmail());
        Assertions.assertNotNull(foundUser);
        verify(userRepository, times(1)).findByEmail(anyString());

    }
}
