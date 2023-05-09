package com.audiogalaxy.audiogalaxy;

import com.audiogalaxy.audiogalaxy.controller.UserController;
import com.audiogalaxy.audiogalaxy.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Random;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @Test
    public void shouldReturn200() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/users");

            mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    public void requestBodyShouldNotBeEmpty() throws Exception {

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/users");

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andDo(print());
    }



    @Test
    public void requestBodyUserNameCanNotBeBlank() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new User("", "hello@email.com", "1,2,3")));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$").value("The username can not be empty or contain spaces"))
                .andDo(print());
    }


    @Test
    public void requestBodyEmailCanNotBeBlank() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new User("Pam", "", "1,2,3")));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$").value("The email can not be empty or contain spaces"))
                .andDo(print());
    }

    @Test
    public void requestBodyPasswordCanNotBeBlank() throws Exception {
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new User("Pam", "pam@gmail.com", "")));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$").value("The password can not be empty or contain spaces"))
                .andDo(print());
    }





}
