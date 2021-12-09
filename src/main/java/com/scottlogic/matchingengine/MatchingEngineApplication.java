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

	}

}
