package com.hei.fifa_gestion_central.DAO;

import com.hei.fifa_gestion_central.Entity.PlayerRanking;

import java.util.List;

public interface SynchronizeDAO {
    void savePlayerRankings(List<PlayerRanking> playerRankings);
}
