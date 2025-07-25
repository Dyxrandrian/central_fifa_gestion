package com.hei.fifa_gestion_central.Controller;

import com.hei.fifa_gestion_central.Service.ClubRankingService;
import com.hei.fifa_gestion_central.Entity.ClubRanking;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/bestClubs")
public class ClubRankingController {
    private final ClubRankingService service;

    public ClubRankingController(ClubRankingService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ClubRanking>> getBestClubs(@RequestParam(defaultValue = "5") int top) {
        try {
            List<ClubRanking> bestClubs = service.getBestClubs(top);
            return ResponseEntity.ok(bestClubs);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
