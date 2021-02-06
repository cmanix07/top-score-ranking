package com.cmani.oyo.topscoreranking.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@Table(name = "PLAYER")
@AllArgsConstructor
@NoArgsConstructor
public class Player implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "playerId")
    public Integer playerId;

    @Column(name = "playerName")
    public String playerName;

    @Min(1)
    @Column(name = "playerScore")
    public Integer playerScore;

    @Column(name="scoreTime")
    public LocalDateTime scoreTime;

  //  @ManyToOne
  //  private Score scores;

}
