package com.hei.fifa_gestion_central.Controller;

import com.hei.fifa_gestion_central.Service.BestPlayerService;
import com.hei.fifa_gestion_central.entity.PlayerRanking;
import com.hei.fifa_gestion_central.enums.DurationUnit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bestPlayers")
public class BestPlayerController {
    private final BestPlayerService bestPlayerService;

    public BestPlayerController(BestPlayerService bestPlayerService) {
        this.bestPlayerService = bestPlayerService;
    }

    @GetMapping
    public ResponseEntity<List<PlayerRanking>> getBestPlayers(
            @RequestParam(defaultValue = "5") int top,
            @RequestParam DurationUnit playingTimeUnit
    ) {
        List<PlayerRanking> bestPlayers = bestPlayerService.getBestPlayers(top, playingTimeUnit);
        return ResponseEntity.ok(bestPlayers);
    }
}
