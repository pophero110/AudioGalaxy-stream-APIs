package com.audiogalaxy.audiogalaxy;

import com.audiogalaxy.audiogalaxy.controller.UserController;
import com.audiogalaxy.audiogalaxy.exception.InformationInvalidException;
import com.audiogalaxy.audiogalaxy.exception.InformationNotFoundException;
import com.audiogalaxy.audiogalaxy.model.User;
import com.audiogalaxy.audiogalaxy.model.request.LoginRequest;
import com.audiogalaxy.audiogalaxy.model.response.LoginResponse;
import com.audiogalaxy.audiogalaxy.security.*;
import com.audiogalaxy.audiogalaxy.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(TestSecurityConfiguration.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @MockBean
    private MyUserDetailsService myUserDetailsService;

    @MockBean
    private JWTUtils jwtUtils;

   @Autowired
    private JwtRequestFilter jwtRequestFilter;


    private final String endpoint = "/api/users/";

    User user_1 = new User("Pam", "pam@gmail.com", "123456");



    @Test
    @DisplayName("return 200 and User Object")
    public void createUserSuccessfully() throws Exception {
        when(userService.createUser(Mockito.any(User.class))).thenReturn(user_1);
        MockHttpServletRequestBuilder mockRequest = post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user_1));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name").value(user_1.getName()))
                .andExpect(jsonPath("$.email").value(user_1.getEmail()))
                .andDo(print());
    }

    @Test
    @DisplayName("request body should not be empty")
    public void requestBodyShouldNotBeEmpty() throws Exception {
        MockHttpServletRequestBuilder mockRequest = post(endpoint);
        mockMvc.perform(mockRequest).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    @DisplayName("user name can not be blank")
    public void userNameCanNotBeBlank() throws Exception {
        when(userService.createUser(Mockito.any(User.class))).thenThrow(new InformationInvalidException("The username can not be empty or contain spaces"));
        MockHttpServletRequestBuilder mockRequest = post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new User("", "hello@email.com", "1,2,3")));

        mockMvc.perform(mockRequest).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    @DisplayName("email can not be blank")
    public void emailCanNotBeBlank() throws Exception {
        when(userService.createUser(Mockito.any(User.class))).thenThrow(new InformationInvalidException("The email can not be empty or contain spaces"));
        MockHttpServletRequestBuilder mockRequest = post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new User("Pam", "", "1,2,3")));

        mockMvc.perform(mockRequest).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    @DisplayName("password can not be blank")
    public void passwordCanNotBeBlank() throws Exception {
        when(userService.createUser(Mockito.any(User.class))).thenThrow(new InformationInvalidException("The password can not be empty or contain spaces"));
        MockHttpServletRequestBuilder mockRequest = post(endpoint).
                contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new User("Pam", "pam@gmail.com", "")));

        mockMvc.perform(mockRequest).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    @DisplayName("password must have at least 6 characters")
    public void passwordMustHaveAtLeast6Characters() throws Exception {
        when(userService.createUser(Mockito.any(User.class))).thenThrow(new InformationInvalidException("The password must contain 6 characters"));
        MockHttpServletRequestBuilder mockRequest = post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new User("Pam", "pam@gmail.com", "12345")));

        mockMvc.perform(mockRequest).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    @DisplayName("when login successfully should return 200 and a token")
    public void shouldLoginSuccessfully() throws Exception {
        User loginRequest = new User("tim", "tim@hotmail.com", "tim123");
        when(userService.loginUser(Mockito.any(LoginRequest.class))).thenReturn(new LoginResponse("token"));

        MockHttpServletRequestBuilder mockRequest = post(endpoint + "login/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.message").value("token"))
                .andDo(print());
    }

    @Test
    @DisplayName("when user login unsuccessfully should return 400")
    public void shouldLoginUnSuccessfully() throws Exception {
        User loginRequest = new User("tim", "tim@hotmail.com", "tim123");
        when(userService.loginUser(Mockito.any(LoginRequest.class))).thenThrow(new InformationInvalidException("User not valid"));

        MockHttpServletRequestBuilder mockRequest = post(endpoint + "login/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("should login unsuccessfully when user account is inactive")
    public void shouldLoginUnSuccessfullyWhenUserIsInactive() throws Exception {
        User loginRequest = new User("tim", "tim@hotmail.com", "tim123");
        when(userService.loginUser(Mockito.any(LoginRequest.class))).thenThrow(new InformationNotFoundException("The user account is inactive"));

        MockHttpServletRequestBuilder mockRequest = post(endpoint + "login/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("when user account is active should return 200")
    public void deactivateUserSuccessfully() throws Exception {
        when(userService.setUserToInactive()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        MockHttpServletRequestBuilder mockRequest = put(endpoint + "deactivate/");

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("when user account is inactive should return 404")
    public void deactivateUserUnSuccessfully() throws Exception {
        when(userService.setUserToInactive()).thenThrow(new InformationNotFoundException("The user account is inactive"));

        MockHttpServletRequestBuilder mockRequest = put(endpoint + "deactivate/");

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("when logged in user updates username with a blank")
    public void updateUserNameNotBlank() throws Exception {
        User loggedInUser = new User("", "tim@hotmail.com", "tim123");
        when(userService.updateUsername(Mockito.any(User.class))).thenThrow(new InformationInvalidException("Username cannot be blank"));

        MockHttpServletRequestBuilder mockRequest = put(endpoint + "profile/")
        .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loggedInUser));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("when logged in user updates username with a valid name")
    public void updateUserNameSuccess() throws Exception {
        User loggedInUser = new User("tim2", "tim@hotmail.com", "tim123");
        when(userService.updateUsername(Mockito.any(User.class))).thenReturn(loggedInUser);

        MockHttpServletRequestBuilder mockRequest = put(endpoint + "profile/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loggedInUser));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect((jsonPath("$", notNullValue())))
                .andExpect((jsonPath("$.name")).value(loggedInUser.getName()))
                .andDo(print());
    }
}
