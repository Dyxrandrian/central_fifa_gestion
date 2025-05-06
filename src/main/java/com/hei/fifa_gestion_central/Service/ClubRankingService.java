package com.hei.fifa_gestion_central.Service;

import com.hei.fifa_gestion_central.DAO.ClubRankingDAOImpl;
import com.hei.fifa_gestion_central.Entity.ClubRanking;
import com.hei.fifa_gestion_central.Entity.ClubRankingProcessor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ClubRankingService {
    private final ClubRankingDAOImpl dao;
    private final ClubRankingProcessor processor;

    public ClubRankingService(ClubRankingDAOImpl dao, ClubRankingProcessor processor) {
        this.dao = dao;
        this.processor = processor;
    }

    public List<ClubRanking> getBestClubs(int top) throws SQLException {
        List<ClubRanking> all = dao.findAll();
        return processor.process(all, top);
    }
}
