package com.ecse321.RideShare.model;

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ecse321.RideShare.RideShareService;

@ComponentScan("com.ecse321.RideShare.*")

public class Trip {
	@Autowired
	RideShareService service;
	
	JdbcTemplate jdbcTemplate;
	
	private static int tripIDCounter = 1; //Counter to ensure no two tripIDs are the same
	private final int tripID;
	private final int driverID; //This will be passed in when the trip is created and shouldn't change
	private final String driverEmail; //Driver contact info should also not change since it references the driver's user data
	private final String driverPhoneNumber;
	
	private LocalDate departureDate; //this is just date part
	private LocalTime departureTime; //this is just the time part
	private String departureLocation;
	private ArrayList<String> destinations = new ArrayList<String>();
	private ArrayList<Float> tripDurations = new ArrayList<Float>();
	private ArrayList<Float> prices = new ArrayList<Float>();
	
	private int availableSeats;
	private ArrayList<Integer> passengerIDList = new ArrayList<Integer>();
	private String vehicleType;
	private String licensePlate;
	private String otherComments;

	public Trip(int driverID, String driverEmail, String driverPhone, String date, String time, String depLocation, ArrayList<String> destinations, 
			ArrayList<Float> tripDurations, ArrayList<Float> prices, int seats, String vehicleType, String licensePlate, String comments) {
		tripID = tripIDCounter;
		tripIDCounter++;
		this.driverID = driverID;
		this.driverEmail = driverEmail;
		this.driverPhoneNumber = driverPhone;
		
		this.departureDate = LocalDate.parse(date);
		this.departureTime = LocalTime.parse(time);
		this.departureLocation = depLocation;
		this.destinations = destinations;
		this.tripDurations = tripDurations;
		this.prices = prices;
		
		this.availableSeats = seats;
		this.vehicleType = vehicleType;
		this.licensePlate = licensePlate;
		this.otherComments = comments;

	}
	
	//Get methods for typical fields that might be searched for
	public int getTripID() {
		return this.tripID;
	}
	public int getDriverID() {
		return this.driverID;
	}
	public LocalDate getDepartureDate() {
		return this.departureDate;
	}
	public LocalTime getDepartureTime() {
		return this.departureTime;
	}
	public String getDepartureLocation() { 
		return this.departureLocation;
	}
	public ArrayList<Float> getTripDurations() {
		return this.tripDurations;
	}
	public ArrayList<String> getDestination() {
		return this.destinations;
	}
	public ArrayList<Integer> getPassengerIDList() {
		return this.passengerIDList;
	}
	public ArrayList<Float> getPrices() {
		return this.prices;
	}
	public int getSeats() {
		return this.availableSeats;
	}
	public String getVehicleType() {
		return this.vehicleType;
	}
	public String getLicencePlate() {
		return this.licensePlate;
	}
	public String getDriverPhone() {
		return this.driverPhoneNumber;
	}
	public String getComments() {
		return this.otherComments;
	}
	
	//Likely don't want drivers changing a lot of the info on their trip, because you don't want existing passengers blind-sided
	//by a sudden change to price or destination, etc. So not many things need to be set after the fact
	public void setOtherComments(String comments) {
		this.otherComments = comments;
	}
}
