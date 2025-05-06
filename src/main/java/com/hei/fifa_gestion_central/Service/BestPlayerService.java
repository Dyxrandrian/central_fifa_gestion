package com.hei.fifa_gestion_central.Service;

import com.hei.fifa_gestion_central.Entity.PlayerRanking;
import com.hei.fifa_gestion_central.enums.DurationUnit;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BestPlayerService {
    public List<PlayerRanking> getBestPlayers(int top, DurationUnit playingTimeUnit) {
        // La logique métier sera ajoutée plus tard : tri des joueurs par buts marqués, puis par temps de jeu
        return List.of(); // Renvoie une liste vide pour l’instant
    }
}
