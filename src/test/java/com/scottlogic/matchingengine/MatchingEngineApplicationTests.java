//package com.scottlogic.matchingengine;
//
//import com.scottlogic.matchingengine.entities.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.Date;
//
//import static org.springframework.test.util.AssertionErrors.assertEquals;
//import static org.springframework.test.util.AssertionErrors.assertTrue;
//
//@SpringBootTest
//class MatchingEngineApplicationTests {
//
//	private Matcher matcher;
//	private Account buyAccount;
//	private Account sellAccount;
//	private Date date;
//
//	@Test
//	void contextLoads() {
//	}
//
//	@BeforeEach
//	public void initEach(){
//		matcher = new Matcher();
//		buyAccount = new Account(1, "Test Buy Account");
//		sellAccount = new Account(2, "Test Sell Account");
//		date = new Date();
//	}
//
//	@Test
//	void buyOrderIntoBuyList(){
//		Order order = new Order(1, 20, 10, Action.BUY, 1, new Timestamp(date.getTime()));
//		matcher.processOrder(order);
//		assertEquals("buyList is of length 1", 1, matcher.buyList.size());
//	}
//	@Test
//	void sellOrderIntoSellList(){
//		Order order = new Order(1, 20, 10, Action.SELL, 1, new Timestamp(date.getTime()));
//		matcher.processOrder(order);
//		assertEquals("sellList is of length 1", 1,matcher.sellList.size());
//	}
//
//	@Test
//	void tradeShouldOccur(){
//		Order buyOrder = new Order(1, 20, 10, Action.BUY, 1, new Timestamp(date.getTime()));
//		Order sellOrder = new Order(2, 19, 10, Action.SELL, 1, new Timestamp(date.getTime()));
//		matcher.processOrder(buyOrder);
//		matcher.processOrder(sellOrder);
//
//		assertEquals("Trade List of length 1", 1, matcher.tradeList.size());
//
//	}
//
//	@Test
//	void tradeShouldOccurPriceHigher(){
//		Order buyOrder = new Order(1, 20, 10, Action.BUY, 1, new Timestamp(date.getTime()));
//		Order sellOrder = new Order(2, 19, 9, Action.SELL, 1, new Timestamp(date.getTime()));
//		matcher.processOrder(buyOrder);
//		matcher.processOrder(sellOrder);
//		assertEquals("Trade List of length 1", 1, matcher.tradeList.size());
//	}
//
//	@Test
//	void tradeShouldNotOccur(){
//		Order buyOrder = new Order(1, 20, 9, Action.BUY, 1, new Timestamp(date.getTime()));
//		Order sellOrder = new Order(2, 20, 10, Action.SELL, 1, new Timestamp(date.getTime()));
//		matcher.processOrder(buyOrder);
//		matcher.processOrder(sellOrder);
//		assertEquals("Trade List of length 0", 0, matcher.tradeList.size());
//	}
//
//	@Test
//	void tradeShouldOccurRemainingBuySizeLeftOver(){
//		Order buyOrder = new Order(1, 25, 10, Action.BUY, 1, new Timestamp(date.getTime()));
//		Order sellOrder = new Order(2, 20, 10, Action.SELL, 1, new Timestamp(date.getTime()));
//		matcher.processOrder(sellOrder);
//		matcher.processOrder(buyOrder);
//		Order expectedBuyOrder = new Order(1, 5, 10, Action.BUY, 1, new Timestamp(date.getTime()));
//		assertTrue("Buy list has size 5 left over at price 10", matcher.buyList.contains(expectedBuyOrder));
//		assertTrue("Sell list is empty", matcher.sellList.isEmpty());
//		assertEquals("Buy map has length of 1", 1, matcher.buyList.size());
//		assertEquals("Trade list should have length 1", 1, matcher.tradeList.size());
//
//	}
//
//	@Test
//	void aggSellListShouldReduce(){
//		Order order1 = new Order(1, 10, 10, Action.SELL,1, new Timestamp(date.getTime()));
//		Order order2 = new Order(2, 10, 10, Action.SELL,1, new Timestamp(date.getTime()));
//		Order order3 = new Order(3, 10, 10, Action.SELL,1, new Timestamp(date.getTime()));
//
//		matcher.processOrder(order1);
//		matcher.processOrder(order2);
//		matcher.processOrder(order3);
//
//		Order buyOrder = new Order(4, 10, 10, Action.BUY, 1, new Timestamp(date.getTime()));
//		matcher.processOrder(buyOrder);
//		Order expectedOrder = new Order(20, 10, Action.SELL, 1, null);
//		assertTrue("Agg sell list contains size 20 price 10", matcher.aggSellList.contains(expectedOrder));
//	}
//
//	@Test
//	void aggBuyListShouldReduce(){
//		Order order1 = new Order(1, 10, 10, Action.BUY,1, new Timestamp(date.getTime()));
//		Order order2 = new Order(2, 10, 10, Action.BUY,1, new Timestamp(date.getTime()));
//		Order order3 = new Order(3, 10, 10, Action.BUY,1, new Timestamp(date.getTime()));
//
//		matcher.processOrder(order1);
//		matcher.processOrder(order2);
//		matcher.processOrder(order3);
//
//		Order sellOrder = new Order(4, 10, 10, Action.SELL,1, new Timestamp(date.getTime()));
//		matcher.processOrder(sellOrder);
//		Order expectedOrder = new Order(20, 10, Action.BUY, 1, null);
//		assertTrue("Agg buy list contains size 20 price 10", matcher.aggBuyList.contains(expectedOrder));
//	}
//
//	@Test
//	void tradeShouldOccurRemainingSellSizeLeftOver(){
//		Timestamp globalTimestamp = new Timestamp(date.getTime());
//		Order buyOrder = new Order(1, 15, 10, Action.BUY, 1, globalTimestamp);
//		Order sellOrder = new Order(2, 24, 10, Action.SELL, 1, globalTimestamp);
//
//		matcher.processOrder(sellOrder);
//		matcher.processOrder(buyOrder);
//		assertTrue("Buy  map is empty as it is consumed", matcher.buyList.isEmpty());
//
//		Order expectedSellOrder = new Order(2, 9, 10, Action.SELL, 1, globalTimestamp);
//		assertTrue("Sell list has size 5 left over at price 10", matcher.sellList.contains(expectedSellOrder));
//		assertTrue("Buy list is empty", matcher.buyList.isEmpty());
//		assertEquals("Buy sell has length of 1", 1, matcher.sellList.size());
//		assertEquals("Trade list should have length 1", 1, matcher.tradeList.size());
//	}
//
//
//	@Test
//	void mapShouldAggregate(){
//		Order order1 = new Order(1, 20, 9, Action.BUY, 1, new Timestamp(date.getTime()));
//		Order order2 = new Order(2, 15, 10, Action.BUY, 1, new Timestamp(date.getTime()));
//		Order order3 = new Order(3, 13, 9, Action.BUY,1, new Timestamp(date.getTime()));
//		Order order4 = new Order(4, 13, 9, Action.BUY, 1, new Timestamp(date.getTime()));
//		Order order5 = new Order(5, 20, 12, Action.BUY,1, new Timestamp(date.getTime()));
//
//		ArrayList<Order> temporaryMap = new ArrayList<>();
//		temporaryMap.add(order1);
//		temporaryMap.add(order2);
//		temporaryMap.add(order3);
//		temporaryMap.add(order4);
//		temporaryMap.add(order5);
//
//		ArrayList<Order> expectedList = new ArrayList<>();
//		Order expOrder1 = new Order(0,46, 9, Action.BUY, 1, null);
//		Order expOrder2 = new Order(0,15, 10, Action.BUY, 1, null);
//		Order expOrder3 = new Order(0,20, 12, Action.BUY, 1, null);
//		expectedList.add(expOrder1);
//		expectedList.add(expOrder2);
//		expectedList.add(expOrder3);
//
//		ArrayList<Order> orders = matcher.aggregateMap(temporaryMap);
//
//		//assertEquals("Agg Buy List should be of length 2", 2, orders.size());
//		assertTrue("Agg Buy list contains expected array list", orders.containsAll(expectedList));
//	}
//
//	@Test
//	void mapShouldAggregateTwo(){
//		Order order1 = new Order(1, 10, 5, Action.BUY, 1, new Timestamp(date.getTime()));
//		Order order2 = new Order(1, 10, 5, Action.BUY, 1, new Timestamp(date.getTime()));
//		Order order3 = new Order(1, 10, 5, Action.BUY, 1, new Timestamp(date.getTime()));
//		Order order4 = new Order(1, 10, 5, Action.BUY, 1, new Timestamp(date.getTime()));
//		Order order5 = new Order(1, 10, 5, Action.BUY, 1, new Timestamp(date.getTime()));
//
//		ArrayList<Order> temporaryMap = new ArrayList<>();
//		temporaryMap.add(order1);
//		temporaryMap.add(order2);
//		temporaryMap.add(order3);
//		temporaryMap.add(order4);
//		temporaryMap.add(order5);
//
//		ArrayList<Order> expectedList = new ArrayList<>();
//		Order expOrder1 = new Order(0,50, 5, Action.BUY, 1, null);
//
//		expectedList.add(expOrder1);
//
//		ArrayList<Order> orders = matcher.aggregateMap(temporaryMap);
//		assertEquals("Agg Buy List should be of length 1", 1, orders.size());
//		assertTrue("Agg Buy list contains expected array list", orders.containsAll(expectedList));
//	}
//
//
//	@Test
//	void mapShouldCumulate(){
//		Timestamp globalTimestamp = new Timestamp(new Date().getTime());
//		Account globalAccount = new Account(1, "Test");
//		Order order1 = new Order(1, 14, 9, Action.BUY, 1, globalTimestamp);
//		Order order2 = new Order(2, 12, 10, Action.BUY,1, globalTimestamp);
//		Order order3 = new Order(3, 13, 9, Action.BUY, 1, globalTimestamp);
//		Order order4 = new Order(4, 10, 9, Action.BUY, 1, globalTimestamp);
//
//		ArrayList<Order> temporaryList = new ArrayList<>();
//		temporaryList.add(order1);
//		temporaryList.add(order2);
//		temporaryList.add(order3);
//		temporaryList.add(order4);
//
//		CumulateOrder expOrder1 = new CumulateOrder(49, 9, Action.BUY);
//		CumulateOrder expOrder2 = new CumulateOrder(22, 10, Action.BUY);
//		CumulateOrder expOrder3 = new CumulateOrder(35, 9, Action.BUY );
//		CumulateOrder expOrder4 = new CumulateOrder(10, 9, Action.BUY );
//
//		ArrayList<CumulateOrder> expectedList = new ArrayList<>();
//		expectedList.add(expOrder4);
//		expectedList.add(expOrder2);
//		expectedList.add(expOrder3);
//		expectedList.add(expOrder1);
//
//		ArrayList<CumulateOrder> cumulatedList = matcher.cumulateList(temporaryList);
//		assertTrue("Temp map contains cumulative values as defined", cumulatedList.containsAll(expectedList));
//
//	}
//
//
//}
