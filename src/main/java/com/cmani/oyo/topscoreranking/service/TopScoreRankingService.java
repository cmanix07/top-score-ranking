package com.cmani.oyo.topscoreranking.service;

import com.cmani.oyo.topscoreranking.dto.PlayerDto;
import com.cmani.oyo.topscoreranking.entity.Player;
import com.cmani.oyo.topscoreranking.repository.TopScoreRankingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopScoreRankingService {
    @Autowired
    TopScoreRankingRepository topScoreRankingRepository;

    public void createPlayerScore(Player player) {
        topScoreRankingRepository.save(player);
    }
}
