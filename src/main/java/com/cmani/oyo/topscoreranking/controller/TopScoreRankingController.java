package com.cmani.oyo.topscoreranking.controller;

import com.cmani.oyo.topscoreranking.dto.PlayerHistoryDto;
import com.cmani.oyo.topscoreranking.dto.ScoreDto;
import com.cmani.oyo.topscoreranking.entity.Player;
import com.cmani.oyo.topscoreranking.service.TopScoreRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/score-ranking/api/v1")
public class TopScoreRankingController {

    private static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    @Autowired
    private TopScoreRankingService topScoreRankingService;

    public TopScoreRankingController(){

    };


    @PostMapping("/player")
    public ResponseEntity createOrUpdatePlayerScore(@RequestBody Player player){

        topScoreRankingService.createPlayerScore(player);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/player/{playerId}")
    public ResponseEntity<ScoreDto> getPlayerScore(@PathVariable("playerId") @Validated int playerId){
        ScoreDto scoreDto = topScoreRankingService.getPlayerScore(playerId);
        return ResponseEntity.ok(scoreDto);
    }

    @DeleteMapping("/player/{playerId}")
    public ResponseEntity deletePlayerScore(@PathVariable("playerId") int playerId ){
         topScoreRankingService.deletePlayerScore(playerId);
         return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/player-history/{playerName}")
    public ResponseEntity<PlayerHistoryDto> getPlayerScoreHistory(@PathVariable("playerName") String players){
        PlayerHistoryDto playerScoreHistory = topScoreRankingService.getPlayerScoreHistory(players);
        return ResponseEntity.ok().body(playerScoreHistory);
    }

   @PostMapping("/player-history")
    public ResponseEntity<PlayerHistoryDto> getPlayerScoreList(@RequestBody(required = false) List<String> players,
                                                                     @DateTimeFormat(pattern = DATE_FORMAT_YYYY_MM_DD)
                                                                     @RequestBody(required = false) LocalDateTime beforeTime,
                                                                     @DateTimeFormat(pattern = DATE_FORMAT_YYYY_MM_DD)
                                                                     @RequestBody(required = false) LocalDateTime afterTime,
                                                                                  Pageable page){
      PlayerHistoryDto playerScoreHistoryList = topScoreRankingService.getPlayerScoreList(players,beforeTime,afterTime,page);
        return ResponseEntity.ok().body(playerScoreHistoryList);
    }


}
