package com.bots.demo.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author sekharv
 *
 */
@CrossOrigin("*")
@RestController
public class BotController {
	
	private static final Logger logger = LoggerFactory.getLogger(BotController.class);
		
	RestTemplate restTemplate = new RestTemplate();
	
	@CrossOrigin("*")
	@RequestMapping(value = "/comman", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String comman(@RequestBody String json) {
		System.out.println(json);
		logger.info(json);
		return "success";
		
	}
	
	@CrossOrigin("*")
	@RequestMapping(value = "/incoming", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> sendMessage(@RequestBody String json) {
		System.out.println(json);
		logger.info(json);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Basic ZGlyZWN0dGVzdDpmZDliMmE1NC00Njg1LTQxOGQtODdjZi0xZWRkM2JhZDY5Yzk=");
		String Url = "https://api.kik.com/v1/message";
		String request_body = "{\"messages\":[{\"body\":\"Rhuurjthjf\",\"to\":\"sekharvtest\",\"type\":\"text\",\"chatId\":\"db7c0de068c4bc52f5d256580005d0e6d9b2dc39d769ef0ab5ecd5fcc806d848\"}]}";
		
		HttpEntity<String> httpEntity = new HttpEntity<String>(request_body, headers);
		ResponseEntity<String> response1 = restTemplate.postForEntity(Url, httpEntity, String.class);
		
		return response1;
		
	}

	@RequestMapping(value = "/slackwebhookurl", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> slashComman(@RequestBody String json) {
		System.out.println(json);
		JSONObject jsonObject = new JSONObject(json);
		ResponseEntity<String> responseEntity;
		ResponseEntity<String> response;
		if (jsonObject.has("event")) {
			JSONObject jsonObject2 = jsonObject.getJSONObject("event");
			logger.info("event");
			if (jsonObject2 != null) {
				//XenvoiceBot
				//String Url = "https://hooks.slack.com/services/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";
				
				//eng test QA
				String Url = "https://hooks.slack.com/services/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";
				String input = "{\"text\":\"Hello, How can i help you\"}";
				String input1 = "{\"text\":\"Hi, How can i help you\"}";
				String input2 = "{\"text\":\"Greate  how can i help u\"}";
				String input3 = "{\"text\":\"i didn't get u tell me once again\"}";
				String string = jsonObject2.getString("text");
				if (jsonObject2.has("client_msg_id")) {
					if ("hi".equals(string)) {
						response = restTemplate.postForEntity(Url, input, String.class);
						logger.info("event");
						return response;
					}
					if ("hello".equals(string)) {
						response = restTemplate.postForEntity(Url, input1, String.class);
						return response;
					}
					if ("help".equals(string)) {
						response = restTemplate.postForEntity(Url, input2, String.class);
						return response;
					} else {

						response = restTemplate.postForEntity(Url, input3, String.class);
						return response;
					}
				} else {
					responseEntity = new ResponseEntity<>(json, HttpStatus.OK);
					return responseEntity;
				}
			} else {
				responseEntity = new ResponseEntity<>(json, HttpStatus.OK);
				return responseEntity;
			}
		} else {
			responseEntity = new ResponseEntity<>(json, HttpStatus.OK);
			return responseEntity;
		}

	}

	@RequestMapping(value = "/facebookwebhookurl", method = RequestMethod.POST)
	public String webhookUrl(@RequestBody String json) {
		System.out.println(json);
		JSONObject jsonObject = new JSONObject(json);

		JSONObject entry = jsonObject.getJSONArray("entry").getJSONObject(0);
		JSONArray messaging = entry.getJSONArray("messaging");
		JSONObject sender = messaging.getJSONObject(0).getJSONObject("sender");
		System.out.println(sender.getString("id"));
		JSONObject postback;
		JSONObject message;
		//if (message != null) {
		if (messaging.getJSONObject(0).has("message")) {
		    message = messaging.getJSONObject(0).getJSONObject("message");
			handleMessage(sender.getString("id"), message);
		} else if (messaging.getJSONObject(0).has("postback")) {
			postback = messaging.getJSONObject(0).getJSONObject("postback");
			handlePostback(sender.getString("id"), postback);
		}

		String string = jsonObject.getString("object");
		if (string.equals("page")) {
			return "EVENT_RECEIVED";
		}
		return null;
	}

	private void handlePostback(String senderid, JSONObject postback) {
		JSONObject payload = postback;;
		String response = "";
		if (payload != null) {
			response = "Welcome to xenvoice!";
		} else if (payload.equals(null)) {
			response = "{ \"text\": \"Oops, try sending another image.\"}";
		}
		callSendAPI(senderid, response);
	}

	private void handleMessage(String sender_psid, JSONObject message) {
		String response = "";
		if (message.getString("text") != null) {
			if (message.getString("text").equalsIgnoreCase("hi")) {
				response = "Hi, how can we help you today?";
			} else if (message.getString("text").equalsIgnoreCase("hello")) {
				response = "Hi,How can i help you";
			} else if (message.getString("text").equalsIgnoreCase("help")
					|| message.getString("text").equalsIgnoreCase("help1")
					|| message.getString("text").equalsIgnoreCase("help2")
					|| message.getString("text").equalsIgnoreCase("help.")) {
				response = "Do you need any help?";
				logger.info(response);
				
			} else if (message.getString("text").equalsIgnoreCase("Get Started")
					|| message.getString("text").equalsIgnoreCase("started over")
					|| message.getString("text").equalsIgnoreCase("start")) {
				response = "Welcome to xenvoice, How can i help you";
			} else if (message.getString("text").equalsIgnoreCase("yes")) {
				response = "How can i help you";
			} else if (message.getString("text").equalsIgnoreCase("commands")) {
				response = "Basic commands: Hi,Hello,Help,Start,what is xenvoice";
			} else if (message.getString("text").equalsIgnoreCase("menu")) {
				response = "Sorry, Menu not found Can you mention your query in detail";
			} else if (message.getString("text").equalsIgnoreCase("no")
					|| message.getString("text").equalsIgnoreCase("ok")) {
				response = "Thanks for chatting with us have a nice day";
			} else if (message.getString("text").equalsIgnoreCase("goodbye")) {
				response = "Thanks for chatting with us.";
			} else if (message.getString("text").equalsIgnoreCase("problem")) {
				response = "I understand the problem and will be happy to help you.";
			} else if (message.getString("text").equalsIgnoreCase("query")) {
				response = "Can you mention your query in detail?";
			} else if (message.getString("text").equalsIgnoreCase("requirements")) {
				response = "May I know what exactly your requirements are?";
			} else if (message.getString("text").equalsIgnoreCase("solution")) {
				response = "I am really sorry this has happened. Let me see if I can find a solution to it";
			} else if (message.getString("text").equalsIgnoreCase("thank you")
					|| message.getString("text").equalsIgnoreCase("thanku")) {
				response = "Thank you for chatting with us today. Have a nice day. Good bye";
			} else if (message.getString("text").equalsIgnoreCase("who are you")
					|| message.getString("text").equalsIgnoreCase("who r u")) {
				response = "This is xenvoice";
			} else if (message.getString("text").equalsIgnoreCase("what is xenvoice")
					|| message.getString("text").equalsIgnoreCase("what is xenvoice?")
					|| message.getString("text").equalsIgnoreCase("what is xenvoice ?")
					|| message.getString("text").equalsIgnoreCase("xenvoice ?")
					|| message.getString("text").equalsIgnoreCase("xenvoice?")
					|| message.getString("text").equalsIgnoreCase("xenvoice")
					|| message.getString("text").equalsIgnoreCase("info")) {
				response = "A business communication system that offers voice, text&FAX services.";
			} else if (message.getString("text").equalsIgnoreCase("more")) {
				response = "Shrink the borders of global workforce using our application. Enjoy world-class voice and text messaging services at a lowest ever prices.";
			} else {
				response = "I didn't get that. Can you say it again?";
			}
			logger.info(response);
		} /*
			 * else if(message.getString("attachments")!=null) {
			 * message.getJSONObject("attachments").getJSONObject("payload").getString("url"
			 * ); //received_message.attachments[0].payload.url; response =
			 * "{\"attachment\":{\"type\":\"template\",\"payload\":{\"template_type\":\"generic\",\"elements\":[{\"title\":\"Is this the right picture?\",\"subtitle\":\"Tap a button to answer.\",\"image_url\":\"attachment_url\",\"buttons\":[{\"type\":\"postback\",\"title\":\"Yes!\",\"payload\":\"yes\"},{\"type\":\"postback\",\"title\":\"No!\",\"payload\":\"no\"}]}]}}}"
			 * ; }
			 */
		callSendAPI(sender_psid, response);
	}

	private ResponseEntity<String> callSendAPI(String sender_psid, String response) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String request_body = "{\"recipient\":{\"id\":\"" + sender_psid + "\"},\"message\":{\"text\":\"" + response
				+ "\"}}";
		logger.info(request_body);
		System.out.println(request_body);
		
		// Jhon pall
		String Url = "https://graph.facebook.com/v3.1/me/messages?access_token=EAAQKgGmZCUJUBAAsxrmH0HEEY5PDZBouGNHSruZALLa2vXLnSOZCDIiR6EKbpt990MiTKCNnygTKnqYsA88oR5aL38xaclK0nPKb6nhFDMUQQvN4uzDV2euHMmAGK5uRPciKsd2JX8bdxDh2IxQTro69cyUceOO9csVbWR7GJQZDZD";
		//Ramharsh java_qa
		//String Url = "https://graph.facebook.com/v3.1/me/messages?access_token=EAADZCyOlKKp8BABtHZAcf3gDWaTC7hi0N8tyTRB5H0HclDBputLR8AHiYM86bveoMJFe7INyBy8FGFVkdM5zc5rqjtqheM8ZCYRarse2JpZAeV0YqYwoCXXXsBNJjnuxlbJThZC2VGiSJWUSIqpAbTzhloRlM21QI2C5dZAkFzAgZDZD";
		HttpEntity<String> httpEntity = new HttpEntity<String>(request_body, headers);
		ResponseEntity<String> response1 = restTemplate.postForEntity(Url, httpEntity, String.class);
		if (response1 != null) {
			System.out.println("message sent!");
			logger.info(response1.toString());
			return response1;
		} else {
			System.out.println("Unable to send message:");
			logger.info(response1.toString());
			return response1;
		}
	}

	@RequestMapping(value = "/facebookwebhookurl", method = RequestMethod.GET)
	public String verificationToken(@RequestParam("hub.mode") String hubmode,
			@RequestParam("hub.verify_token") String hubverify_token,
			@RequestParam("hub.challenge") String hubchallenge) {
		System.out.println("working verification");
		// token should be a random string.
		String token = "*123456789";
		if (hubmode != null && hubverify_token != null) {
			if (hubmode.equals("subscribe") && hubverify_token.equals(token)) {
				logger.info(hubchallenge);
				return hubchallenge;
			} else {
				return null;
			}
		}
		return null;
	}
}
