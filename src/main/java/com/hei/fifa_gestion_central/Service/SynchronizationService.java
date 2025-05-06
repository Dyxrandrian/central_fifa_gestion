package com.hei.fifa_gestion_central.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hei.fifa_gestion_central.DAO.SynchronizeDAO;
import com.hei.fifa_gestion_central.DTO.ClubDTO;
import com.hei.fifa_gestion_central.DTO.PlayerDTO;
import com.hei.fifa_gestion_central.DTO.PlayerStatsDTO;
import com.hei.fifa_gestion_central.Entity.PlayerRanking;
import com.hei.fifa_gestion_central.Entity.PlayingTime;
import com.hei.fifa_gestion_central.enums.Championship;
import com.hei.fifa_gestion_central.enums.DurationUnit;
import org.springframework.stereotype.Service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class SynchronizationService {
    private final SynchronizeDAO synchronizeDAO;

    public SynchronizationService(SynchronizeDAO synchronizeDAO) {
        this.synchronizeDAO = synchronizeDAO;
    }

    public void synchronize() {
        List<ClubDTO> clubDTO = fetchClubsFromExternalApi();
        List<PlayerDTO> playerDTO = fetchPlayersFromExternalApi();
        synchronizeDAO.saveClubs(clubDTO);
        synchronizeDAO.savePlayers(playerDTO);
    }

    private List<ClubDTO> fetchClubsFromExternalApi() {
        try {
            // 1. Récupérer la liste des clubs
            URL clubsUrl = new URL("http://localhost:8080/clubs");
            HttpURLConnection connClubs = (HttpURLConnection) clubsUrl.openConnection();
            connClubs.setRequestMethod("GET");
            connClubs.setRequestProperty("Accept", "application/json");

            if (connClubs.getResponseCode() != 200) {
                throw new RuntimeException("Erreur HTTP /clubs : " + connClubs.getResponseCode());
            }

            // 2. Mapper le résultat JSON vers une liste de ClubDTO
            ObjectMapper mapper = new ObjectMapper();
            List<ClubDTO> clubs = mapper.readValue(
                    connClubs.getInputStream(),
                    new TypeReference<List<ClubDTO>>() {}
            );
            connClubs.disconnect();

            System.out.println("Fetched " + clubs.size() + " clubs from: " + clubsUrl);
            return clubs;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des clubs : " + e.getMessage(), e);
        }
    }

    private List<PlayerDTO> fetchPlayersFromExternalApi() {
        try {
            // 1. Récupérer la liste des joueurs
            URL playersUrl = new URL("http://localhost:8080/players");
            HttpURLConnection connPlayers = (HttpURLConnection) playersUrl.openConnection();
            connPlayers.setRequestMethod("GET");
            connPlayers.setRequestProperty("Accept", "application/json");

            if (connPlayers.getResponseCode() != 200) {
                throw new RuntimeException("Erreur HTTP /players : " + connPlayers.getResponseCode());
            }

            // 2. Mapper le résultat JSON vers une liste de PlayerDTO
            ObjectMapper mapper = new ObjectMapper();
            List<PlayerDTO> players = mapper.readValue(
                    connPlayers.getInputStream(),
                    new TypeReference<List<PlayerDTO>>() {}
            );
            connPlayers.disconnect();

            System.out.println("Fetched " + players.size() + " players from: " + playersUrl);
            return players;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des joueurs : " + e.getMessage(), e);
        }
    }



}
