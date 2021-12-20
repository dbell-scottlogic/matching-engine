package com.scottlogic.matchingengine;


import com.scottlogic.matchingengine.controllers.MatcherRestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchingEngineApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatchingEngineApplication.class, args);

		MatcherRestController matcherRestController = new MatcherRestController();

	}

}
