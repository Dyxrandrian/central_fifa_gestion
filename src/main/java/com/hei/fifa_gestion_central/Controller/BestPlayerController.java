package com.hei.fifa_gestion_central.Controller;

import com.hei.fifa_gestion_central.Entity.PlayerRanking;
import com.hei.fifa_gestion_central.Service.BestPlayerService;
<<<<<<< HEAD
=======
import com.hei.fifa_gestion_central.Entity.PlayerRanking;
>>>>>>> 986e6b9a140816c28089970ebf7760df4069da92
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
            @RequestParam(name = "top", defaultValue = "5") int top,
            @RequestParam(name = "playingTimeUnit") DurationUnit unit
    ) {
        List<PlayerRanking> bestPlayers = bestPlayerService.getBestPlayers(top, unit);
        return ResponseEntity.ok(bestPlayers);
    }
}
