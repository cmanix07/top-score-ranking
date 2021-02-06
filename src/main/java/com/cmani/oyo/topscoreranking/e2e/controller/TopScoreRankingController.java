package com.cmani.oyo.topscoreranking.e2e.controller;

import com.cmani.oyo.topscoreranking.dto.PlayerHistoryDto;
import com.cmani.oyo.topscoreranking.dto.ScoreDto;
import com.cmani.oyo.topscoreranking.entity.Player;
import com.cmani.oyo.topscoreranking.service.TopScoreRankingService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Api("score-ranking")
@RestController
@RequestMapping("/score-ranking/api/v1")
public class TopScoreRankingController {

    private static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";


    private TopScoreRankingService topScoreRankingService;

    public TopScoreRankingController(TopScoreRankingService topScoreRankingService){
        this.topScoreRankingService = topScoreRankingService;
    };


    @PostMapping("/score")
    @ResponseBody
    public ResponseEntity createOrUpdatePlayerScore(@RequestBody Player player){

        topScoreRankingService.createPlayerScore(player);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/score/{playerId}")
    public ResponseEntity<ScoreDto> getPlayerScoreById(@PathVariable("playerId") @Validated int playerId){
        ScoreDto scoreDto = topScoreRankingService.getPlayerScoreById(playerId);
        return ResponseEntity.ok(scoreDto);
    }

    @DeleteMapping("/score/{playerId}")
    public ResponseEntity deletePlayerScoreById(@PathVariable("playerId") int playerId ){
         topScoreRankingService.deletePlayerScoreById(playerId);
         return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/player-history/{playerName}")
    public ResponseEntity<PlayerHistoryDto> getPlayerScoreHistory(@PathVariable("playerName") String players){
        PlayerHistoryDto playerScoreHistory = topScoreRankingService.getPlayerScoreHistory(players);
        return ResponseEntity.ok().body(playerScoreHistory);
    }

   @GetMapping("/player-history")
    public ResponseEntity<PlayerHistoryDto> getPlayerScoreList(@RequestParam(required = false) List<String> players,
                                                                     @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                     @RequestParam(required = false) Date beforeTime,
                                                                     @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                     @RequestParam(required = false) Date afterTime,
                                                                     Pageable page){
      PlayerHistoryDto playerScoreHistoryList = topScoreRankingService.getPlayerScoreList(players,beforeTime,afterTime,page);
        return ResponseEntity.ok().body(playerScoreHistoryList);
    }

}
