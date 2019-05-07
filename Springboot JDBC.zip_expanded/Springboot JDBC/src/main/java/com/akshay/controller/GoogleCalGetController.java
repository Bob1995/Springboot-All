package com.atmecs.sapho.controller;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar.Events;
import com.google.api.services.calendar.CalendarScopes;

@RestController("/login/google")
public class GoogleCalGetController {

	private static final String APPLICATION_NAME = "Sceduled Events";
	private static HttpTransport httpTransport;
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static com.google.api.services.calendar.Calendar client;
	ObjectMapper objectMapper;
	GoogleClientSecrets clientSecrets;
	GoogleAuthorizationCodeFlow flow;
	Credential credential;

	@Value("${google.client.client-id}")
	private String clientId;

	@Value("${google.client.client-secret}")
	private String clientSecret;

	@Value("${google.client.redirectUriGet}")
	private String redirectURI;

	final DateTime currentDate = new DateTime(
			Date.from((LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())));
	final DateTime previousDate = new DateTime(
			Date.from((LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant())));

	@RequestMapping(value = "/login/google", method = RequestMethod.GET)
	public RedirectView googleConnectionStatus(HttpServletRequest request) throws Exception {
		return new RedirectView(authorize());
	}

	private String authorize() throws Exception {
		AuthorizationCodeRequestUrl authorizationUrl;
		if (flow == null) {
			Details web = new Details();
			web.setClientId(clientId);
			web.setClientSecret(clientSecret);
			clientSecrets = new GoogleClientSecrets().setWeb(web);
			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
					Collections.singleton(CalendarScopes.CALENDAR)).build();
		}
		authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectURI);
		return authorizationUrl.build();
	}

	@RequestMapping(value = "/login/google", method = RequestMethod.GET, params = "code")
	public ResponseEntity<String> oauth2Callback(@RequestParam(value = "code") String code) {
		com.google.api.services.calendar.model.Events eventList;
		String eventResponse;
		try {
			TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
			credential = flow.createAndStoreCredential(response, "userID");
			client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
					.setApplicationName(APPLICATION_NAME).build();
	
			Events events = client.events();
			// eventList =
			// events.list("primary").setTimeMin(previousDate)..setTimeMax(currentDate).execute();
			eventList = events.list("primary").setUpdatedMin(previousDate).execute();
			eventResponse = eventList.getItems().toString();

			objectMapper = new ObjectMapper();
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
			Object obj = null;
			obj = objectMapper.readValue(eventResponse, Object.class);
			System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
			FileWriter fw = new FileWriter(
					"C:\\JAVA\\SYNERGY.zip_expanded\\SYNERGY\\src\\main\\resources\\GoogleApiResponses\\GetEvents.json");
			fw.write(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
			fw.close();

		} catch (Exception e) {
			eventResponse = "Exception while handling OAuth2 callback (" + e.getMessage() + ")."
					+ " Redirecting to google connection status page.";
		}
		return new ResponseEntity<>(eventResponse, HttpStatus.OK);
	}
}