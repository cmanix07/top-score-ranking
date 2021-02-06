package com.cmani.oyo.topscoreranking.controller;

import com.cmani.oyo.topscoreranking.TopScoreRankingApplication;
import com.cmani.oyo.topscoreranking.dto.PlayerHistoryDto;
import com.cmani.oyo.topscoreranking.dto.ScoreDto;
import com.cmani.oyo.topscoreranking.entity.Player;
import com.cmani.oyo.topscoreranking.repository.TopScoreRankingRepository;
import com.cmani.oyo.topscoreranking.service.TopScoreRankingService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@EnableAutoConfiguration
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {TopScoreRankingApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TopScoreRankingControllerE2ETest {

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
    public void testPlayerHistoryByDateRange_Success() throws Exception {
        final int TOP_SCORE = 95;
        final int LOW_SCORE = 95;
        final Double AVG_SCORE = 97.5;

        String baseUrl = BASE_URL + "/player-history?player={players}&beforeTime={beforeTime}&afterTime={afterTime}";
        List<String> players = List.of("chinta A");
       /* ResponseEntity<PlayerHistoryDto> response =  testRestTemplate.getForEntity(baseUrl, PlayerHistoryDto.class, players,SCORE_DATE_1,SCORE_DATE_2);
        assertThat(response.getBody().getPlayerScoreList().size()).isEqualTo(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTopScore()).isEqualTo(TOP_SCORE);
        assertThat(response.getBody().getLowScore()).isEqualTo(LOW_SCORE);
        assertThat(response.getBody().getTotalPages()).isEqualTo(1);*/

        MvcResult result =
                mockMvc.perform(get(baseUrl, players,SCORE_DATE_1,SCORE_DATE_2))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                        .andExpect(jsonPath("$.[*].[0].playerId").value(2))
                        .andExpect(jsonPath("$.[*].[0].playerName").value("chinta A"))
                        .andExpect(jsonPath("$.[*].[0].score").value(95))
                        .andReturn();
    }

    //Get List of player based on afterTime
    @Test
    public void testPlayerHistoryByDateAfter_Success() throws JSONException {
        final int TOP_SCORE = 100;
        final int LOW_SCORE = 95;
        final Double AVG_SCORE = (100.0+95.0)/2;

        //TODO need to fix the test cases using TestRestTemplate
        String baseUrl = BASE_URL + "/player-history?afterTime={afterTime}";
        List<String> players = List.of("chinta A");
       // ResponseEntity<ScoreDtoPageHelper> response =  testRestTemplate.getForEntity(baseUrl, ScoreDtoPageHelper.class, SCORE_DATE_1);
        //System.out.println(response.getBody());
       // response.getBody().getPlayerScoreList().stream().forEach(p-> System.out.println("playerName: "+p.getPlayerName()+" score: "+p.getScore()+" id: & time "+p.getPlayerId()+p.getScoreTime()));
        /*assertThat(response.getBody().getPlayerScoreList().size()).isEqualTo(2);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTopScore()).isEqualTo(TOP_SCORE);
        assertThat(response.getBody().getLowScore()).isEqualTo(LOW_SCORE);
        assertThat(response.getBody().getLowScore()).isEqualTo(LOW_SCORE);
        assertThat(response.getBody().getAverage()).isEqualTo(AVG_SCORE);
        assertThat(response.getBody().getTotalPages()).isEqualTo(1);*/


        List<JSONObject> list = new ArrayList<>();
        JSONObject resJson1 = new JSONObject();
        JSONObject resJson2 = new JSONObject();
        resJson1.put("playerId",2);
        resJson1.put("playerName","chinta A");
        resJson1.put("score",95);
        resJson1.put("scoreTime","2021-02-05T12:00:00");
        resJson2.put("playerId",1);
        resJson2.put("playerName","chinta A");
        resJson2.put("score",100);
        resJson2.put("scoreTime","2021-02-05T12:00:00");
        list.add(resJson1);
        list.add(resJson2);

        JSONObject resJson3 = new JSONObject();
        resJson3.put("content",list);

        try {
            MvcResult result =
                    mockMvc.perform(get(baseUrl, SCORE_DATE_1))
                            .andExpect(status().isOk())
                            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                            .andExpect(jsonPath("$.[*].[0].playerId").value(2))
                            .andExpect(jsonPath("$.[*].[0].playerName").value("chinta A"))
                            .andExpect(jsonPath("$.[*].[0].score").value(95))
                            .andExpect(jsonPath("$.[*].[1].playerId").value(1))
                            .andExpect(jsonPath("$.[*].[1].playerName").value("chinta A"))
                            .andExpect(jsonPath("$.[*].[1].score").value(100))

                   .andReturn();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //TODO
    // Get Player history By afterTime range : Success
    //Get Player history By beforeTime range:  Success
    // Get Player history By List of player Names: Success
    // Get Player history by name, afterTime, beforeTime: Success


    // Negative Scenario
   // Body = {"content":[{"playerId":2,"playerName":"chinta A","score":95,"scoreTime":"2021-02-05T12:00:00"},{"playerId":1,"playerName":"chinta A","score":100,"scoreTime":"2021-02-04T12:00:00"}],
    //    "pageable":{"sort":{"sorted":false,"unsorted":true,"empty":true},"offset":0,"pageNumber":0,"pageSize":20,"paged":true,"unpaged":false},"totalPages":1,"last":true,"totalElements":2,"numberOfElements":2,"number":0,"sort":{"sorted":false,"unsorted":true,"empty":true},"first":true,"size":20,"empty":false}
}
