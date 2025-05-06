package com.hei.fifa_gestion_central.DAO;

import com.hei.fifa_gestion_central.DTO.ClubDTO;
import com.hei.fifa_gestion_central.DTO.PlayerDTO;
import com.hei.fifa_gestion_central.Entity.PlayerRanking;

import java.util.List;

public interface SynchronizeDAO {
    void savePlayerRankings(List<PlayerRanking> playerRankings);
    void saveClubs(List<ClubDTO> clubs);

    void savePlayers(List<PlayerDTO> players);
}
