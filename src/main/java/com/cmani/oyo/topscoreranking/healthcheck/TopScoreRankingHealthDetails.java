package com.cmani.oyo.topscoreranking.healthcheck;

import com.cmani.oyo.topscoreranking.dto.PlayerHistoryDto;
import com.cmani.oyo.topscoreranking.service.TopScoreRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.web.annotation.RestControllerEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
@RestControllerEndpoint(id= "tsr-health")
public class TopScoreRankingHealthDetails {

    private TopScoreRankingService topScoreRankingService;

    public TopScoreRankingHealthDetails(TopScoreRankingService topScoreRankingService) {
        this.topScoreRankingService = topScoreRankingService;
    }

    @ResponseBody
    @GetMapping("/short")
    public ResponseEntity<HealthDetailVO> getTopScoreRankingHealthInShort() {
        PlayerHistoryDto playerHistoryDto = topScoreRankingService.getPlayerScoreHistory("chinta");
        HealthDetailVO healthDetail = HealthDetailVO.builder()
                .responseTimeMs(100)
                .httpStatusCode(playerHistoryDto.getStatus())
                .httpStatus("OK")
                .message(playerHistoryDto.getMessage())
                .build();
        return ResponseEntity.ok().body(healthDetail);

    }

}
