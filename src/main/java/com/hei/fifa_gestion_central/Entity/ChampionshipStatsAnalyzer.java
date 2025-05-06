package com.hei.fifa_gestion_central.Entity;

import com.hei.fifa_gestion_central.enums.Championship;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ChampionshipStatsAnalyzer {
    public List<ChampionshipRanking> computeRankings(Map<Championship, List<Integer>> stats) {
        List<ChampionshipRanking> rankings = stats.entrySet().stream()
                .map(entry -> new ChampionshipRanking(
                        0,
                        entry.getKey(),
                        calculateMedian(entry.getValue())
                ))
                .sorted(Comparator.comparingDouble(ChampionshipRanking::getDifferenceGoalsMedian))
                .collect(Collectors.toList());

        for (int i = 0; i < rankings.size(); i++) {
            rankings.get(i).setRank(i + 1);
        }

        return rankings;
    }

    private double calculateMedian(List<Integer> values) {
        Collections.sort(values);
        int size = values.size();
        if (size % 2 == 1) {
            return values.get(size / 2);
        } else {
            return (values.get(size / 2 - 1) + values.get(size / 2)) / 2.0;
        }
    }
}
