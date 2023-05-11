package com.audiogalaxy.audiogalaxy;

import com.audiogalaxy.audiogalaxy.controller.UserController;
import com.audiogalaxy.audiogalaxy.exception.InformationInvalidException;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(UserController.class)
@Import(SecurityConfiguration.class)
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
    @DisplayName("when user account is not active should return 404")
    public void shouldNotBeAbleToInactiveAcct() throws Exception {
        User loginRequest = new User("tim", "mm", "tim123");
        when(userService.setUserToInactive(Mockito.any(LoginRequest.class))).thenThrow(new InformationInvalidException("not found"));

        MockHttpServletRequestBuilder mockRequest = put(endpoint + "login/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest));

        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    @DisplayName("when user account is not active should return 200")
    public void shouldBeActiveUser() throws Exception {
        User loginRequest = new User("tim", "tim@hotmail.com", "tim123");
        loginRequest.setActive(false);
        when(userService.setUserToInactive(Mockito.any(LoginRequest.class))).thenThrow(new InformationInvalidException("you are not active"));

        MockHttpServletRequestBuilder mockRequest = put(endpoint + "login/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginRequest));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andDo(print());

    }


}
