package com.hei.fifa_gestion_central.DAO;

import com.hei.fifa_gestion_central.enums.Championship;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

public interface ChampionshipStatsDAO {
    Map<Championship, List<Integer>> getDifferenceGoalsByChampionship();
}
