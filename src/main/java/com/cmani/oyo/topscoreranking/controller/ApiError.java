package com.cmani.oyo.topscoreranking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;

public class ApiError {
    public ApiError(HttpStatus badRequest, String s, String error, HttpMessageNotReadableException ex) {
    }
}
