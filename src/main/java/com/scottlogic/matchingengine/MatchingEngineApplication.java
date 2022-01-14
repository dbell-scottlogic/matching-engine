package com.scottlogic.matchingengine;


import com.scottlogic.matchingengine.controllers.MatcherRestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class MatchingEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatchingEngineApplication.class, args);

//		try {
//			org.h2.tools.Server server = org.h2.tools.Server.createTcpServer().start();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		MatcherRestController matcherRestController = new MatcherRestController();

	}

}
