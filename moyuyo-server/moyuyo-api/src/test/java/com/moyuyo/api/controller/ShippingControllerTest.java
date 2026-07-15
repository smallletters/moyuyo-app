package com.moyuyo.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyuyo.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class ShippingControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getShippingRate_ShouldReturnRates() throws Exception {
        mockMvc.perform(get("/api/v1/shipping/rate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.carrier").isString());
    }

    @Test
    void estimateShipping_ShouldReturnList() throws Exception {
        mockMvc.perform(get("/api/v1/shipping/estimate")
                        .param("country", "US")
                        .param("weight", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isArray());
    }
}
