package com.cmani.oyo.topscoreranking.controller;

import com.cmani.oyo.topscoreranking.dto.ScoreDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public class ScoreDtoPageHelper  extends PageImpl<ScoreDto> {

    @JsonCreator
    // Note: I don't need a sort, so I'm not including one here.
    // It shouldn't be too hard to add it in tho.
    public ScoreDtoPageHelper(@JsonProperty("content") List<ScoreDto> content,
                      @JsonProperty("number") int number,
                      @JsonProperty("size") int size,
                      @JsonProperty("totalElements") Long totalElements) {
        super(content,PageRequest.of(number,size),totalElements);
    }
}

