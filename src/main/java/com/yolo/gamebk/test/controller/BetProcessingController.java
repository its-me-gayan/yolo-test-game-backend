package com.yolo.gamebk.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yolo.gamebk.test.dto.generic.GenericResponse;
import com.yolo.gamebk.test.dto.BetRequest;
import com.yolo.gamebk.test.dto.BetResponse;
import com.yolo.gamebk.test.service.BetProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/5/24
 * Time: 10:47 AM
 */

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
public class BetProcessingController {

    private final BetProcessingService betProcessingService;
    private final ObjectMapper objectMapper;

    /**
     * Handles the POST request for placing a bet.
     *
     * @param betRequest the request body containing bet details
     * @return ResponseEntity containing the response with bet result details
     * @throws Exception if any error occurs during bet processing
     */
    @PostMapping("/game/bet")
    public ResponseEntity<GenericResponse> captureBet(@RequestBody @Valid BetRequest betRequest) throws Exception {
        log.info("Bet Captured and started processing");
        log.debug("Incoming request - {}" , objectMapper.writeValueAsString(betRequest));
        GenericResponse betResponseGenericResponse = betProcessingService.processBet(betRequest);
        log.info("Bet Proceed and response send");
        return ResponseEntity.status(betResponseGenericResponse.getHttpStatusCode()).body(betResponseGenericResponse);
    }
}
