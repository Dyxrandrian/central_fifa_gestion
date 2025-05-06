package com.hei.fifa_gestion_central.DAO;

import com.hei.fifa_gestion_central.DTO.ClubDTO;
import com.hei.fifa_gestion_central.DTO.CoachDTO;
import com.hei.fifa_gestion_central.DTO.PlayerDTO;
import com.hei.fifa_gestion_central.Entity.ClubRanking;
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
                    coachStmt.setObject(1, coach.getId());
                    coachStmt.setString(2, coach.getName());
                    coachStmt.setString(3, coach.getNationality());
                    coachStmt.addBatch();
                }

                // Insertion du club
                clubStmt.setObject(1, club.getId());
                clubStmt.setString(2, club.getName());
                clubStmt.setString(3, club.getAcronym());
                clubStmt.setInt(4, club.getYearCreation());
                clubStmt.setString(5, club.getStadium());
                clubStmt.setObject(6, coach != null ? coach.getId() : null);
                clubStmt.addBatch();
            }

            // Exécution des batchs
            coachStmt.executeBatch();
            clubStmt.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde des clubs : " + e.getMessage(), e);
        }
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

    public void savePlayers(List<PlayerDTO> players) {
        String sql = "INSERT INTO player (id, name, position, nationality, age, club_id) " +
                "VALUES (?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT (id) DO NOTHING";

        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (PlayerDTO player : players) {
                stmt.setObject(1, player.getId()); // UUID
                stmt.setString(2, player.getName());
                stmt.setObject(3, player.getPosition().name(), Types.OTHER); // On utilise Types.OTHER pour l'ENUM
                stmt.setString(4, player.getNationality());
                stmt.setInt(5, player.getAge());
                stmt.setObject(6, player.getClub() != null ? player.getClub().getId() : null); // Peut être null
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
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
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
                    stmt.setObject(3, UUID.fromString(cr.getClub().getId())); // club_id

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
        String insertSql = """
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
            UUID championshipId = getChampionshipIdByEnum(conn, championship);
            if (championshipId == null) {
                throw new RuntimeException("Aucun championnat trouvé avec le nom : " + championship);
            }

            try (
                    PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                    PreparedStatement selectPlayerStmt = conn.prepareStatement(selectPlayerIdSql)
            ) {
                for (PlayerRanking pr : playerRankings) {
                    // Recherche du player_id par nom
                    selectPlayerStmt.setString(1, pr.getName());
                    ResultSet rs = selectPlayerStmt.executeQuery();

                    if (!rs.next()) {
                        System.err.println("Joueur introuvable : " + pr.getName());
                        continue; // Skip ce joueur s'il n'existe pas
                    }

                    UUID playerId = UUID.fromString(rs.getString("id"));

                    insertStmt.clearParameters();
                    insertStmt.setObject(1, UUID.randomUUID()); // id
                    insertStmt.setObject(2, playerId);           // player_id

                    if (pr.getRank() != null) {
                        insertStmt.setInt(3, pr.getRank());
                    } else {
                        insertStmt.setNull(3, Types.INTEGER);
                    }

                    insertStmt.setObject(4, championshipId);     // championship_id
                    insertStmt.setInt(5, pr.getScoredGoals());

                    if (pr.getPlayingTime().getId() != null) {
                        insertStmt.setObject(6, UUID.fromString(pr.getPlayingTime().getId()));
                    } else {
                        insertStmt.setNull(6, Types.OTHER);
                    }

                    insertStmt.setInt(7, seasonYear);

                    insertStmt.addBatch();
                }

                insertStmt.executeBatch();
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
