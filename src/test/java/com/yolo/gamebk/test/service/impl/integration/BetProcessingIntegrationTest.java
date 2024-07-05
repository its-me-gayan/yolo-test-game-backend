package com.yolo.gamebk.test.service.impl.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yolo.gamebk.test.dto.BetRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/5/24
 * Time: 1:49 PM
 */
@DisplayName("Bet Processing Integration Tests")
@AutoConfigureMockMvc
@SpringBootTest
public class BetProcessingIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void placeBetsTest_Status200() throws Exception {
        BetRequest betRequest = BetRequest
                .builder()
                .selectedNumber(50)
                .bet(40.5).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/game/bet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(betRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseCode").value("000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.TRUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseMessage").value("Data proceed successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.winAmount").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.generatedNumber").exists());

    }
}