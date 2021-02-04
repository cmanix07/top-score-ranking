package com.cmani.oyo.topscoreranking.service;

import com.cmani.oyo.topscoreranking.dto.PlayerDto;
import com.cmani.oyo.topscoreranking.dto.PlayerHistoryDto;
import com.cmani.oyo.topscoreranking.dto.ScoreDto;
import com.cmani.oyo.topscoreranking.entity.Player;
import com.cmani.oyo.topscoreranking.repository.TopScoreRankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TopScoreRankingService {
    @Autowired
    TopScoreRankingRepository topScoreRankingRepository;

    public void createPlayerScore(Player player) {
        topScoreRankingRepository.save(player);
    }

    public ScoreDto getPlayerScore(int playerId) {
        Optional<Player> player = topScoreRankingRepository.findById(playerId);
        ScoreDto scoreDto = ScoreDto.builder().build();

        scoreDto = player.map(p -> {
          return  ScoreDto.builder()
                    .playerId(p.playerId)
                    .playerName(p.playerName)
                    .score(p.playerScore)
                    .scoreTime(p.scoreTime)
                    .build();
        }
        ).orElse(scoreDto);
        return scoreDto;
    }

    public void deletePlayerScore(int playerId) {
        topScoreRankingRepository.deleteById(playerId);
    }

    public PlayerHistoryDto getPlayerScoreHistory(String players) {
        List<Player> playerList = topScoreRankingRepository.findAllByPlayerName(players);

        PlayerHistoryDto playerHistoryDto = PlayerHistoryDto.builder().build();
        List<ScoreDto> scoreDtoList = playerList.stream().map(p->{
            return ScoreDto.builder()
                    .playerId(p.getPlayerId())
                    .playerName(p.getPlayerName())
                    .score(p.getPlayerScore())
                    .scoreTime(p.getScoreTime())
                    .build();

        }).collect(Collectors.toList());

        if(!playerList.isEmpty()){
            playerHistoryDto.setTopScore(playerList.stream().max(Comparator.comparing(Player::getPlayerScore)).get().getPlayerScore());
            playerHistoryDto.setLowScore(playerList.stream().min(Comparator.comparing(Player::getPlayerScore)).get().getPlayerScore());
            playerHistoryDto.setAverage(playerList.stream().mapToDouble(Player::getPlayerScore).average().getAsDouble());
        }
        playerHistoryDto.setPlayerScoreList(scoreDtoList);
       return playerHistoryDto;
    }
}
