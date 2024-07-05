package com.yolo.gamebk.test.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Author: Gayan Sanjeewa
 * User: gayan
 * Date: 7/5/24
 * Time: 11:03 AM
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BetRequest {
    @NotNull
    @Min(1)
    @Max(100)
    private Integer selectedNumber;

    @NotNull
    @Min(1)
    private Double bet;
}
