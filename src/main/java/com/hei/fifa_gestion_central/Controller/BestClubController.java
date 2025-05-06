package com.hei.fifa_gestion_central.Controller;

import com.hei.fifa_gestion_central.Entity.ClubRanking;
import com.hei.fifa_gestion_central.Service.BestClubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bestClubs")
public class BestClubController {
    private final BestClubService bestClubService;

    public BestClubController(BestClubService bestClubService) {
        this.bestClubService = bestClubService;
    }

    @GetMapping
    public ResponseEntity<List<ClubRanking>> getBestClubs(
            @RequestParam(defaultValue = "5") int top
    ) {
        List<ClubRanking> bestClubs = bestClubService.getBestClubs(top);
        return ResponseEntity.ok(bestClubs);
    }
}
