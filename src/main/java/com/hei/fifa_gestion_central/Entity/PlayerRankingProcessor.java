package com.hei.fifa_gestion_central.Entity;

import com.hei.fifa_gestion_central.enums.DurationUnit;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlayerRankingProcessor {

    public List<PlayerRanking> process(List<PlayerRanking> players, DurationUnit unit, int top) {
        // Convertir les durées dans l'unité demandée
        for (PlayerRanking player : players) {
            double normalized = convertToRequestedUnit(player.getPlayingTime(), unit);
            player.getPlayingTime().setValue(normalized);
            player.getPlayingTime().setDurationUnit(unit); // Optionnel : harmoniser aussi l'unité
        }

        // Trier par buts marqués puis par temps de jeu
        players.sort((p1, p2) -> {
            int goalsComparison = Integer.compare(p2.getScoredGoals(), p1.getScoredGoals());
            if (goalsComparison != 0) return goalsComparison;
            return Double.compare(p2.getPlayingTime().getValue(), p1.getPlayingTime().getValue());
        });

        // Attribuer les rangs
        int rank = 1;
        for (PlayerRanking player : players) {
            player.setRank(rank++);
        }

        // Retourner les X meilleurs joueurs
        return players.stream().limit(top).collect(Collectors.toList());
    }

    private double convertToRequestedUnit(PlayingTime time, DurationUnit targetUnit) {
        double value = time.getValue();
        DurationUnit currentUnit = time.getDurationUnit();

        // Convertir en secondes
        double seconds = switch (currentUnit) {
            case HOUR -> value * 3600;
            case MINUTE -> value * 60;
            case SECOND -> value;
        };

        // Convertir en unité cible
        return switch (targetUnit) {
            case HOUR -> seconds / 3600;
            case MINUTE -> seconds / 60;
            case SECOND -> seconds;
        };
    }
}
