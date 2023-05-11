package com.audiogalaxy.audiogalaxy;

import com.audiogalaxy.audiogalaxy.controller.UserController;
import com.audiogalaxy.audiogalaxy.exception.InformationInvalidException;
import com.audiogalaxy.audiogalaxy.model.User;
import com.audiogalaxy.audiogalaxy.model.request.LoginRequest;
import com.audiogalaxy.audiogalaxy.security.JWTUtils;
import com.audiogalaxy.audiogalaxy.security.MyUserDetails;
import com.audiogalaxy.audiogalaxy.security.MyUserDetailsService;
import com.audiogalaxy.audiogalaxy.security.SecurityConfiguration;
import com.audiogalaxy.audiogalaxy.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@Import(SecurityConfiguration.class)
@ExtendWith(MockitoExtension.class)
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

    private PasswordEncoder passwordEncoder;

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
        MockHttpServletRequestBuilder mockRequest = post(endpoint).
                contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)

                .content(mapper.writeValueAsString(new User("", "hello@email.com", "1,2,3")));

        mockMvc.perform(mockRequest).andExpect(status().isBadRequest()).andDo(print());
    }


    @Test
    @DisplayName("email can not be blank")
    public void emailCanNotBeBlank() throws Exception {
        when(userService.createUser(Mockito.any(User.class))).thenThrow(new InformationInvalidException("The email can not be empty or contain spaces"));
        MockHttpServletRequestBuilder mockRequest = post(endpoint).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(new User("Pam", "", "1,2,3")));

        mockMvc.perform(mockRequest).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    @DisplayName("password can not be blank")
    public void passwordCanNotBeBlank() throws Exception {
        when(userService.createUser(Mockito.any(User.class))).thenThrow(new InformationInvalidException("The password can not be empty or contain spaces"));
        MockHttpServletRequestBuilder mockRequest = post(endpoint).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(new User("Pam", "pam@gmail.com", "")));

        mockMvc.perform(mockRequest).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    @DisplayName("password must have at least 6 characters")
    public void passwordMustHaveAtLeast6Characters() throws Exception {
        when(userService.createUser(Mockito.any(User.class))).thenThrow(new InformationInvalidException("The password must contain 6 characters"));
        MockHttpServletRequestBuilder mockRequest = post(endpoint).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(new User("Pam", "pam@gmail.com", "12345")));

        mockMvc.perform(mockRequest).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    @DisplayName("only allow authenticated user to login endpoint valid")
    public void onlyAllowAuthenticatedUserToLogin() throws Exception {
        User loginRequest = new User("tim", "tim@hotmail.com", "tim123");
        String token = jwtUtils.generateJwtToken(new MyUserDetails(loginRequest));
        String body = "{name:" + loginRequest.getName() + ",password:" + loginRequest.getPassword() + "}";

        mockMvc.perform(post(endpoint + "/login/").content(body)
                        .content(this.mapper.writeValueAsString(loginRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andDo(print());
    }

}
