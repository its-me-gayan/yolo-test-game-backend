package com.yolo.gamebk.test.service.impl;

import com.yolo.gamebk.test.dto.BetRequest;
import com.yolo.gamebk.test.dto.BetResponse;
import com.yolo.gamebk.test.dto.generic.GenericResponse;
import com.yolo.gamebk.test.service.BetProcessingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/5/24
 * Time: 11:44 AM
 */
@DisplayName("Bet Processing Service")
@SpringBootTest
class BetProcessingServiceImplTest {

    @Autowired
    private BetProcessingService betProcessingService;

    @Test
    void processBet_Success() throws Exception {
        double tolerance = 0.01;

        BetRequest betRequest = BetRequest
                .builder()
                .selectedNumber(100)
                .bet(50.5).build();

        GenericResponse betResponseGenericResponse = betProcessingService.processBet(betRequest);

        Assertions.assertTrue(betResponseGenericResponse.isSuccess());
        Assertions.assertEquals(betResponseGenericResponse.getHttpStatusCode() , HttpStatus.OK.value());
        Assertions.assertNotNull(betResponseGenericResponse.getData());

        BetResponse response = (BetResponse) betResponseGenericResponse.getData();

        if (betRequest.getSelectedNumber() > response.getGeneratedNumber()) {
            Assertions.assertEquals(expectedWin(betRequest) , response.getWinAmount() ,tolerance );
        } else {
            Assertions.assertEquals(0, response.getWinAmount());
        }
    }


    private double expectedWin(BetRequest betRequest){
        if (Math.abs(100 - betRequest.getSelectedNumber()) < 1e-6) {
            return 0.0;
        }
        return betRequest.getBet() * (99.0 / (100 - betRequest.getSelectedNumber()));
    }
}