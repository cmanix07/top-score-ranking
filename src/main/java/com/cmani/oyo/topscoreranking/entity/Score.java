package com.cmani.oyo.topscoreranking.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;


public class Score {

 /*   @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "scoreId")
    public Integer scoreId;

    @Min(1)
    @Column(name = "playerScore")
    public Integer playerScore;

    @Column(name="scoreTime")
    public LocalDateTime scoreTime;

    @OneToMany(mappedBy = "player")
    private List<Player> player;*/

}
