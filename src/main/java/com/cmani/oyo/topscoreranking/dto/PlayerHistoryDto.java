package com.cmani.oyo.topscoreranking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerHistoryDto implements Serializable {

    private int status;
    private String message;

    private Double average;
    private Integer topScore;
    private Integer lowScore;
    private List<ScoreDto> playerScoreList;

    private int totalPages;
    private int pageNumber;
    private int totalElements;


}
