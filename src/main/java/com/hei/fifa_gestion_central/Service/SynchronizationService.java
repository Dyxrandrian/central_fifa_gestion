    package com.hei.fifa_gestion_central.Service;

    import com.fasterxml.jackson.core.type.TypeReference;
    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.hei.fifa_gestion_central.DAO.SynchronizeDAO;
    import com.hei.fifa_gestion_central.DTO.ClubDTO;
    import com.hei.fifa_gestion_central.DTO.PlayerDTO;
    import com.hei.fifa_gestion_central.DTO.PlayerStatsDTO;
    import com.hei.fifa_gestion_central.Entity.ClubRanking;
    import com.hei.fifa_gestion_central.Entity.PlayerRanking;
    import com.hei.fifa_gestion_central.Entity.PlayingTime;
    import com.hei.fifa_gestion_central.enums.Championship;
    import org.springframework.stereotype.Service;

    import java.net.HttpURLConnection;
    import java.net.URL;
    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    public class SynchronizationService {
        private final SynchronizeDAO synchronizeDAO;

        private static final List<String> CHAMPIONSHIP_PORTS = List.of(
                "8084", "8085"
        );

        public SynchronizationService(SynchronizeDAO synchronizeDAO) {
            this.synchronizeDAO = synchronizeDAO;
        }

        public void synchronize() {
            for (String port : CHAMPIONSHIP_PORTS) {
                Championship championship = Championship.valueOf(getChampionshipByPort(port));

                List<ClubDTO> clubDTOs = fetchDataFromApi(
                        "http://localhost:" + port + "/clubs",
                        new TypeReference<List<ClubDTO>>() {}
                );

                List<PlayerDTO> playerDTOs = fetchDataFromApi(
                        "http://localhost:" + port + "/players",
                        new TypeReference<List<PlayerDTO>>() {}
                );

                List<ClubRanking> clubRankings = fetchDataFromApi(
                        "http://localhost:" + port + "/clubs/statistics/2024",
                        new TypeReference<List<ClubRanking>>() {}
                );

                List<PlayerRanking> playerRankings = playerDTOs.stream().map(player -> {
                    try {
                        // Récupérer les statistiques du joueur
                        PlayerStatsDTO playerStats = fetchDataFromApi(
                                "http://localhost:" + port + "/players/" + player.getId() + "/statistics/2024",
                                new TypeReference<PlayerStatsDTO>() {}
                        );

                        // Création du PlayerRanking
                        PlayerRanking ranking = new PlayerRanking();
                        ranking.setId(String.valueOf(player.getId()));
                        ranking.setName(player.getName());
                        ranking.setNumber(player.getNumber());
                        ranking.setPosition(player.getPosition());
                        ranking.setNationality(player.getNationality());
                        ranking.setAge(player.getAge());
                        ranking.setChampionship(championship);

                        // Ajouter les données statistiques
                        ranking.setScoredGoals(playerStats.getScoredGoals());
                        // Créer l'objet PlayingTime et l'affecter à playerRanking
                        PlayingTime playingTime = new PlayingTime();
                        playingTime.setValue(playerStats.getPlayingTime().getValue());
                        playingTime.setDurationUnit(playerStats.getPlayingTime().getDurationUnit());
                        ranking.setPlayingTime(playingTime);

                        return ranking;
                    } catch (Exception e) {
                        System.err.println("Erreur lors de la récupération des statistiques du joueur " + player.getId() + ": " + e.getMessage());
                        return null;
                    }
                }).filter(r -> r != null).collect(Collectors.toList());

                // Sauvegarder dans la base de données
                synchronizeDAO.saveClubs(clubDTOs);
                synchronizeDAO.savePlayers(playerDTOs);
                synchronizeDAO.saveClubRankings(clubRankings, championship, 2024);
                synchronizeDAO.savePlayerRankings(playerRankings, championship, 2024);
            }
        }

        private String getChampionshipByPort(String port) {
            switch (port) {
                case "8084":
                    return "SERIEA";
                case "8085":
                    return "LIGUE_1";
                default:
                    throw new IllegalArgumentException("Port inconnu: " + port);
            }
        }

        // Méthode générique pour récupérer des données depuis l'API
        private <T> T fetchDataFromApi(String apiUrl, TypeReference<T> typeReference) {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                int status = conn.getResponseCode();
                if (status == 404) {
                    return null;
                } else if (status != 200) {
                    throw new RuntimeException("Erreur HTTP : " + status + " pour l'URL : " + apiUrl);
                }

                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(conn.getInputStream(), typeReference);
            } catch (Exception e) {
                throw new RuntimeException("Erreur lors de la récupération des données depuis l'API : " + apiUrl + " -> " + e.getMessage(), e);
            }
        }
    }
