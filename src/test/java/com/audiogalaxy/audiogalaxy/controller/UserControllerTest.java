package com.audiogalaxy.audiogalaxy.controller;

import com.audiogalaxy.audiogalaxy.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;





@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    private UserController userController;  // eliminate dependency of JPA

    // Create standard user objects
    User USER_1 = new User(1L, "Pam", "pam@hotmail.com", "pam", true);
    User USER_2 = new User(2L, "Jeff", "jeff@hotmail.com", "jeff", true);
    User USER_3 = new User(3L, "Wanda", "wanda@hotmail.com", "wanda", true);

    @Test
    public void shouldReturnHelloTeam() throws Exception {
        mockMvc.perform(get("/auth/users/hello/"))
                .andExpect(content().string("Hello Team!"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    /**
     *
     */
    @Test
    public void shouldCreateUser_success() throws Exception {
        // controller needs to create user/person
        when(userController.createUser(Mockito.any(User.class))).thenReturn(USER_1);

        // build mock request
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/auth/users/register/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(USER_1));  //serialization

        // make/perform mock request
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id").value(USER_1.getId()))
                .andExpect(jsonPath("$.username").value(USER_1.getUsername()))
                .andExpect(jsonPath("$.email").value(USER_1.getEmail()))
                .andDo(print());
    }



}