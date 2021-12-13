package com.scottlogic.matchingengine;

import com.scottlogic.matchingengine.entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

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
		assertEquals("buyList is of length 1", 1, matcher.buyMap.size());
	}
	@Test
	void sellOrderIntoSellList(){
		Order order = new Order(1, 20, 10, Action.SELL, sellAccount, new Timestamp(date.getTime()));
		matcher.processOrder(order);
		assertEquals("sellList is of length 1", 1,matcher.sellMap.size());
	}

	@Test
	void tradeShouldOccur(){
		Order buyOrder = new Order(1, 20, 10, Action.BUY, buyAccount, new Timestamp(date.getTime()));
		Order sellOrder = new Order(2, 19, 10, Action.SELL, sellAccount, new Timestamp(date.getTime()));
		matcher.processOrder(buyOrder);
		matcher.processOrder(sellOrder);
		assertEquals("Trade List of length 1", 1, matcher.tradeList.size());
	}

	@Test
	void tradeShouldOccurPriceHigher(){
		Order buyOrder = new Order(1, 20, 10, Action.BUY, buyAccount, new Timestamp(date.getTime()));
		Order sellOrder = new Order(2, 19, 9, Action.SELL, sellAccount, new Timestamp(date.getTime()));
		matcher.processOrder(buyOrder);
		matcher.processOrder(sellOrder);
		assertEquals("Trade List of length 1", 1, matcher.tradeList.size());
	}

	@Test
	void tradeShouldNotOccur(){
		Order buyOrder = new Order(1, 20, 9, Action.BUY, buyAccount, new Timestamp(date.getTime()));
		Order sellOrder = new Order(2, 20, 10, Action.SELL, sellAccount, new Timestamp(date.getTime()));
		matcher.processOrder(buyOrder);
		matcher.processOrder(sellOrder);
		assertEquals("Trade List of length 0", 0, matcher.tradeList.size());
	}

	@Test
	void tradeShouldOccurRemainingBuySizeLeftOver(){
		Order buyOrder = new Order(1, 25, 10, Action.BUY, buyAccount, new Timestamp(date.getTime()));
		Order sellOrder = new Order(2, 20, 10, Action.SELL, sellAccount, new Timestamp(date.getTime()));
		matcher.processOrder(sellOrder);
		matcher.processOrder(buyOrder);
		Order expectedBuyOrder = new Order(1, 5, 10, Action.BUY, buyAccount, new Timestamp(date.getTime()));
		assertTrue("Buy list has size 5 left over at price 10", matcher.buyMap.containsValue(expectedBuyOrder));
		assertTrue("Sell list is empty", matcher.sellMap.isEmpty());
		assertEquals("Buy map has length of 1", 1, matcher.buyMap.size());
		assertEquals("Trade list should have length 1", 1, matcher.tradeList.size());

	}

	@Test
	void tradeShouldOccurRemainingSellSizeLeftOver(){
		Timestamp globalTimestamp = new Timestamp(date.getTime());
		Order buyOrder = new Order(1, 15, 10, Action.BUY, buyAccount, globalTimestamp);
		Order sellOrder = new Order(2, 24, 10, Action.SELL, sellAccount, globalTimestamp);

		matcher.processOrder(sellOrder);
		matcher.processOrder(buyOrder);
		assertTrue("Buy  map is empty as it is consumed", matcher.buyMap.isEmpty());

		Order expectedSellOrder = new Order(2, 9, 10, Action.SELL, sellAccount, globalTimestamp);
		assertTrue("Sell list has size 5 left over at price 10", matcher.sellMap.containsValue(expectedSellOrder));
		assertTrue("Buy list is empty", matcher.buyMap.isEmpty());
		assertEquals("Buy sell has length of 1", 1, matcher.sellMap.size());
		assertEquals("Trade list should have length 1", 1, matcher.tradeList.size());
	}


	@Test
	void mapShouldAggregate(){
		Order order1 = new Order(1, 20, 9, Action.BUY, new Account(1, "Test"), new Timestamp(date.getTime()));
		Order order2 = new Order(2, 15, 10, Action.BUY, new Account(2, "Test"), new Timestamp(date.getTime()));
		Order order3 = new Order(3, 13, 9, Action.BUY, new Account(3, "Test"), new Timestamp(date.getTime()));
		Order order4 = new Order(4, 13, 9, Action.BUY, new Account(4, "Test"), new Timestamp(date.getTime()));

		HashMap<Account, Order> temporaryMap = new HashMap<>();
		temporaryMap.put(order1.getAccount(), order1);
		temporaryMap.put(order2.getAccount(), order2);
		temporaryMap.put(order3.getAccount(), order3);
		temporaryMap.put(order4.getAccount(), order4);

		ArrayList<Order> orders = matcher.aggregateMap(temporaryMap);
		assertEquals("Agg Buy List should be of length 2", 2, orders.size());
	}

	@Test
	void mapShouldCumulate(){
		Timestamp globalTimestamp = new Timestamp(new Date().getTime());
		Account globalAccount = new Account(1, "Test");
		Order order1 = new Order(1, 14, 9, Action.BUY, new Account(1, "Test"), globalTimestamp);
		Order order2 = new Order(2, 12, 10, Action.BUY, new Account(1, "Test"), globalTimestamp);
		Order order3 = new Order(3, 13, 9, Action.BUY, new Account(1, "Test"), globalTimestamp);
		Order order4 = new Order(4, 10, 9, Action.BUY, new Account(1, "Test"), globalTimestamp);

		ArrayList<Order> temporaryList = new ArrayList<>();
		temporaryList.add(order1);
		temporaryList.add(order2);
		temporaryList.add(order3);
		temporaryList.add(order4);

		CumulateOrder expOrder1 = new CumulateOrder(49, 9, Action.BUY);
		CumulateOrder expOrder2 = new CumulateOrder(22, 10, Action.BUY);
		CumulateOrder expOrder3 = new CumulateOrder(35, 9, Action.BUY );
		CumulateOrder expOrder4 = new CumulateOrder(10, 9, Action.BUY );

		ArrayList<CumulateOrder> expectedList = new ArrayList<>();
		expectedList.add(expOrder4);
		expectedList.add(expOrder2);
		expectedList.add(expOrder3);
		expectedList.add(expOrder1);

		ArrayList<CumulateOrder> cumulatedList = matcher.cumulateList(temporaryList);
		assertTrue("Temp map contains cumulative values as defined", cumulatedList.containsAll(expectedList));

	}


}
