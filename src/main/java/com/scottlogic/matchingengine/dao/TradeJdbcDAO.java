package com.scottlogic.matchingengine.dao;

import com.scottlogic.matchingengine.entities.Trade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Component
public class TradeJdbcDAO implements DAO<Trade>{

    private static final Logger log = LoggerFactory.getLogger(TradeJdbcDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public TradeJdbcDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TradeJdbcDAO(){

    }

    RowMapper<Trade> rowMapper = (rs, rowNum) -> {
        Trade trade = new Trade();
        trade.setTradeId(rs.getInt(1));
        trade.setPrice(rs.getInt(2));
        trade.setBuyOrderId(rs.getInt(3));
        trade.setSellOrderId(rs.getInt(4));
        trade.setBuyAccountUsername(rs.getString(5));
        trade.setSellAccountUsername(rs.getString(6));
        trade.setTimestamp(Timestamp.valueOf(rs.getString(7)));
        return trade;
    };

    @Override
    public List<Trade> list() {
        String sql = "SELECT * from TRADES";
        log.info("Listing trades");
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void create(Trade trade) {
        System.out.println(trade);
        String sql = "INSERT INTO TRADES ( price,  buyOrderId, sellOrderId, buyAccountUsername, sellAccountUsername, time) VALUES (?,?,?,?,?,?);";
        System.out.println(jdbcTemplate.getDataSource());
        //jdbcTemplate.update(sql,trade.getPrice(), trade.getBuyOrderId(), trade.getSellOrderId(), trade.getBuyAccountUsername(), trade.getSellAccountUsername(), trade.getTimestamp());
        log.info("New trade created: " + trade.getTradeId());
    }

    @Override
    public Optional<Trade> read(String id) {
        String sql = "SELECT * FROM TRADES WHERE tradeId = ?;";
        Trade trade = null;
        try{
            trade = jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
        } catch (DataAccessException ex){
            log.info("Trade not found");
        }
        return Optional.ofNullable(trade);
    }

    @Override
    public void update(Trade trade, String id) {

    }

    @Override
    public void delete(int id) {

    }
}
