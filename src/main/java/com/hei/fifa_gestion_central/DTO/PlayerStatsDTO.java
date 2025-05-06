package com.hei.fifa_gestion_central.DTO;

import com.hei.fifa_gestion_central.Entity.PlayingTime;
import lombok.Data;

@Data
public class PlayerStatsDTO {
    private int scoredGoals;
    private PlayingTime playingTime;
}

