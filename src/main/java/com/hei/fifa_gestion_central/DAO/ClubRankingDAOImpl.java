package com.hei.fifa_gestion_central.DAO;

import com.hei.fifa_gestion_central.Mapper.ClubRankingMapper;
import com.hei.fifa_gestion_central.database.Datasource;
import com.hei.fifa_gestion_central.Entity.ClubRanking;
import org.springframework.stereotype.Repository;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ClubRankingDAOImpl {
    private final Datasource dataSource;
    private final ClubRankingMapper mapper;

    public ClubRankingDAOImpl(Datasource dataSource) {
        this.dataSource = dataSource;
        this.mapper = new ClubRankingMapper();
    }

    public List<ClubRanking> findAll() throws SQLException {
        List<ClubRanking> result = new ArrayList<>();
        String sql = """
    SELECT cr.*, 
           c.id AS club_id, c.name AS club_name, c.acronym, c.year_creation, c.stadium,
           co.name AS coach_name, co.nationality AS coach_nationality
    FROM club_ranking cr
    JOIN club c ON cr.club_id = c.id
    LEFT JOIN coach co ON c.coach_id = co.id
""";


        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                result.add(mapper.map(rs));
            }
        }

        return result;
    }
}
