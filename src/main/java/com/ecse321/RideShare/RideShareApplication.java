package com.ecse321.RideShare;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.ecse321.RideShare.model.User;
import com.ecse321.RideShare.model.Trip;


@SpringBootApplication
@RestController

public class RideShareApplication {
	private static final Logger LOG = LoggerFactory.getLogger(RideShareApplication.class);
  
	public static void main(String[] args) {
		SpringApplication.run(RideShareApplication.class, args);
	}
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@RequestMapping(path="/")
	public String greeting() {
		return "Hello, world! ";
	}
	
	//Created a new path just to provide the instructions on how to create a new user or trip
	@RequestMapping(path="/instructions")
	public String instructions() {
		return "Create a new user with: /users/{firstName}/{lastName}/{email}/{phoneNumber}/{password}/{isAdmin} \n"
        		+ "Create a new trip with: /trips/{driverID}/{driverEmail}/{driverPhone}/{Date in format: dd-MM-yyyy HH:mm:ss}/{depLocation}/{destinations}/{tripDurations}/{prices}/{seats}/{vehicleType}/{licensePlate}/{comments}";
		}
	
/*
 * API for Requesting Data
 */
	/* 
	 * For user data (DB: 'user_table')
	 */
	
	// returns the full list of users
	@RequestMapping(path="/users", method=RequestMethod.GET)
	public String user_list (ModelMap modelMap, @RequestParam(name="id", defaultValue= "") String userid ) {	
		List<Map<String,Object>> list;
		list = jdbcTemplate.queryForList("select * from user_table");
		return list.toString();
    }
	
	// search user_table based on id or name
	@RequestMapping(path="/users/search", method=RequestMethod.GET)
	public String user_search (ModelMap modelMap, @RequestParam(name="id", defaultValue= "") String userid, @RequestParam(name="keyword", defaultValue= "") String keyword) {
		if (userid.isEmpty() == false) {
			List<Map<String,Object>> list;
			list = jdbcTemplate.queryForList("select * from user_table where userid = ?", Integer.parseInt(userid));
			return list.toString();
		}
		else if (keyword.isEmpty() == false) {
			List<Map<String,Object>> list;
			
	    		// this is security-wise terribly terribly BAD, but let it be for now
			String keywords[] = keyword.split(" ");	// split using space. 
			
			// only checks for first two elements
			if (keywords[0].isEmpty() == false) {
				keyword = "firstname ='" + keywords[0] + "' OR lastname = '" + keywords[0] + "'";
			}
			if (keywords[1].isEmpty() == false) {
				keyword = keyword + " OR firstname ='" + keywords[1] + "' OR lastname = '" + keywords[1] + "' ";
			}
			
			String query = "select * from user_table WHERE " + keyword;
			list = jdbcTemplate.queryForList(query);
			return list.toString();
		}
		else {
			return "Usage: Send a POST request to \"/users/search?id={id}\" or \"/users/search?keyword={name}\"";
		}
	}
	
	
	/* 
	 * For trip data (DB: 'trip_table')
	 */
	
	// return the list of trips
	@RequestMapping(path="/trips", method=RequestMethod.GET)
	public String trip() {
		List<Map<String,Object>> list;
		list = jdbcTemplate.queryForList("select * from trip_table");
		return list.toString();
	}
       
	// searching trips based on queries
	@RequestMapping(path="/trips/search", method=RequestMethod.GET)
	public String trip_search(ModelMap modelMap, @RequestParam(name="id", defaultValue= "") String tripid, @RequestParam(name="dep", defaultValue="") String departure, 
			@RequestParam(name="dest", defaultValue="") String destination, @RequestParam(name="date", defaultValue="") String date, 
			@RequestParam(name="seats", defaultValue="") String seats) {
		if (tripid.isEmpty() == false) {
			List<Map<String,Object>> list;
			list = jdbcTemplate.queryForList("select * from trip_table where trip_id = ?", Integer.parseInt(tripid));
			return list.toString();
		}
		else if (departure.isEmpty() == false) {
			if (departure.isEmpty() == false) { departure = "departure_location='" + departure + "'"; } else { return "Error. "; }
    			if (destination.isEmpty() == false) { destination = "AND destinations='" + destination + "'"; }
    			if (date.isEmpty() == false) { date = "AND departure_date='" + date + "'"; }
    			if (seats.isEmpty() == false) { date = "AND seats_available>='" + seats + "'"; }
    		
    			List<Map<String,Object>> list;
    			// this is security-wise terribly terribly BAD, but leave as is for now
    			String query = "select * from trip_table WHERE " + departure + destination + date;
    			list = jdbcTemplate.queryForList(query);
    			return list.toString();
		}
		else {
			return "Usage: Send a POST request to \"/trips/search?dep={departure_location}&dest={destination}&date={departure_date}&seats={seats_required}\"";
		}
    		
    }
    
    /*
     * API for Writing into Database
     */
    
    //Creates a new user by taking all of the below-specified inputs
    @PostMapping("/users/{firstName}/{lastName}/{email}/{phoneNumber}/{password}/{isAdmin}")
	public String createUser(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName, @PathVariable("email") String email,
			@PathVariable("phoneNumber") String phoneNumber, @PathVariable("password") String password, @PathVariable("isAdmin") int isAdminInt) {
    	
    	//Currently accepting an int for isAdmin and turning it into a boolean afterwards
    	Boolean isAdmin = false;
    	if (isAdminInt == 1) {
			isAdmin = true;
		}
    	else {
    		isAdmin = false;
    	}
    	//Creates the object and returns a confirmation message that it worked
    	User newUser = new User (firstName, lastName, email, phoneNumber, password, isAdmin);
		String confirmationText = "New User: " + newUser.getFirstName() + " " + newUser.getLastName() + " created successfully!";
		return confirmationText;
	}
    
    //Creates a new trip by taking all of the below-specified inputs
    @PostMapping("/trips/{driverID}/{driverEmail}/{driverPhone}/{date}/{depLocation}/{destinations}/{tripDurations}/{prices}/{seats}/{vehicleType}/{licensePlate}/{comments}")
    public String createTrip(@PathVariable("driverID") int driverID, @PathVariable("driverEmail") String driverEmail, @PathVariable("driverPhone") String driverPhone,
    		@PathVariable("date") String date, @PathVariable("depLocation") String depLocation, @PathVariable("destinations") String destinations, 
    		@PathVariable("tripDurations") String tripDurations, @PathVariable("prices") String prices, @PathVariable("seats") int seats, 
    		@PathVariable("vehicleType") String vehicleType, @PathVariable("licensePlate") String licensePlate, @PathVariable("comments") String comments) {
    	
    	//Array Lists receive their data as pure string (with commas to separate) from the URL and use below methods to create a separated list from that
    	ArrayList<String> destinationList = new ArrayList<String>(Arrays.asList(separateByComma(destinations)));
    	
    	//Duration and Prices need to be in float format but are input as strings, so they are converted first
    	String[] strDuration = separateByComma(tripDurations);
    	Float[] floatDuration = stringToFloatArray(strDuration);    	
    	ArrayList<Float> tripDurationList = new ArrayList<Float>(Arrays.asList(floatDuration));
    	
    	String[] strPrices = separateByComma(prices);
    	Float[] floatPrices = stringToFloatArray(strPrices);    	
    	ArrayList<Float> pricesList = new ArrayList<Float>(Arrays.asList(floatPrices));
    	
    	//Creates the object and returns a confirmation message that it worked
    	Trip newTrip = new Trip (driverID, driverEmail, driverPhone, date, depLocation, destinationList, tripDurationList, pricesList, seats, vehicleType, licensePlate, comments);
    	String confirmationText = "New Trip from " + newTrip.getDepartureLocation() + " to " + String.join(", then ", newTrip.getDestination()) + " created successfully!";
    	return confirmationText;
    }
    
    //Simple method to take a string and separate it by commas into an array (for user input)
    public String[] separateByComma(String input) {
    	String[] values = input.split("\\s*,\\s*");

    	return values;
    }
    
    //Simple method that takes a string array and converts each value into a float (for user input)
    public Float[] stringToFloatArray(String[] strArray) {
    	Float[] floatValues = new Float[strArray.length];
		for (int i = 0; i<strArray.length; i++) {
			floatValues[i] = Float.parseFloat(strArray[i]);
		}
		return floatValues;
    }
}

