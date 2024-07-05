package com.yolo.gamebk.test.service.impl;

import com.yolo.gamebk.test.dto.BetRequest;
import com.yolo.gamebk.test.dto.BetResponse;
import com.yolo.gamebk.test.dto.generic.GenericResponse;
import com.yolo.gamebk.test.service.BetProcessingService;
import com.yolo.gamebk.test.util.AbstractResponseGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/5/24
 * Time: 10:48 AM
 */
@Service
@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BetProcessingServiceImpl implements BetProcessingService {
    private final SecureRandom random = new SecureRandom();

    private final AbstractResponseGenerator responseGenerator;
    /**
     * Processes a bet request and returns a response.
     *
     * @param betRequest the request containing bet details
     * @return an AbstractResponse containing the result of the bet processing
     * @throws Exception if any error occurs during bet processing
     */
    @Override
    public GenericResponse processBet(BetRequest betRequest) throws Exception {
        log.info("bet service processing");
        int generatedNumber = random.nextInt(100) + 1;
        log.debug("System Generated number - {}", generatedNumber);
        double winAmount = 0;
        String responseDescription = "No Win calculated";
        if (betRequest.getSelectedNumber() > generatedNumber) {
            //if 100-getSelectedNumber become 0 , we are trying to divide 99.0/0 , this will give us infinite result
            if (Math.abs(100 - betRequest.getSelectedNumber()) < 1e-6) { // Check if the difference is close to zero.
                log.debug("selected number close to zero and ignoring with no win - {}", betRequest.getSelectedNumber());
                winAmount = 0.0;
            } else {
                log.info("winning found");
                winAmount = betRequest.getBet() * (99.0 / (100 - betRequest.getSelectedNumber()));
                log.debug("------ A winning found and processing -----");
                log.debug("------ Bet : {} -----", betRequest.getBet());
                log.debug("------ Received Chosen Number : {} -----", betRequest.getSelectedNumber());
                log.debug("------ System Generated Number : {} -----", generatedNumber);
                log.debug("------ Win : {} -----", winAmount);
                responseDescription = "Win calculated and included";
            }

        }
        log.info("started generating final response");
        BetResponse betResponse = new BetResponse();
        betResponse.setWinAmount(winAmount);
        betResponse.setGeneratedNumber(generatedNumber);
        return responseGenerator.constructSuccessResponse(betResponse, responseDescription, HttpStatus.OK);
    }
}
