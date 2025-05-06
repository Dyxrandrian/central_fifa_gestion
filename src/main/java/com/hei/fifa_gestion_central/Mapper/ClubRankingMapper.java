package com.hei.fifa_gestion_central.Mapper;

import com.hei.fifa_gestion_central.Entity.Club;
import com.hei.fifa_gestion_central.Entity.ClubRanking;
import com.hei.fifa_gestion_central.Entity.Coach;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClubRankingMapper {
    public ClubRanking map(ResultSet rs) throws SQLException {
        Coach coach = new Coach(
                rs.getString("coach_name"),
                rs.getString("coach_nationality")
        );

        Club club = new Club(
                rs.getString("club_id"),
                rs.getString("club_name"),
                rs.getString("acronym"),
                rs.getInt("year_creation"),
                rs.getString("stadium"),
                coach
        );

        return new ClubRanking(
                null, // rank will be computed separately
                club,
                rs.getInt("ranking_points"),
                rs.getInt("scored_goals"),
                rs.getInt("conceded_goals"),
                rs.getInt("difference_goals"),
                rs.getInt("clean_sheet_number")
        );
    }
}
