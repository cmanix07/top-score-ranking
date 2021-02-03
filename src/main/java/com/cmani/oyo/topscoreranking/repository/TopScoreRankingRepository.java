package com.cmani.oyo.topscoreranking.repository;

import com.cmani.oyo.topscoreranking.entity.Player;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopScoreRankingRepository extends PagingAndSortingRepository<Player, Integer> {


}
