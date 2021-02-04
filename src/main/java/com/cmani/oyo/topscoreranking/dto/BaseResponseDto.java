package com.cmani.oyo.topscoreranking.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponseDto implements Serializable {

    private int status;
    private String message;
}
