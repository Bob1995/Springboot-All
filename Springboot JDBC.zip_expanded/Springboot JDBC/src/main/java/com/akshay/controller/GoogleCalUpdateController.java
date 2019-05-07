package com.atmecs.sapho.controller;

import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
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
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;

@RestController("/login/updateEvents")
public class GoogleCalUpdateController {

	private static final String APPLICATION_NAME = "Update Event Schedule";
	private static HttpTransport httpTransport;
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static com.google.api.services.calendar.Calendar client;

	ObjectMapper objectMapper;
	Object obj;
	GoogleClientSecrets clientSecrets;
	GoogleAuthorizationCodeFlow flow;
	Credential credential;
	FileWriter fw;
	
	@Value("${google.client.client-id}")
	private String clientId;

	@Value("${google.client.client-secret}")
	private String clientSecret;

	@Value("${google.client.redirectUriUpdate}")
	private String redirectURI;

	@RequestMapping(value = "/login/updateEvents", method = RequestMethod.GET)
	public RedirectView googleConnectionStatus(HttpServletRequest request) throws Exception {
		System.out.println("Updated Events");
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

	@RequestMapping(value = "/login/updateEvents", method = RequestMethod.GET, params = "code")
	public ResponseEntity<String> oauth2Callback(@RequestParam(value = "code") String code) {
		String eventResponse;
		try {
			
			//Event details
			String event_summary="SAPO meeting";
			String event_location="American Eagle";
			String event_description="Discussion realted to connectors";
			String calendarId = "primary";
			String event_attendee1="a@gmail.com";
			String event_attendee2="b@gmail.com";
			String event_updatedMsg="Busy with demo";
			
			TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectURI).execute();

			credential = flow.createAndStoreCredential(response, "userID");

			client = new com.google.api.services.calendar.Calendar.Builder(httpTransport, JSON_FACTORY, credential)
					.setApplicationName(APPLICATION_NAME).build();

			/*
			 * Creation of event
			 */
			Event event = new Event();
			event.setSummary(event_summary).setLocation(event_location)
					.setDescription(event_description);

			DateTime startDateTime = new DateTime(
					Date.from((LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())));
			EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("America/Los_Angeles");
			event.setStart(start);

			DateTime endDateTime = new DateTime(
					Date.from((LocalDateTime.now().plusDays(2).atZone(ZoneId.systemDefault()).toInstant())));
			EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("America/Los_Angeles");
			event.setEnd(end);

			String[] recurrence = new String[] { "RRULE:FREQ=DAILY;COUNT=2" };
			event.setRecurrence(Arrays.asList(recurrence));

			EventAttendee[] attendees = new EventAttendee[] { new EventAttendee().setEmail(event_attendee1),
					new EventAttendee().setEmail(event_attendee2), };
			event.setAttendees(Arrays.asList(attendees));

			EventReminder[] reminderOverrides = new EventReminder[] {
					new EventReminder().setMethod("email").setMinutes(24 * 60),
					new EventReminder().setMethod("popup").setMinutes(10), };
			Event.Reminders reminders = new Event.Reminders().setUseDefault(false)
					.setOverrides(Arrays.asList(reminderOverrides));
			event.setReminders(reminders);

			
			event = client.events().insert(calendarId, event).execute();
			System.out.printf("Event created:");
			eventResponse = event.toString();

			/*
			 * Update Event
			 */
			event.setDescription(event_updatedMsg);
			Event updatedEvent = client.events().update(calendarId, event.getId(), event).execute();
			System.out.println(updatedEvent.getUpdated());
			eventResponse = updatedEvent.toString();
			System.out.println("Event Updated Successfully");
			
			objectMapper = new ObjectMapper();
			objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
			obj = objectMapper.readValue(eventResponse, Object.class);
			System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
			fw = new FileWriter(
					"C:\\JAVA\\SYNERGY.zip_expanded\\SYNERGY\\src\\main\\resources\\GoogleApiResponses\\UpdatedEvent.json");
			fw.write(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
			fw.close();

			
		} catch (Exception e) {
			eventResponse = "Exception while handling OAuth2 callback (" + e.getMessage() + ")."
					+ " Redirecting to google connection status page.";
		}
		return new ResponseEntity<>(eventResponse, HttpStatus.OK);

	}
}