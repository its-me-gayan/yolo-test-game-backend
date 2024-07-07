package com.yolo.gamebk.test.service.impl.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yolo.gamebk.test.dto.BetRequest;
import org.hamcrest.CoreMatchers;
import org.hamcrest.collection.IsCollectionWithSize;
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
 *
 *  This class contains integration tests for the Bet Processing functionality.
 */
@DisplayName("Bet Processing Integration Tests")
@AutoConfigureMockMvc
@SpringBootTest
public class BetProcessingIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    /**
     * Test to ensure that a bet can be placed successfully.
     * The test checks for a 200 OK status and validates the response content.
     */
    @Test
    void placeBets_Status200_Test() throws Exception {
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

    /**
     * Test to ensure validation fails when one field (selectedNumber) is invalid.
     * The test checks for a 400 Bad Request status and validates the error response.
     */
    @Test
    void placeBetsValidationFailed_One_Field_Status400_Test() throws Exception {
        BetRequest betRequest = BetRequest
                .builder()
                .selectedNumber(0)
                .bet(40.5).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/game/bet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(betRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseCode").value("001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.FALSE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseMessage").value("Validation Failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseDescription").value("Request failed due to field validations failures"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldError").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldError").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldError",IsCollectionWithSize.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldError", CoreMatchers.hasItem("field selectedNumber must be greater than or equal to 1")));

    }

    /**
     * Test to ensure validation fails when multiple fields (selectedNumber and bet) are invalid.
     * The test checks for a 400 Bad Request status and validates the error response.
     */
    @Test
    void placeBetsValidationFailed_Multiple_Field_Status400_Test() throws Exception {
        BetRequest betRequest = BetRequest
                .builder()
                .selectedNumber(0)
                .bet(0.0).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/game/bet")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(betRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseCode").value("001"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(Boolean.FALSE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseMessage").value("Validation Failed"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.responseDescription").value("Request failed due to field validations failures"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldError").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldError").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldError",IsCollectionWithSize.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldError", CoreMatchers.hasItem("field selectedNumber must be greater than or equal to 1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fieldError", CoreMatchers.hasItem("field bet must be greater than or equal to 1")));

    }
}