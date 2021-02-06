package com.cmani.oyo.topscoreranking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerDto  implements Serializable {
    private int playerId;
    private String name;

}
