package com.bots.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Wit_itContoler {

	
	RestTemplate restTemplate = new RestTemplate();

	private static final Logger logger = LoggerFactory.getLogger(BotController.class);

	@RequestMapping(value = "getwitintents", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> hetWitIntents(@RequestParam String text) {
		String url = "https://api.wit.ai/message?q="+text;
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);		
		return response;
	}

}
