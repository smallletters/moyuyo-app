package com.moyuyo.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyuyo.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class CommunityControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listPosts_ShouldReturnPage() throws Exception {
        mockMvc.perform(get("/api/v1/community/posts")
                        .param("page", "1")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.records").isArray());
    }

    @Test
    void getPostDetail_WithInvalidId_ShouldReturnError() throws Exception {
        mockMvc.perform(get("/api/v1/community/posts/99999"))
                .andExpect(status().is4xxClientError());
    }
}
