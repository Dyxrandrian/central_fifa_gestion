package com.hei.fifa_gestion_central.DAO;

import com.hei.fifa_gestion_central.enums.Championship;

import java.util.List;
import java.util.Map;

public interface ChampionshipStatsDAO {
    Map<Championship, List<Integer>> getDifferenceGoalsByChampionship();
}
