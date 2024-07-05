package com.yolo.gamebk.test.util;

import com.yolo.gamebk.test.dto.generic.GenericResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/5/24
 * Time: 11:24 AM
 */

/**
 * Generic class to construct success responses for various types of responses.
 */
@Log4j2
@Component
public class AbstractResponseGenerator {

    /**
     * Constructs a successful response with the given parameters.
     *
     * @param responseBody the body of the response
     * @param responseDescription the description of the response
     * @param status the HTTP status of the response
     * @return an AbstractResponse containing the constructed response
     */
    public GenericResponse constructSuccessResponse(Object responseBody , String responseDescription, HttpStatus status){
        log.info("constructing success response for bet");
       return GenericResponse.builder()
                .responseCode("000")
                .isSuccess(Boolean.TRUE)
                .responseMessage("Data proceed successfully")
                .responseDescription(responseDescription)
                .httpStatusCode(status.value())
                .timestamp(LocalDateTime.now())
                .data(responseBody).build();

    }

    public GenericResponse constructFieldErrorResponse(List<String> fieldErrors , String responseDescription, HttpStatus status){
        log.info("constructing success response for bet");
        return GenericResponse.builder()
                .responseCode("999")
                .isSuccess(Boolean.FALSE)
                .responseMessage("Request Failed")
                .responseDescription(responseDescription)
                .fieldError(fieldErrors)
                .httpStatusCode(status.value())
                .timestamp(LocalDateTime.now())
                .build();

    }
}
