package com.atmecs.sapho.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
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
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.api.services.calendar.CalendarScopes;

@RestController("/login/google")
public class EventsController {

	private static final String APPLICATION_NAME = "Sceduled Events";
	private static HttpTransport httpTransport;
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static com.google.api.services.calendar.Calendar client;
	
	GoogleClientSecrets clientSecrets;
	GoogleAuthorizationCodeFlow flow;
	Credential credential;

	@Value("${google.client.client-id}")
	private String clientId;

	@Value("${google.client.client-secret}")
	private String clientSecret;

	@Value("${google.client.redirectUriGet}")
	private String redirectURI;

	@Value("${google.client.redirectUriPost}")
	private String redirectURI1;
	
	@Value("${google.client.redirectUriRoomBooking}")
	private String redirectURI2;

	final DateTime currentDate = new DateTime(
			Date.from((LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())));
	final DateTime previousDate = new DateTime(
			Date.from((LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toInstant())));

	/*
	 * API: Authorization with gmail
	 */
	@RequestMapping(value = "/login/google", method = RequestMethod.GET)
	public RedirectView googleConnectionStatus(HttpServletRequest request) throws Exception {
		return new RedirectView(authorize());
	}

	/*
	 * Oauth2 Authorization With clientID,Client Secrete
	 */
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

	/*
	 * API:Get Event Details
	 */
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
			eventList = events.list("primary").setUpdatedMin(previousDate).execute();
			eventResponse = eventList.getItems().toString();
		
			
		} catch (Exception e) {
			eventResponse = "Exception while handling OAuth2 callback (" + e.getMessage() + ")."
					+ " Redirecting to google connection status page.";
		}
		return new ResponseEntity<>(eventResponse, HttpStatus.OK);
	}

	/*
	 * API:Creation of Events
	 */
	
	@RequestMapping(value = "/login/google/addEvents", method = RequestMethod.POST, params = "code")
	public ResponseEntity<String> oauth2Callback(@RequestParam(value = "code") String code, @RequestBody Event newEvent)
			throws IOException {

		String eventResponse;
		String calendarId = "primary";
		Event event = new Event();
		event.setSummary(newEvent.getSummary()).setLocation(newEvent.getLocation())
				.setDescription(newEvent.getDescription()).setAttendees(newEvent.getAttendees());

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

		EventReminder[] reminderOverrides = new EventReminder[] {
				new EventReminder().setMethod("email").setMinutes(24 * 60),
				new EventReminder().setMethod("popup").setMinutes(10), };
		Event.Reminders reminders = new Event.Reminders().setUseDefault(false)
				.setOverrides(Arrays.asList(reminderOverrides));
		event.setReminders(reminders);

		Event createdEvent = client.events().insert(calendarId, event).execute();
		System.out.printf("Event created successfully:");
		eventResponse = createdEvent.toString();


		return new ResponseEntity<>(eventResponse, HttpStatus.OK);

	}
		
	/*
	 * API:Book Your Room
	 */
	
	@RequestMapping(value = "/login/google/bookYourRoom", method = RequestMethod.POST, params = "code")
	public ResponseEntity<String> roomBooking(@RequestParam(value = "code") String code, @RequestBody Event newEvent)
			throws IOException {
		System.out.println("Book your room");
		System.out.println(newEvent);
		String eventResponse;
		String calendarId = "primary";
		/*
		 * Created Object of event class 
		 * 1.Added Summary,Location,Description
		 * 2.dates
		 * 3.Reminder
		 */
		Event event = new Event();
		event.setSummary(newEvent.getSummary()).setLocation(newEvent.getLocation())
				.setDescription(newEvent.getDescription()).setAttendees(newEvent.getAttendees());

		DateTime startDateTime = new DateTime(
				Date.from((LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant())));
		EventDateTime start = new EventDateTime().setDateTime(startDateTime).setTimeZone("America/Los_Angeles");
		event.setStart(start);

		DateTime endDateTime = new DateTime(
				Date.from((LocalDateTime.now().plusDays(2).atZone(ZoneId.systemDefault()).toInstant())));
		EventDateTime end = new EventDateTime().setDateTime(endDateTime).setTimeZone("America/Los_Angeles");
		event.setEnd(end);

		EventReminder[] reminderOverrides = new EventReminder[] {
				new EventReminder().setMethod("email").setMinutes(24 * 60),
				new EventReminder().setMethod("popup").setMinutes(10), };
		Event.Reminders reminders = new Event.Reminders().setUseDefault(false)
				.setOverrides(Arrays.asList(reminderOverrides));
		event.setReminders(reminders);

		Event createdEvent = client.events().insert(calendarId, event).execute();
		System.out.printf("Your room booked successfully:");
		eventResponse = createdEvent.toString();

		return new ResponseEntity<>(eventResponse, HttpStatus.OK);

	}
}