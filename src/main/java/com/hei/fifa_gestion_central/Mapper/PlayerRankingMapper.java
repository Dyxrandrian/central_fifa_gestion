package com.hei.fifa_gestion_central.Mapper;

import com.hei.fifa_gestion_central.Entity.PlayerRanking;
import com.hei.fifa_gestion_central.Entity.PlayingTime;
import com.hei.fifa_gestion_central.enums.Championship;
import com.hei.fifa_gestion_central.enums.DurationUnit;
import com.hei.fifa_gestion_central.enums.PlayerPosition;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerRankingMapper {
    public PlayerRanking map(ResultSet rs) throws SQLException {
        PlayerRanking player = new PlayerRanking();
        player.setId(rs.getString("player_id"));
        player.setName(rs.getString("name"));
        player.setPosition(PlayerPosition.valueOf(rs.getString("position")));
        player.setNationality(rs.getString("nationality"));
        player.setAge(rs.getInt("age"));
        player.setRank(rs.getInt("rank"));
        player.setScoredGoals(rs.getInt("scored_goals"));
        player.setChampionship(Championship.valueOf(rs.getString("championship")));

        PlayingTime time = new PlayingTime();
        time.setValue(rs.getDouble("playing_time_value"));
        time.setDurationUnit(DurationUnit.valueOf(rs.getString("duration_unit")));
        player.setPlayingTime(time);

        return player;
    }
}
