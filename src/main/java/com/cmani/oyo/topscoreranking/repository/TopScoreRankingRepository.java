package com.cmani.oyo.topscoreranking.repository;

import com.cmani.oyo.topscoreranking.dto.PlayerHistoryDto;
import com.cmani.oyo.topscoreranking.entity.Player;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopScoreRankingRepository extends PagingAndSortingRepository<Player, Integer> {


    List<Player> findAllByPlayerName(@Param("playerName") String playerName);
}
