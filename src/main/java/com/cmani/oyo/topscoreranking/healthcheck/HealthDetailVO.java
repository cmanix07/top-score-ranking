package com.cmani.oyo.topscoreranking.healthcheck;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Getter
@AllArgsConstructor
@Builder
public class HealthDetailVO {

    private long responseTimeMs;
    private  int httpStatusCode;
    private  String httpStatus;
    private String message;



}
