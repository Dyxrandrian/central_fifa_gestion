package com.hei.fifa_gestion_central.DAO;

import com.hei.fifa_gestion_central.DTO.ClubDTO;
import com.hei.fifa_gestion_central.DTO.PlayerDTO;
import com.hei.fifa_gestion_central.Entity.Club;
import com.hei.fifa_gestion_central.Entity.ClubRanking;
import com.hei.fifa_gestion_central.Entity.PlayerRanking;
import com.hei.fifa_gestion_central.enums.Championship;

import java.util.List;

public interface SynchronizeDAO {
    void saveClubs(List<ClubDTO> clubs);

    void savePlayers(List<PlayerDTO> players);

    void saveClubRankings(List<ClubRanking> clubRankings, Championship championship, int seasonYear);

    void savePlayerRankings(List<PlayerRanking> playerRankings, Championship championship, int seasonYear);
}
