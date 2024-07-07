package com.yolo.gamebk.test.exception;

import com.yolo.gamebk.test.dto.generic.GenericResponse;
import com.yolo.gamebk.test.util.AbstractResponseGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/5/24
 * Time: 10:19 PM
 */
@ControllerAdvice
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final AbstractResponseGenerator responseGenerator;
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        // Collect validation errors
        List<String> errorList = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorList.add("field "+error.getField()+" "+error.getDefaultMessage())
        );

        // Return a BAD_REQUEST response with the validation errors
        GenericResponse genericResponse = responseGenerator
                .constructFieldErrorResponse(errorList, "Request failed due to field validations failures", HttpStatus.BAD_REQUEST);
        return ResponseEntity.status(genericResponse.getHttpStatusCode()).body(genericResponse);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse> handleCommonException(Exception ex){
        GenericResponse genericResponse = responseGenerator
                .constructErrorExceptionResponse(
                        ex.getLocalizedMessage(),
                        "exception occurred while processing",
                        HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(genericResponse.getHttpStatusCode()).body(genericResponse);
    }
}
