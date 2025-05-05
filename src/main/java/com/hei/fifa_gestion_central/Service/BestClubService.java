package com.hei.fifa_gestion_central.Service;

import com.hei.fifa_gestion_central.entity.ClubRanking;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BestClubService {
    public List<ClubRanking> getBestClubs(int top) {
        // Logique à implémenter : classement des clubs selon les points et autres critères
        return List.of(); // Retourne une liste vide pour l’instant
    }
}
