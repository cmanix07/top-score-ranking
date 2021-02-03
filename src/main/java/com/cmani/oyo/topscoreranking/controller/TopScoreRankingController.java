package com.cmani.oyo.topscoreranking.controller;

import com.cmani.oyo.topscoreranking.dto.PlayerDto;
import com.cmani.oyo.topscoreranking.entity.Player;
import com.cmani.oyo.topscoreranking.service.TopScoreRankingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/score-ranking/api/v1")
public class TopScoreRankingController {

    @Autowired
    private TopScoreRankingService topScoreRankingService;

    public TopScoreRankingController(){

    };


    @PostMapping("/player")
    public ResponseEntity createOrUpdatePlayerScore(@RequestBody Player player){

        topScoreRankingService.createPlayerScore(player);
        return ResponseEntity.ok().build();
    }

/*
    @GetMapping("/playeer/{playerId}")
    public ResponseEntity<ScoreDto> getPlayerScore(@PathVariable("playerId") @Validated int playerId){
        ScoreDto scoreDto = new ScoreDto();
        return ResponseEntity.ok(scoreDto);
    }

    @DeleteMapping("/player/{playerId}")
    public ResponseEntity deletePlayerScore(@PathVariable("playerId") int playerId ){

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/player-history/{playerId}")
    public ResponseEntity<PlayerHistory> getPlayerScoreHistory(@PathVariable("playerId") @Validated int playerId){

        return null;
    }

    @GetMapping("/player-history/{playerId}")
    public ResponseEntity<List<PlayerHistory>> getPlayerScoreList(List<Player> players, LocalDateTime beforeTime, LocalDateTime afterTime){

        return null;
    }*/
}
