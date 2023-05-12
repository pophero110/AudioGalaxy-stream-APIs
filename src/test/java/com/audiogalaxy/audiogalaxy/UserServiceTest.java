package com.audiogalaxy.audiogalaxy;

import com.audiogalaxy.audiogalaxy.exception.InformationNotFoundException;
import com.audiogalaxy.audiogalaxy.model.User;
import com.audiogalaxy.audiogalaxy.model.request.LoginRequest;
import com.audiogalaxy.audiogalaxy.repository.UserRepository;
import com.audiogalaxy.audiogalaxy.security.MyUserDetails;
import com.audiogalaxy.audiogalaxy.security.UserContext;
import com.audiogalaxy.audiogalaxy.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @MockBean
    LoginRequest loginRequest;

    @MockBean
    UserContext userContext;

    @MockBean
    MyUserDetails myUserDetails;

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
    @DisplayName("login user unsuccessfully when user account is inactive")
    public void userAccountMustBeActiveToLogin() {
        User inActiveUser = new User("tim", "tim@hotmail.com", "tim123");
        inActiveUser.setActive(false);
        LoginRequest loginRequest = new LoginRequest("tim@hotmail.com", "tim123");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(inActiveUser));

        Assertions.assertThrows(InformationNotFoundException.class, () -> {
            userService.loginUser(loginRequest);
        });
    }

    @Test
    @DisplayName("Deactivate user account when user account is active")
    public void testDeactivateUserAccountSuccessfully() {
        User activeUser = new User("tim", "tim@hotmail.com", "tim123");
        activeUser.setActive(true);
        when(userContext.getCurrentLoggedInUser()).thenReturn(activeUser);

        ResponseEntity response = userService.setUserToInactive();
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertFalse(activeUser.getActive());
    }

    @Test
    @DisplayName("Deactivate user account throws an exception when user account is inactive")
    public void testDeactivateUserAccountUnsuccessfully() {
        User activeUser = new User("tim", "tim@hotmail.com", "tim123");
        activeUser.setActive(false);
        when(userContext.getCurrentLoggedInUser()).thenReturn(activeUser);

        Assertions.assertThrows(InformationNotFoundException.class, () -> userService.setUserToInactive());
    }

    @Test
    @DisplayName("ensure user is the correct user to update username and not blank")
    public void updateUserName() {
        User activeUser = new User("tim", "tim@hotmail.com", "tim123");
        when(userContext.getCurrentLoggedInUser()).thenReturn(activeUser);

        Assertions.assertTrue(activeUser.getId() == userContext.getCurrentLoggedInUser().getId());
        Assertions.assertEquals(activeUser.getName(), "tim");

    }

}
