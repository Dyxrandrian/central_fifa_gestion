package com.hei.fifa_gestion_central.teste;

import com.hei.fifa_gestion_central.Entity.PlayerRanking;
import com.hei.fifa_gestion_central.Entity.PlayerRankingProcessor;
import com.hei.fifa_gestion_central.Entity.PlayingTime;
import com.hei.fifa_gestion_central.enums.Championship;
import com.hei.fifa_gestion_central.enums.DurationUnit;
import com.hei.fifa_gestion_central.enums.PlayerPosition;

import java.util.ArrayList;
import java.util.List;

public class TestingPlayerRankingProcessor {
        public static void main (String[] args) {
            // Créez des objets fictifs pour les tests
            List<PlayerRanking> players = new ArrayList<>();

            // Ajouter des joueurs fictifs avec tous les attributs
            players.add(new PlayerRanking(
                    1,  // rank
                    "75e5062e-07e6-4d01-a83d-6de8d260cc48",  // id
                    "Player 3",  // name
                    10,  // number
                    PlayerPosition.DEFENSE,  // position
                    "German",  // nationality
                    30,  // age
                    Championship.LA_LIGA,  // championship
                    10,  // scoredGoals
                    new PlayingTime(1800, DurationUnit.MINUTE)  // playingTime
            ));

            players.add(new PlayerRanking(
                    2,  // rank
                    "46d64741-884c-423d-9d6a-65808955204b",  // id
                    "Player 2",  // name
                    12,  // number
                    PlayerPosition.MIDFIELDER,  // position
                    "Spanish",  // nationality
                    24,  // age
                    Championship.PREMIER_LEAGUE,  // championship
                    12,  // scoredGoals
                    new PlayingTime(1200, DurationUnit.MINUTE)  // playingTime
            ));

            players.add(new PlayerRanking(
                    3,  // rank
                    "5490267d-0bc7-4c65-b522-47f8e39f80e9",  // id
                    "Player 1",  // name
                    9,  // number
                    PlayerPosition.STRIKER,  // position
                    "English",  // nationality
                    28,  // age
                    Championship.PREMIER_LEAGUE,  // championship
                    15,  // scoredGoals
                    new PlayingTime(900, DurationUnit.MINUTE)  // playingTime
            ));

            players.add(new PlayerRanking(
                    4,  // rank
                    "e735dc33-814f-443e-8e9b-775ee979c111",  // id
                    "Player 4",  // name
                    18,  // number
                    PlayerPosition.STRIKER,  // position
                    "French",  // nationality
                    27,  // age
                    Championship.LA_LIGA,  // championship
                    18,  // scoredGoals
                    new PlayingTime(1500, DurationUnit.MINUTE)  // playingTime
            ));

            players.add(new PlayerRanking(
                    5,  // rank
                    "6dc68870-126e-48a0-89d7-bed4ff3dc12a",  // id
                    "Player 6",  // name
                    20,  // number
                    PlayerPosition.MIDFIELDER,  // position
                    "Brazilian",  // nationality
                    22,  // age
                    Championship.SERIA,  // championship
                    20,  // scoredGoals
                    new PlayingTime(1000, DurationUnit.MINUTE)  // playingTime
            ));

            // Créez une instance de PlayerRankingProcessor
            PlayerRankingProcessor processor = new PlayerRankingProcessor();

            // Appelez la méthode process
            List<PlayerRanking> sortedPlayers = processor.process(players, DurationUnit.MINUTE, 5);

            // Afficher les résultats triés
            sortedPlayers.forEach(player -> {
                System.out.println(player.getName() + " - Goals: " + player.getScoredGoals() + ", Playing Time: " + player.getPlayingTime().getValue() + " " + player.getPlayingTime().getDurationUnit());
            });
        }
}


