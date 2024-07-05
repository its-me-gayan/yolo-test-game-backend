package com.yolo.gamebk.test.dto.generic;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/5/24
 * Time: 11:04 AM
 */

/**
 * A generic class representing a standard response structure.
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse {

    private LocalDateTime timestamp;
    private String responseMessage;
    private String responseDescription;
    private String responseCode;
    private Integer httpStatusCode;
    private boolean isSuccess;
    List<String> fieldError;
    private Object data;
}
