package com.scottlogic.matchingengine;

import com.scottlogic.matchingengine.models.Account;
import com.scottlogic.matchingengine.models.Action;
import com.scottlogic.matchingengine.models.Order;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Timestamp;
import java.util.Date;

@SpringBootApplication
public class MatchingEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatchingEngineApplication.class, args);

//		//Establishing
//		Account account = new Account(1, "David Bell");
//		Date date = new Date();
//		Order order = new Order(1, 20, 10, Action.BUY, account, new Timestamp(date.getTime()));
//		Matcher matcher = new Matcher();
//		matcher.processOrder(order);
//
//		System.out.println(matcher.buyList);
//
//		Account account2 = new Account(2, "Steve Jobs");
//		Date date2 = new Date();
//		Order order2 = new Order(2, 20, 10, Action.SELL, account2, new Timestamp(date2.getTime()));
//		matcher.processOrder(order2);
//		System.out.println(matcher.buyList);
////
//		System.out.println(matcher.buyList);
//
//		System.out.println(matcher.tradeList);


//		System.out.println(matcher.buyList);
//		System.out.println(matcher.sellList);
//		System.out.println(matcher.tradeList);

	}

}
