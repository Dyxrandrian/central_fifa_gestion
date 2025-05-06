package com.hei.fifa_gestion_central.Service;

import com.hei.fifa_gestion_central.DAO.PlayerRankingDAOImpl;
import com.hei.fifa_gestion_central.Entity.PlayerRanking;
import com.hei.fifa_gestion_central.Entity.PlayerRankingProcessor;
import com.hei.fifa_gestion_central.enums.DurationUnit;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BestPlayerService {
    private final PlayerRankingDAOImpl playerRankingDao;
    private final PlayerRankingProcessor processor;

    public BestPlayerService(PlayerRankingDAOImpl playerRankingDao, PlayerRankingProcessor processor) {
        this.playerRankingDao = playerRankingDao;
        this.processor = processor;
    }

    public List<PlayerRanking> getBestPlayers(int top, DurationUnit unit) {
        List<PlayerRanking> rawRankings = playerRankingDao.findBestPlayers(top);
        return processor.process(rawRankings, unit, top);
    }
}
