package com.ecse321.RideShare;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import com.ecse321.RideShare.*;
import com.ecse321.RideShare.controller.RideShareController;
import com.ecse321.RideShare.model.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RideShareApplicationTests {
	
	@Mock
	JdbcTemplate jdbcMock;
	
	@InjectMocks
	RideShareController controller;

	@Test
	public void checkUpdateUserRating() {
		User testUser = new User("first", "last", "email@email.com", "123-456-7890", "password", false);
		testUser.updateDriverRating(5);
		float expectedDriverRating = 4.5f;
		assertEquals(expectedDriverRating, testUser.getDriverRating(), 0.001f);
		
		testUser.updatePassengerRating(2);
		float expectedPassengerRating = 3f;
		assertEquals(expectedPassengerRating, testUser.getPassengerRating(), 0.001f);
	}
	
	@Test
	public void checkSetAndGetUserValues() {
		User testUser = new User("firstName", "lastName", "email1@email.com", "123-456-5430", "123abc", false);
		testUser.setPreferences("User Preferences");
		assertEquals("User Preferences", testUser.getPreferences());
	}
	
	@Test
	public void checkSetAndGetTripValues() {
		ArrayList<String> destinations = new ArrayList<String>();
		ArrayList<Float> tripDurations = new ArrayList<Float>();
		ArrayList<Float> prices = new ArrayList<Float>();
		ArrayList<Integer> passengerIDList = new ArrayList<Integer>();
		
		destinations.add("Test's Ville");
		tripDurations.add(3.5f);
		prices.add(20f);
		passengerIDList.add(1);
		
		Trip testTrip = new Trip(1, "driverEmail@email.com", "123-456-7890", "2018-01-10", "12:30:22", "Test City", destinations, 
			tripDurations, prices, 2, "Test Vehicle", "1A2B3C", "No Comments");
		String newComments = "New Comment";
		testTrip.setOtherComments(newComments);
		assertEquals(newComments, testTrip.getComments());
	}
	
	@Test
	public void checkValidDateFormat() {
		String date = "2018-02-12";
		assertTrue(RideShareController.isValidFormat(date, "yyyy-MM-dd"));
		
		date = "2018/02/12";
		assertFalse(RideShareController.isValidFormat(date, "yyyy-MM-dd"));
	}
	
	

}
