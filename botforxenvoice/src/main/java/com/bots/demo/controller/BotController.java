package com.bots.demo.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class BotController {

	RestTemplate restTemplate = new RestTemplate();

	@RequestMapping(value = "/comman", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String comman(@RequestBody String json) {
		System.out.println(json);
		return json;
	}

	@RequestMapping(value = "/slackwebhookurl", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> slashComman(@RequestBody String json) {
		System.out.println(json);
		JSONObject jsonObject = new JSONObject(json);
		ResponseEntity<String> responseEntity;
		ResponseEntity<String> response;
		if (jsonObject.has("event")) {
			JSONObject jsonObject2 = jsonObject.getJSONObject("event");
			if (jsonObject2 != null) {
				String Url = "https://hooks.slack.com/services/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";
				String input = "{\"text\":\"Hello, How can i help you\"}";
				String input1 = "{\"text\":\"Hi, How can i help you\"}";
				String input2 = "{\"text\":\"Greate  how can i help u\"}";
				String input3 = "{\"text\":\"i didn't get u tell me once again\"}";
				String string = jsonObject2.getString("text");
				if (jsonObject2.has("client_msg_id")) {
					if ("hi".equals(string)) {
						response = restTemplate.postForEntity(Url, input, String.class);
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
		JSONObject message = messaging.getJSONObject(0).getJSONObject("message");
		JSONObject postback;
		if (message != null) {
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
		JSONObject payload = postback.getJSONObject("payload");
		String response = "";
		if (payload != null) {
			response = "{ \"text\": \"Thanks!\"}";
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
					|| message.getString("text").equalsIgnoreCase("help.")
					|| message.getString("text").equalsIgnoreCase("info")) {
				response = "Do you need any help?";

			} else if (message.getString("text").equalsIgnoreCase("Get Started")) {
				response = "Welcome to xenvoice, How can i help you";
			}
			else if (message.getString("text").equalsIgnoreCase("yes")) {
				response = "How can i help you";
			}
			else if (message.getString("text").equalsIgnoreCase("no") || message.getString("text").equalsIgnoreCase("ok")) {
				response = "Thanks for chatting with us have a nice day";
			}
			else if (message.getString("text").equalsIgnoreCase("goodbye")) {
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
			} else {
				response = "I didn't get that. Can you say it again? or wait for a moment supporting team will respond to you.";
			}
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
		System.out.println(request_body);
		String Url = "https://graph.facebook.com/v3.1/me/messages?access_token=EAAfCnuEmQJcBAH5FXMqIdixD6QOLMl4jeXTkyKpfQHZAsr9jnqy559zZBWsWNBlegJ1so7nkBTMD7zAv3a3oD0ig8o9smuZBHlauRICIgcHqpTYL7TVGg0p5bkBgGN8RP5ZAtvvO1ClNwfT7VW1lBiB3kP55jhZADPLiXiarFsAZDZD";
		HttpEntity<String> httpEntity = new HttpEntity<String>(request_body, headers);
		ResponseEntity<String> response1 = restTemplate.postForEntity(Url, httpEntity, String.class);
		if (response1 != null) {
			System.out.println("message sent!");
			return response1;
		} else {
			System.out.println("Unable to send message:");
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
				return hubchallenge;
			} else {
				return null;
			}
		}
		return null;
	}
}
