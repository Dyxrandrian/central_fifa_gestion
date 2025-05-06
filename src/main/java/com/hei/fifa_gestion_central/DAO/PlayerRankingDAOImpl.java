package com.hei.fifa_gestion_central.DAO;

import com.hei.fifa_gestion_central.Mapper.PlayerRankingMapper;
import com.hei.fifa_gestion_central.database.Datasource;
import com.hei.fifa_gestion_central.Entity.PlayerRanking;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PlayerRankingDAOImpl {
    private final Datasource datasource;
    private final PlayerRankingMapper mapper;

    public PlayerRankingDAOImpl(Datasource datasource) {
        this.datasource = datasource;
        this.mapper = new PlayerRankingMapper();
    }

    public List<PlayerRanking> findBestPlayers(int top) {
        List<PlayerRanking> players = new ArrayList<>();

        String sql = """
            SELECT pr.rank,
                   p.id AS player_id,
                   p.name,
                   p.position,
                   p.nationality,
                   p.age,
                   c.name AS championship,
                   pr.scored_goals,
                   pt.value AS playing_time_value,
                   pt.duration_unit
            FROM player_ranking pr
            JOIN player p ON pr.player_id = p.id
            JOIN championship c ON pr.championship_id = c.id
            JOIN playing_time pt ON pr.playing_time_id = pt.id
            ORDER BY pr.scored_goals DESC, pt.value DESC
            LIMIT ?
        """;

        try (Connection conn = datasource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, top);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PlayerRanking player = mapper.map(rs);
                    players.add(player);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des meilleurs joueurs", e);
        }

        return players;
    }
}
