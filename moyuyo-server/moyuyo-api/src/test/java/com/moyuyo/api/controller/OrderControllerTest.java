package com.moyuyo.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyuyo.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class OrderControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listOrders_WithoutAuth_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(401));
    }

    @Test
    void createOrder_WithoutAuth_ShouldReturn401() throws Exception {
        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.code").value(401));
    }
}
