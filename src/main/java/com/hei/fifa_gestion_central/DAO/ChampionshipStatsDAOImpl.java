package com.hei.fifa_gestion_central.DAO;

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
        SELECT ch.name AS championship_name, cr.difference_goals
        FROM club_ranking cr
        JOIN championship ch ON cr.championship_id = ch.id
        WHERE cr.difference_goals IS NOT NULL
    """;

        Map<Championship, List<Integer>> result = new EnumMap<>(Championship.class);

        try (Connection connection = datasource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String championshipName = rs.getString("championship_name");
                Championship championship = Championship.valueOf(championshipName); // direct mapping

                int diffGoals = rs.getInt("difference_goals");
                result.computeIfAbsent(championship, k -> new ArrayList<>()).add(diffGoals);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch difference goals by championship", e);
        }

        return result;
    }
}
