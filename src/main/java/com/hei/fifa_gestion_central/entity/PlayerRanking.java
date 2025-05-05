package com.hei.fifa_gestion_central.Entity;

import com.hei.fifa_gestion_central.enums.Championship;
import com.hei.fifa_gestion_central.enums.PlayerPosition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class PlayerRanking {
    private Integer rank;
    private String id;
    private String name;
    private Integer number;
    private PlayerPosition position;
    private String nationality;
    private Integer age;
    private Championship championship;
    private Integer scoredGoals;
    private com.hei.fifa_gestion_central.entity.PlayingTime playingTime;
}
