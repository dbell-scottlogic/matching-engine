package com.scottlogic.matchingengine;

import com.scottlogic.matchingengine.models.Account;
import com.scottlogic.matchingengine.models.Action;
import com.scottlogic.matchingengine.models.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import java.sql.Timestamp;
import java.util.Date;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
class MatchingEngineApplicationTests {

	private Matcher matcher;
	private Account buyAccount;
	private Account sellAccount;
	private Date date;

	@Test
	void contextLoads() {
	}

	@BeforeEach
	public void initEach(){
		matcher = new Matcher();
		buyAccount = new Account(1, "Test Buy Account");
		sellAccount = new Account(2, "Test Sell Account");
		date = new Date();
	}

	@Test
	void buyOrderIntoBuyList(){
		Order order = new Order(1, 20, 10, Action.BUY, buyAccount, new Timestamp(date.getTime()));
		matcher.processOrder(order);
		assertEquals("buyList is of length 1", matcher.buyList.size(),1);
	}
	@Test
	void sellOrderIntoSellList(){
		Order order = new Order(1, 20, 10, Action.SELL, sellAccount, new Timestamp(date.getTime()));
		matcher.processOrder(order);
		assertEquals("sellList is of length 1", matcher.sellList.size(),1);
	}


}
