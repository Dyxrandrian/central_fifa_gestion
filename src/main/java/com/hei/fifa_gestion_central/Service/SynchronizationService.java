/*package com.hei.fifa_gestion_central.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hei.fifa_gestion_central.DAO.SynchronizeDAO;
import com.hei.fifa_gestion_central.Entity.PlayerRanking;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
public class SynchronizationService {
    private final SynchronizeDAO synchronizeDAO;

    public SynchronizationService(SynchronizeDAO synchronizeDAO) {
        this.synchronizeDAO = synchronizeDAO;
    }

    public void synchronize() {
        List<PlayerRanking> players = fetchPlayerRankingsFromExternalApi();
        synchronizeDAO.savePlayerRankings(players);
    }

    private List<PlayerRanking> fetchPlayerRankingsFromExternalApi() {
        try {
            // 1. Récupérer la liste des joueurs
            URL playersUrl = new URL("http://localhost:8080/players");
            HttpURLConnection connPlayers = (HttpURLConnection) playersUrl.openConnection();
            connPlayers.setRequestMethod("GET");
            connPlayers.setRequestProperty("Accept", "application/json");

            if (connPlayers.getResponseCode() != 200) {
                throw new RuntimeException("Erreur HTTP /players : " + connPlayers.getResponseCode());
            }

            ObjectMapper mapper = new ObjectMapper();
            List<PlayerDTO> players = mapper.readValue(connPlayers.getInputStream(),
                    new TypeReference<List<PlayerDTO>>() {});
            connPlayers.disconnect();

            // 2. Récupérer les statistiques de chaque joueur
            int currentSeason = 2024; // ou autre logique pour déterminer l'année
            List<PlayerRanking> rankings = new ArrayList<>();

            for (PlayerDTO player : players) {
                String statsUrlStr = String.format("http://localhost:8080/players/%s/statistics/%d", player.getId(), currentSeason);
                URL statsUrl = new URL(statsUrlStr);
                HttpURLConnection connStats = (HttpURLConnection) statsUrl.openConnection();
                connStats.setRequestMethod("GET");
                connStats.setRequestProperty("Accept", "application/json");

                if (connStats.getResponseCode() != 200) {
                    throw new RuntimeException("Erreur HTTP /players/{id}/statistics : " + connStats.getResponseCode());
                }

                PlayerStatisticsDTO stats = mapper.readValue(connStats.getInputStream(), PlayerStatisticsDTO.class);
                connStats.disconnect();

                // Adapter selon ton modèle PlayerRanking
                PlayerRanking ranking = new PlayerRanking();
                ranking.setPlayerId(player.getId());
                ranking.setPlayerName(player.getName());
                ranking.setScoredGoals(stats.getScoredGoals());
                ranking.setPlayingTime(stats.getPlayingTime().getValue());
                // etc.
                rankings.add(ranking);
            }

            return rankings;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la synchronisation complète : " + e.getMessage(), e);
        }
    }

}*/
