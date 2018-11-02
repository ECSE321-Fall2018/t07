package com.ecse321.RideShare.model;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ecse321.RideShare.RideShareService;

@ComponentScan("com.ecse321.RideShare.*")

public class User {
	
	@Autowired
	RideShareService service;
	
	JdbcTemplate jdbcTemplate;
	
	//JdbcTemplate jdbcTemplate;
	
	private static int idCounter = 1; //A counter that is incremented every time a User is created, so no two ID's are equal
	private final int userID; //User ID is final since it should never be changed
	private final Boolean isAdmin; //isAdmin is final since it should never be changed
	
	//All basic user information
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String password;
	private String preferences;
	
	//Ratings are assumed to be on a 0-5 system right now, they will start at 4. Total trips are needed to average rating
	private float passengerRating;
	private int totalPassengerTrips;
	private float driverRating;
	private int totalDriverTrips;
	
	
	//Class constructor for the user
	public User(String firstName, String lastName, String email, String phoneNumber, String password, Boolean isAdmin) {
		//Set all of the info for the user given the input
		userID = idCounter;
		idCounter++;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.isAdmin = isAdmin;
		this.preferences = "";
		this.passengerRating = 4.0f;
		this.driverRating = 4.0f;
		
		//Users will start with 1 "trip" just so the average of their initial score of 4 works (their first rating shouldnt account for 100% of rating then)
		this.totalPassengerTrips = 1;
		this.totalDriverTrips = 1;
	}
	
	//All getter methods
	public int getUserID() {
		return this.userID;
	}
	public boolean getIsAdmin() {
		return this.isAdmin;
	}
	public String getFirstName() {
		return this.firstName;
	}
	public String getLastName() {
		return this.lastName;
	}
	public String getEmail() {
		return this.email;
	}
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	public String getPreferences() {
		return this.preferences;
	}
	public String getPassword() {
		return this.password;
	}
	public float getPassengerRating() {
		return this.passengerRating;
	}
	public float getDriverRating() {
		return this.driverRating;
	}
	
	//All setter methods
	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}
	public void updatePassengerRating(int newestRating) {
		this.totalPassengerTrips++;
		//Running average without storing a sum of all ratings
		this.passengerRating = (this.passengerRating * (this.totalPassengerTrips - 1) + newestRating)/this.totalPassengerTrips;
	}
	public void updateDriverRating(int newestRating) {
		this.totalDriverTrips++;
		//Running average without storing a sum of all ratings
		this.driverRating = (this.driverRating * (this.totalDriverTrips - 1) + newestRating)/this.totalDriverTrips;
	}
		
}
