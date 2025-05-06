package com.hei.fifa_gestion_central.DAO;

import com.hei.fifa_gestion_central.DTO.ClubDTO;
import com.hei.fifa_gestion_central.DTO.CoachDTO;
import com.hei.fifa_gestion_central.DTO.PlayerDTO;
import com.hei.fifa_gestion_central.Entity.PlayerRanking;
import com.hei.fifa_gestion_central.database.DatasourceCentral;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SynchronizerDAOImpl implements SynchronizeDAO {
    private final DatasourceCentral datasource;


    public SynchronizerDAOImpl(DatasourceCentral datasource) {
        this.datasource = datasource;
    }

    @Override
    public void savePlayerRankings(List<PlayerRanking> playerRankings) {
        String sql = "INSERT INTO player_ranking (id, name, number, position, nationality, age, championship, scored_goals, playing_time_value, playing_time_unit, rank) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (PlayerRanking player : playerRankings) {
                stmt.setString(1, player.getId());
                stmt.setString(2, player.getName());
                stmt.setInt(3, player.getNumber());
                stmt.setString(4, player.getPosition().name());
                stmt.setString(5, player.getNationality());
                stmt.setInt(6, player.getAge());
                stmt.setString(7, player.getChampionship().name());
                stmt.setInt(8, player.getScoredGoals());
                stmt.setDouble(9, player.getPlayingTime().getValue());
                stmt.setString(10, player.getPlayingTime().getDurationUnit().name());
                stmt.setInt(11, player.getRank());
                stmt.addBatch(); // optimise l'insert
            }

            stmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde des rankings joueurs : " + e.getMessage(), e);
        }
    }

    @Override
    public void saveClubs(List<ClubDTO> clubs) {
        String checkCoachSql = "SELECT COUNT(1) FROM coach WHERE id = ?";
        String insertCoachSql = "INSERT INTO coach (id, name, nationality) VALUES (?, ?, ?)";

        String checkClubSql = "SELECT COUNT(1) FROM club WHERE id = ?";
        String insertClubSql = "INSERT INTO club (id, name, acronym, year_creation, stadium, coach_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = datasource.getConnection();
             PreparedStatement checkCoachStmt = conn.prepareStatement(checkCoachSql);
             PreparedStatement insertCoachStmt = conn.prepareStatement(insertCoachSql);
             PreparedStatement checkClubStmt = conn.prepareStatement(checkClubSql);
             PreparedStatement insertClubStmt = conn.prepareStatement(insertClubSql)) {

            for (ClubDTO club : clubs) {
                CoachDTO coach = club.getCoach();

                // Vérifier et insérer le coach s'il n'existe pas
                if (coach != null) {
                    checkCoachStmt.setObject(1, coach.getId());
                    ResultSet coachRs = checkCoachStmt.executeQuery();
                    if (coachRs.next() && coachRs.getInt(1) == 0) {
                        insertCoachStmt.setObject(1, coach.getId());
                        insertCoachStmt.setString(2, coach.getName());
                        insertCoachStmt.setString(3, coach.getNationality());
                        insertCoachStmt.addBatch();
                    }
                }

                // Vérifier si le club existe
                checkClubStmt.setObject(1, club.getId());
                ResultSet clubRs = checkClubStmt.executeQuery();
                if (clubRs.next() && clubRs.getInt(1) > 0) {
                    continue; // club déjà présent → on ignore
                }

                // Insérer le club
                insertClubStmt.setObject(1, club.getId());
                insertClubStmt.setString(2, club.getName());
                insertClubStmt.setString(3, club.getAcronym());
                insertClubStmt.setInt(4, club.getYearCreation());
                insertClubStmt.setString(5, club.getStadium());
                insertClubStmt.setObject(6, coach != null ? coach.getId() : null);
                insertClubStmt.addBatch();
            }

            // Exécution des batchs
            insertCoachStmt.executeBatch();
            insertClubStmt.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde des clubs : " + e.getMessage(), e);
        }
    }



    @Override
    public void savePlayers(List<PlayerDTO> players) {
        // Requête SQL pour insérer un joueur
        String insertPlayerSql = "INSERT INTO player (id, name, position, nationality, age, club_id) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO NOTHING"; // Pour éviter les doublons en cas d'existence de l'ID

        // Requête pour vérifier si le joueur existe déjà
        String checkPlayerExistenceSql = "SELECT COUNT(1) FROM player WHERE id = ?";

        try (Connection conn = datasource.getConnection();
             PreparedStatement playerStmt = conn.prepareStatement(insertPlayerSql);
             PreparedStatement checkPlayerStmt = conn.prepareStatement(checkPlayerExistenceSql)) {

            for (PlayerDTO player : players) {
                // Vérifier si le joueur existe déjà dans la base
                checkPlayerStmt.setObject(1, player.getId());
                ResultSet rs = checkPlayerStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    // Le joueur existe déjà, ne rien faire
                    continue;
                }

                // Insertion du joueur
                playerStmt.setObject(1, player.getId());
                playerStmt.setString(2, player.getName());
                playerStmt.setString(3, player.getPosition().name());  // Position (STRIKER, MIDFIELDER, etc.)
                playerStmt.setString(4, player.getNationality());
                playerStmt.setInt(5, player.getAge());

                // Lien avec le club (ID du club est référencé)
                playerStmt.setObject(6, player.getClub() != null ? player.getClub().getId() : null);

                playerStmt.addBatch(); // Ajouter à la batch pour une insertion optimisée
            }

            // Exécution de la batch d'insertion des joueurs
            playerStmt.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde des joueurs : " + e.getMessage(), e);
        }
    }

}
