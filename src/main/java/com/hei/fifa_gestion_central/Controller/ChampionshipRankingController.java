package com.hei.fifa_gestion_central.Controller;

import com.hei.fifa_gestion_central.Service.ChampionshipRankingService;
import com.hei.fifa_gestion_central.Entity.ChampionshipRanking;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/championshipRankings")
public class ChampionshipRankingController {
    private final ChampionshipRankingService championshipRankingService;

    public ChampionshipRankingController(ChampionshipRankingService championshipRankingService) {
        this.championshipRankingService = championshipRankingService;
    }

    @GetMapping
    public ResponseEntity<List<ChampionshipRanking>> getChampionshipRankings() {
        List<ChampionshipRanking> rankings = championshipRankingService.getChampionshipRankings();
        return ResponseEntity.ok(rankings);
    }
}
