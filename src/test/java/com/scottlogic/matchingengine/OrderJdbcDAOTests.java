//package com.scottlogic.matchingengine;
//
//import com.scottlogic.matchingengine.dao.AccountJdbcDAO;
//import com.scottlogic.matchingengine.dao.OrderJdbcDAO;
//import com.scottlogic.matchingengine.entities.Action;
//import com.scottlogic.matchingengine.entities.Order;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import java.sql.Timestamp;
//import java.util.Date;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest
//public class OrderJdbcDAOTests {
//
//    @Autowired
//    JdbcTemplate jdbcTemplate = new JdbcTemplate();
//
//    @Autowired
//    OrderJdbcDAO orderJdbcDAO = new OrderJdbcDAO(jdbcTemplate);
//
//
//    @Autowired
//    AccountJdbcDAO accountJdbcDAO = new AccountJdbcDAO(jdbcTemplate);
//
//    @Test
//    public void shouldCreateOrder(){
//        Order order = new Order();
//        order.setPrice(1);
//        order.setSize(3);
//        order.setAction(Action.BUY);
//        order.setTimestamp(new Timestamp(new Date().getTime()));
//        order.setAccount(accountJdbcDAO.read("David97").get());
//
//        orderJdbcDAO.create(order);
//
//        Order order2 = new Order();
//        order2.setPrice(10);
//        order2.setSize(5);
//        order2.setAction(Action.BUY);
//        order2.setTimestamp(new Timestamp(new Date().getTime()));
//        order2.setAccount(accountJdbcDAO.read("David97").get());
//
//        orderJdbcDAO.create(order2);
//
//        assertEquals(2, orderJdbcDAO.list().size());
//        System.out.println(orderJdbcDAO.list());
//
//    }
//}
