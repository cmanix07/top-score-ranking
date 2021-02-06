package com.cmani.oyo.topscoreranking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScoreDto implements Serializable {

    private int playerId;
    private String playerName;
    private int score;
    private LocalDateTime scoreTime;

}
