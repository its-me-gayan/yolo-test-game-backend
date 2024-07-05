package com.yolo.gamebk.test.service;

import com.yolo.gamebk.test.dto.BetRequest;
import com.yolo.gamebk.test.dto.BetResponse;
import com.yolo.gamebk.test.dto.generic.GenericResponse;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/5/24
 * Time: 10:48 AM
 */

/**
 * Service interface for processing bets.
 */
public interface BetProcessingService {

    /**
     * Processes a bet request and returns a response.
     *
     * @param betRequest the request containing bet details
     * @return an AbstractResponse containing the result of the bet processing
     * @throws Exception if any error occurs during bet processing
     */
    GenericResponse processBet(BetRequest betRequest) throws Exception;
}
