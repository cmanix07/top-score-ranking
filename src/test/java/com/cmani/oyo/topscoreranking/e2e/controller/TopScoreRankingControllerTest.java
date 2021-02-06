package com.cmani.oyo.topscoreranking.e2e.controller;

import com.cmani.oyo.topscoreranking.TopScoreRankingApplication;
import com.cmani.oyo.topscoreranking.dto.PlayerHistoryDto;
import com.cmani.oyo.topscoreranking.dto.ScoreDto;
import com.cmani.oyo.topscoreranking.entity.Player;
import com.cmani.oyo.topscoreranking.repository.TopScoreRankingRepository;
import com.cmani.oyo.topscoreranking.service.TopScoreRankingService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@EnableAutoConfiguration
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TopScoreRankingApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TopScoreRankingControllerTest {

    @Autowired
    TopScoreRankingService topScoreRankingService;
    @Autowired
    TopScoreRankingRepository topScoreRankingRepository;
    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    MockMvc mockMvc;

    private static final String BASE_URL = "/score-ranking/api/v1";

    private static final int PLAYER_ID_1 = 1;
    private static final int PLAYER_ID_2 = 2;
    private static final int PLAYER_ID_3 = 3;
    private static final String PLAYER_NAME_1 ="chinta A";
    private static final String PLAYER_NAME_2 ="chinta A";
    private static final String PLAYER_NAME_3 ="chinta C";
    private static final int SCORE_1 = 100;
    private static final int SCORE_2 = 95;
    private static final int SCORE_3 = 75;
    private static final LocalDateTime SCORE_DATE_1 = LocalDateTime.of(2021,02,04,12,00);
    private static final LocalDateTime SCORE_DATE_2 = LocalDateTime.of(2021,02,05,12,00);

    private List<Player> playerList ;
    Player player1,player2, player3;



    @BeforeAll
    public void setup() {
        player1 = Player.builder()
                .playerId(PLAYER_ID_1)
                .playerName(PLAYER_NAME_1)
                .playerScore(SCORE_1)
                .scoreTime(SCORE_DATE_1)
                .build();
        player2 = Player.builder()
                .playerName(PLAYER_NAME_2)
                .playerScore(SCORE_2)
                .scoreTime(SCORE_DATE_2)
                .build();
        player3 = Player.builder()
                .playerId(PLAYER_ID_3)
                .playerName(PLAYER_NAME_3)
                .playerScore(SCORE_3)
                .scoreTime(null)
                .build();

        playerList = List.of(player1, player2, player3);
        topScoreRankingRepository.saveAll(playerList);
    }


    // Create a player Test : Success
    @Test
    public void testCreatePlayerScore_success() throws Exception {

        String baseUrl = BASE_URL + "/score";
        player3.setPlayerId(4);
        ResponseEntity response =  testRestTemplate.postForEntity(baseUrl,player3, JSONObject.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        topScoreRankingRepository.deleteById(player3.getPlayerId());
    }

    @Test
    public void testDeletePlayerById_Success(){
        int sizeBeforeDelete = topScoreRankingRepository.findAll().size();
        System.out.println("*******sizeBeforeDelete*******"+sizeBeforeDelete);
        assertThat(topScoreRankingRepository.findById(player3.getPlayerId()).isPresent()).isTrue();
        String baseUrl = BASE_URL + "/score/{playerId}";
        testRestTemplate.delete(baseUrl,player3.getPlayerId());
        Optional<Player> playerDto = topScoreRankingRepository.findById(player3.getPlayerId());
        assertThat(playerDto.isPresent()).isFalse();
        System.out.println("*******sizeAfterDelete*******"+topScoreRankingRepository.findAll().size());

    }

    // Get Player By Id : Success
    @Test
    public void testGetPlayerById_Success(){
        String baseUrl = BASE_URL + "/score/{playerId}";
        ResponseEntity<ScoreDto> response =  testRestTemplate.getForEntity(baseUrl, ScoreDto.class,player1.getPlayerId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getPlayerId()).isEqualTo(player1.getPlayerId());
        assertThat(response.getBody().getPlayerName()).isEqualTo(player1.getPlayerName());
        assertThat(response.getBody().getScore()).isEqualTo(player1.getPlayerScore());
        assertThat(response.getBody().getScoreTime()).isEqualTo(player1.getScoreTime());

    }

    // Get Player By name : Success
    @Test
    public void testPlayerByName_success(){
        final int TOP_SCORE = 100;
        final int LOW_SCORE = 95;
        final Double AVG_SCORE = 97.5;

        String baseUrl = BASE_URL + "/player-history/{playerName}";
        ResponseEntity<PlayerHistoryDto> response =  testRestTemplate.getForEntity(baseUrl, PlayerHistoryDto.class,player1.playerName);
        response.getBody().getPlayerScoreList().stream().forEach(p-> System.out.println("playerName: "+p.getPlayerName()+" score: "+p.getScore()+" id: & time "+p.getPlayerId()+p.getScoreTime()));
        assertThat(response.getBody().getPlayerScoreList().size()).isEqualTo(2);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTopScore()).isEqualTo(TOP_SCORE);
        assertThat(response.getBody().getLowScore()).isEqualTo(LOW_SCORE);


    }

    // Get Player history By date range with player name: Success
    @Test
    public void testPlayerHistoryByDateRange_Success(){
        final int TOP_SCORE = 95;
        final int LOW_SCORE = 95;
        final Double AVG_SCORE = 97.5;

        String baseUrl = BASE_URL + "/player-history?player={players}&beforeTime={beforeTime}&afterTime={afterTime}";
        List<String> players = List.of("chinta A");
        ResponseEntity<PlayerHistoryDto> response =  testRestTemplate.getForEntity(baseUrl, PlayerHistoryDto.class, players,SCORE_DATE_1,SCORE_DATE_2);
        response.getBody().getPlayerScoreList().stream().forEach(p-> System.out.println("playerName: "+p.getPlayerName()+" score: "+p.getScore()+" id: & time "+p.getPlayerId()+p.getScoreTime()));
        assertThat(response.getBody().getPlayerScoreList().size()).isEqualTo(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTopScore()).isEqualTo(TOP_SCORE);
        assertThat(response.getBody().getLowScore()).isEqualTo(LOW_SCORE);
        assertThat(response.getBody().getTotalPages()).isEqualTo(1);
    }

    //Get List of player based on afterTime
    @Test
    public void testPlayerHistoryByDateAfter_Success(){
        final int TOP_SCORE = 100;
        final int LOW_SCORE = 95;
        final Double AVG_SCORE = (100.0+95.0)/2;

        String baseUrl = BASE_URL + "/player-history?afterTime={afterTime}";
        List<String> players = List.of("chinta A");
        ResponseEntity<PlayerHistoryDto> response =  testRestTemplate.getForEntity(baseUrl, PlayerHistoryDto.class, SCORE_DATE_1);
        response.getBody().getPlayerScoreList().stream().forEach(p-> System.out.println("playerName: "+p.getPlayerName()+" score: "+p.getScore()+" id: & time "+p.getPlayerId()+p.getScoreTime()));
        assertThat(response.getBody().getPlayerScoreList().size()).isEqualTo(2);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTopScore()).isEqualTo(TOP_SCORE);
        assertThat(response.getBody().getLowScore()).isEqualTo(LOW_SCORE);
        assertThat(response.getBody().getLowScore()).isEqualTo(LOW_SCORE);
        assertThat(response.getBody().getAverage()).isEqualTo(AVG_SCORE);
        assertThat(response.getBody().getTotalPages()).isEqualTo(1);
    }

    //TODO
    // Get Player history By afterTime range : Success
    //Get Player history By beforeTime range:  Success
    // Get Player history By List of player Names: Success
    // Get Player history by name, afterTime, beforeTime: Success


    // Negative Scenario

}
