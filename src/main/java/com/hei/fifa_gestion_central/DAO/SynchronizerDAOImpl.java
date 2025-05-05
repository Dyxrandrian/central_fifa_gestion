package com.hei.fifa_gestion_central.DAO;

import com.hei.fifa_gestion_central.Entity.PlayerRanking;
import com.hei.fifa_gestion_central.database.Datasource;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SynchronizerDAOImpl implements SynchronizeDAO {
    private final Datasource datasource;


    public SynchronizerDAOImpl(Datasource datasource) {
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

}
