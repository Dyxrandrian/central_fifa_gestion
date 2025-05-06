package com.hei.fifa_gestion_central.Entity;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClubRankingProcessor {
    public List<ClubRanking> process(List<ClubRanking> rankings, int top) {
        rankings.sort(Comparator.comparingInt(ClubRanking::getRankingPoints).reversed());

        int rank = 1;
        for (ClubRanking ranking : rankings) {
            ranking.setRank(rank++);
        }

        return rankings.stream().limit(top).collect(Collectors.toList());
    }
}
