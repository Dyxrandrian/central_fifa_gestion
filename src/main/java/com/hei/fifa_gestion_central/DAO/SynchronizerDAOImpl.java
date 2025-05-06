package com.hei.fifa_gestion_central.DAO;

import com.hei.fifa_gestion_central.DTO.ClubDTO;
import com.hei.fifa_gestion_central.DTO.CoachDTO;
import com.hei.fifa_gestion_central.DTO.PlayerDTO;
import com.hei.fifa_gestion_central.Entity.Club;
import com.hei.fifa_gestion_central.Entity.ClubRanking;
import com.hei.fifa_gestion_central.Entity.Coach;
import com.hei.fifa_gestion_central.database.Datasource;
import com.hei.fifa_gestion_central.Entity.PlayerRanking;
import com.hei.fifa_gestion_central.enums.Championship;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.UUID;

@Repository
public class SynchronizerDAOImpl implements SynchronizeDAO {
    private final Datasource datasource;


    public SynchronizerDAOImpl(Datasource datasource) {
        this.datasource = datasource;
    }

    public void saveClubs(List<ClubDTO> clubs) {
        String insertCoachSql = "INSERT INTO coach (id, name, nationality) VALUES (?, ?, ?) " +
                "ON CONFLICT (id) DO NOTHING";

        String insertClubSql = "INSERT INTO club (id, name, acronym, year_creation, stadium, coach_id) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO NOTHING";

        try (Connection conn = datasource.getConnection();
             PreparedStatement coachStmt = conn.prepareStatement(insertCoachSql);
             PreparedStatement clubStmt = conn.prepareStatement(insertClubSql)) {

            for (ClubDTO club : clubs) {
                // Insertion du coach
                CoachDTO coach = club.getCoach();
                if (coach != null) {
                    coachStmt.setObject(1, coach.getId());  // Conversion en UUID si nécessaire
                    coachStmt.setString(2, coach.getName());
                    coachStmt.setString(3, coach.getNationality());
                    coachStmt.addBatch();
                }

                // Insertion du club
                clubStmt.setObject(1, club.getId()); // Assurez-vous que club.getId() est aussi de type UUID
                clubStmt.setString(2, club.getName());
                clubStmt.setString(3, club.getAcronym());
                clubStmt.setInt(4, club.getYearCreation());
                clubStmt.setString(5, club.getStadium());
                clubStmt.setObject(6, coach != null ? coach.getId() : null); // Conversion du coach_id si nécessaire
                clubStmt.addBatch();
            }

            // Exécution des batchs
            coachStmt.executeBatch();
            clubStmt.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde des clubs : " + e.getMessage(), e);
        }
    }


    public void savePlayers(List<PlayerDTO> players) {
        String sql = "INSERT INTO player (id, name, number, position, nationality, age, club_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO NOTHING";

        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (PlayerDTO player : players) {
                stmt.setObject(1, player.getId()); // UUID
                stmt.setString(2, player.getName());
                stmt.setInt(3, player.getNumber());
                stmt.setObject(4, player.getPosition().name(), Types.OTHER); // On utilise Types.OTHER pour l'ENUM
                stmt.setString(5, player.getNationality());
                stmt.setInt(6, player.getAge());
                stmt.setObject(7, player.getClub() != null ? player.getClub().getId() : null); // Peut être null
                stmt.addBatch();
            }

            stmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde des joueurs : " + e.getMessage(), e);
        }
    }


    @Override
    public void saveClubRankings(List<ClubRanking> clubRankings, Championship championship, int seasonYear) {
        String sql = """
        INSERT INTO club_ranking (
            id, championship_id, club_id, rank, ranking_points,
            scored_goals, conceded_goals, difference_goals,
            clean_sheet_number, season_year
        )
        VALUES (?, ?, ?::uuid, ?, ?, ?, ?, ?, ?, ?)
        ON CONFLICT (club_id, championship_id, season_year)
        DO UPDATE SET
            rank = EXCLUDED.rank,
            ranking_points = EXCLUDED.ranking_points,
            scored_goals = EXCLUDED.scored_goals,
            conceded_goals = EXCLUDED.conceded_goals,
            difference_goals = EXCLUDED.difference_goals,
            clean_sheet_number = EXCLUDED.clean_sheet_number
    """;

        try (Connection conn = datasource.getConnection()) {
            conn.setAutoCommit(false); // Commencer une transaction

            UUID championshipId = getChampionshipIdByEnum(conn, championship);
            if (championshipId == null) {
                throw new RuntimeException("Aucun championnat trouvé avec le nom : " + championship);
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (ClubRanking cr : clubRankings) {
                    stmt.clearParameters(); // Sécurité si le JDBC l'exige
                    stmt.setObject(1, UUID.randomUUID()); // id
                    stmt.setObject(2, championshipId);     // championship_id
                    stmt.setObject(3, cr.getClub().getId()); // club_id

                    if (cr.getRank() != null) {
                        stmt.setInt(4, cr.getRank());
                    } else {
                        stmt.setNull(4, Types.INTEGER);
                    }

                    stmt.setInt(5, cr.getRankingPoints());
                    stmt.setInt(6, cr.getScoredGoals());
                    stmt.setInt(7, cr.getConcededGoals());
                    stmt.setInt(8, cr.getDifferenceGoals());
                    stmt.setInt(9, cr.getCleanSheetNumber());
                    stmt.setInt(10, seasonYear);

                    stmt.addBatch();
                }

                stmt.executeBatch();
                conn.commit(); // Terminer la transaction avec succès
            } catch (SQLException e) {
                conn.rollback(); // Annuler si erreur dans le batch
                throw new RuntimeException("Erreur lors de la sauvegarde des classements clubs : " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la connexion à la base de données : " + e.getMessage(), e);
        }
    }

    @Override
    public void savePlayerRankings(List<PlayerRanking> playerRankings, Championship championship, int seasonYear) {
        String insertPlayingTimeSql = """
        INSERT INTO playing_time (value, duration_unit)
        VALUES (?, ?::duration_unit)
        RETURNING id
    """;

        String insertPlayerRankingSql = """
        INSERT INTO player_ranking (
            id, player_id, rank, championship_id,
            scored_goals, playing_time_id, season_year
        )
        VALUES (?, ?, ?, ?, ?, ?, ?)
        ON CONFLICT (player_id)
        DO UPDATE SET
            rank = EXCLUDED.rank,
            championship_id = EXCLUDED.championship_id,
            scored_goals = EXCLUDED.scored_goals,
            playing_time_id = EXCLUDED.playing_time_id,
            season_year = EXCLUDED.season_year
    """;

        String selectPlayerIdSql = "SELECT id FROM player WHERE name = ?";

        try (Connection conn = datasource.getConnection()) {
            conn.setAutoCommit(false);

            // Récupérer l'ID du championnat à partir de l'énumération
            UUID championshipId = getChampionshipIdByEnum(conn, championship);
            if (championshipId == null) {
                throw new RuntimeException("Aucun championnat trouvé avec le nom : " + championship);
            }

            try (
                    PreparedStatement insertPlayingTimeStmt = conn.prepareStatement(insertPlayingTimeSql);
                    PreparedStatement insertPlayerRankingStmt = conn.prepareStatement(insertPlayerRankingSql);
                    PreparedStatement selectPlayerStmt = conn.prepareStatement(selectPlayerIdSql)
            ) {
                for (PlayerRanking pr : playerRankings) {
                    // Insérer dans la table playing_time et récupérer l'ID généré
                    insertPlayingTimeStmt.setDouble(1, pr.getPlayingTime().getValue());
                    insertPlayingTimeStmt.setString(2, pr.getPlayingTime().getDurationUnit().name());
                    ResultSet rsPlayingTime = insertPlayingTimeStmt.executeQuery();

                    if (!rsPlayingTime.next()) {
                        System.err.println("Erreur lors de l'insertion du temps de jeu pour le joueur : " + pr.getName());
                        continue; // Passer au joueur suivant en cas d'erreur d'insertion
                    }

                    UUID playingTimeId = UUID.fromString(rsPlayingTime.getString("id"));

                    // Recherche du player_id par nom
                    selectPlayerStmt.setString(1, pr.getName());
                    ResultSet rsPlayer = selectPlayerStmt.executeQuery();

                    if (!rsPlayer.next()) {
                        System.err.println("Joueur introuvable : " + pr.getName());
                        continue; // Passer au joueur suivant s'il n'est pas trouvé
                    }

                    UUID playerId = UUID.fromString(rsPlayer.getString("id"));

                    // Insérer dans player_ranking en utilisant l'ID de playing_time
                    insertPlayerRankingStmt.clearParameters();
                    insertPlayerRankingStmt.setObject(1, UUID.randomUUID()); // Générer un ID pour player_ranking
                    insertPlayerRankingStmt.setObject(2, playerId); // player_id

                    if (pr.getRank() != null) {
                        insertPlayerRankingStmt.setInt(3, pr.getRank());
                    } else {
                        insertPlayerRankingStmt.setNull(3, Types.INTEGER);
                    }

                    insertPlayerRankingStmt.setObject(4, championshipId); // championship_id
                    insertPlayerRankingStmt.setInt(5, pr.getScoredGoals());
                    insertPlayerRankingStmt.setObject(6, playingTimeId); // playing_time_id
                    insertPlayerRankingStmt.setInt(7, seasonYear);

                    insertPlayerRankingStmt.addBatch();
                }

                // Exécuter le batch d'insertion
                insertPlayerRankingStmt.executeBatch();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                throw new RuntimeException("Erreur lors de la sauvegarde des classements joueurs : " + e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion : " + e.getMessage(), e);
        }
    }






    private UUID getChampionshipIdByEnum(Connection conn, Championship championship) throws SQLException {
        String query = "SELECT id FROM championship WHERE name::text = ?;\n";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, championship.name());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return UUID.fromString(rs.getString("id"));
                }
            }
        }
        return null;
    }





}
