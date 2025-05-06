package com.hei.fifa_gestion_central.Service;

import com.hei.fifa_gestion_central.DAO.ChampionshipStatsDAO;
import com.hei.fifa_gestion_central.Entity.ChampionshipRanking;
import com.hei.fifa_gestion_central.Entity.ChampionshipStatsAnalyzer;
import com.hei.fifa_gestion_central.enums.Championship;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ChampionshipRankingService {
    private final ChampionshipStatsDAO statsDAO;
    private final ChampionshipStatsAnalyzer analyzer;

    public ChampionshipRankingService(ChampionshipStatsDAO statsDAO, ChampionshipStatsAnalyzer analyzer) {
        this.statsDAO = statsDAO;
        this.analyzer = analyzer;
    }

    public List<ChampionshipRanking> getChampionshipRankings() {
        Map<Championship, List<Integer>> stats = statsDAO.getDifferenceGoalsByChampionship();
        return analyzer.computeRankings(stats);
    }
}
