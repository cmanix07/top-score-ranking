package com.cmani.oyo.topscoreranking.service;

import com.cmani.oyo.topscoreranking.dto.PlayerDto;
import com.cmani.oyo.topscoreranking.dto.PlayerHistoryDto;
import com.cmani.oyo.topscoreranking.dto.ScoreDto;
import com.cmani.oyo.topscoreranking.entity.Player;
import com.cmani.oyo.topscoreranking.repository.TopScoreRankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TopScoreRankingService {


    TopScoreRankingRepository topScoreRankingRepository;

    public TopScoreRankingService(TopScoreRankingRepository topScoreRankingRepository) {
        this.topScoreRankingRepository = topScoreRankingRepository;
    }

    public void createPlayerScore(Player player) {
        topScoreRankingRepository.save(player);
    }

    public ScoreDto getPlayerScoreById(int playerId) {
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

    public void deletePlayerScoreById(int playerId) {
        topScoreRankingRepository.deleteById(playerId);
    }

    public PlayerHistoryDto getPlayerScoreHistory(String players) {
        List<Player> playerList = topScoreRankingRepository.findAllByPlayerName(players);
        return buildPlayerHistoryDto(new PageImpl<Player>(playerList));

    }

    public PlayerHistoryDto getPlayerScoreList(List<String> players, Date beforeTime, Date afterTime, Pageable page) {
        List<Player> playerList = topScoreRankingRepository.findAll((root,criteriaQuery,criteriaBuilder)->{
            Predicate p = criteriaBuilder.conjunction();
            if (!ObjectUtils.isEmpty(beforeTime)){
                p = criteriaBuilder.and(p,criteriaBuilder.greaterThanOrEqualTo(root.get("scoreTime"),new Timestamp(beforeTime.getTime()).toLocalDateTime()));
            }
            if(!ObjectUtils.isEmpty(afterTime)){
                p = criteriaBuilder.and(p,criteriaBuilder.greaterThanOrEqualTo(root.get("scoreTime"),new Timestamp(afterTime.getTime()).toLocalDateTime()));
            }
            if (!ObjectUtils.isEmpty(players)){
                p = criteriaBuilder.and(p,root.get("playerName").in(players));
            }
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("scoreTime")));
            return p;
        },page).stream().collect(Collectors.toList());

//new PageImpl<PlayerHistoryDto>((List.of(buildPlayerHistoryDto(playerList))));
        return buildPlayerHistoryDto(new PageImpl<Player>(playerList));

    }

    private PlayerHistoryDto buildPlayerHistoryDto(PageImpl<Player> playerList) {

        PlayerHistoryDto playerHistoryDto = PlayerHistoryDto.builder().build();
        playerHistoryDto.setStatus(HttpStatus.OK.value());
        playerHistoryDto.setMessage("Success");
        playerHistoryDto.setTotalPages(playerList.getTotalPages());
        playerHistoryDto.setPageNumber(playerList.getNumber());
        playerHistoryDto.setTotalElements(playerList.getNumberOfElements());

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
