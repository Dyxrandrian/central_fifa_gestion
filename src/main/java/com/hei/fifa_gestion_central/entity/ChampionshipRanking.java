package com.hei.fifa_gestion_central.Entity;

import com.hei.fifa_gestion_central.enums.Championship;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Data
public class ChampionshipRanking {
    private Integer rank;
    private Championship championship;
    private Double differenceGoalsMedian;
}
