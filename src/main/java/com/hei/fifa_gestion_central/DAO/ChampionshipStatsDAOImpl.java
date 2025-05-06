package com.hei.fifa_gestion_central.DAO;

import com.hei.fifa_gestion_central.Mapper.ChampionshipMapper;
import com.hei.fifa_gestion_central.database.Datasource;
import com.hei.fifa_gestion_central.enums.Championship;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ChampionshipStatsDAOImpl implements ChampionshipStatsDAO {
    private final Datasource datasource;

    public ChampionshipStatsDAOImpl(Datasource datasource) {
        this.datasource = datasource;
    }

    @Override
    public Map<Championship, List<Integer>> getDifferenceGoalsByChampionship() {
        String query = """
            SELECT championship_id, difference_goals
            FROM club_ranking
            WHERE difference_goals IS NOT NULL
        """;

        Map<Championship, List<Integer>> result = new EnumMap<>(Championship.class);

        try (Connection connection = datasource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UUID championshipId = rs.getObject("championship_id", UUID.class);
                Integer diffGoals = rs.getInt("difference_goals");

                // Suppose que tu as une méthode utilitaire pour mapper UUID → enum Championship
                Championship championship = ChampionshipMapper.mapToEnum(championshipId);
                result.computeIfAbsent(championship, k -> new ArrayList<>()).add(diffGoals);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch difference goals by championship", e);
        }

        return result;
    }
}
