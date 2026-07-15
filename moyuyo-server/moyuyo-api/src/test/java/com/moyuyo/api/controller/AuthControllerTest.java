package com.moyuyo.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyuyo.BaseIntegrationTest;
import com.moyuyo.common.dto.auth.LoginRequest;
import com.moyuyo.common.dto.auth.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class AuthControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_ShouldReturnSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@moyuyo.com");
        request.setPassword("Password123!");
        request.setNickname("TestUser");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.email").value("test@moyuyo.com"));
    }

    @Test
    void register_WithExistingEmail_ShouldFail() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("duplicate@moyuyo.com");
        request.setPassword("Password123!");
        request.setNickname("User1");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_WithValidCredentials_ShouldReturnToken() throws Exception {
        RegisterRequest reg = new RegisterRequest();
        reg.setEmail("login-test@moyuyo.com");
        reg.setPassword("Password123!");
        reg.setNickname("LoginUser");
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reg)));

        LoginRequest login = new LoginRequest();
        login.setEmail("login-test@moyuyo.com");
        login.setPassword("Password123!");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty());
    }

    @Test
    void login_WithWrongPassword_ShouldFail() throws Exception {
        LoginRequest login = new LoginRequest();
        login.setEmail("nonexistent@moyuyo.com");
        login.setPassword("WrongPass1!");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isBadRequest());
    }
}
