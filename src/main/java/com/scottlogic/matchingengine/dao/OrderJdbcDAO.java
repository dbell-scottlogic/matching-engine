package com.scottlogic.matchingengine.dao;

import com.scottlogic.matchingengine.entities.Account;
import com.scottlogic.matchingengine.entities.Action;
import com.scottlogic.matchingengine.entities.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrderJdbcDAO implements DAO<Order>{

    private static final Logger log = LoggerFactory.getLogger(OrderJdbcDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    AccountJdbcDAO accountJdbcDAO;

    RowMapper<Order> rowMapper = (rs, rowNum) -> {
        Order order = new Order();
        order.setOrderId(rs.getInt("orderId"));
        order.setSize(rs.getInt("size"));
        order.setAction(Action.valueOf(rs.getString("action")));
        order.setAccount(accountJdbcDAO.read(rs.getString("username")).get());
        order.setPrice(rs.getInt("price"));
        order.setTimestamp(rs.getTimestamp("time"));
        return order;
    };

    public OrderJdbcDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountJdbcDAO = new AccountJdbcDAO(jdbcTemplate);
    }

    public OrderJdbcDAO() {

    }

    @Override
    public List<Order> list() {
        String sql = "SELECT * FROM ORDERS;";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public void create(Order order) {
        String sql ="INSERT INTO ORDERS (size, price, action, username, time) VALUES (?, ?, ?, ? ,?);";
        jdbcTemplate.update(sql, order.getSize(), order.getPrice(), order.getAction().toString(), order.getAccount().getUsername(), order.getTimestamp());
    }

    @Override
    public Optional<Order> read(String id) {
        return Optional.empty();
    }

    @Override
    public void update(Order order, String id) {

    }

    @Override
    public void delete(int id) {

    }
}
