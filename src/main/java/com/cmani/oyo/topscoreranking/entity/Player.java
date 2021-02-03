package com.cmani.oyo.topscoreranking.entity;

import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "PLAYER")
public class Player implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "playerId")
    public Integer playerId;

  //  @Pattern(regexp = "^([a-zA-Z]+/s/)*([a-zA-Z])$", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Player name must contain only letters or space")
    @Column(name = "playerName")
    public String playerName;

    @Min(1)
    @Column(name = "playerScore")
    public Integer playerScore;

    @Column(name="scoreTime")
    public LocalDateTime scoreTime;

}
