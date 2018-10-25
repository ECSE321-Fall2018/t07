package com.ecse321.RideShare;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@ComponentScan("com.ecse321.RideShare.*")

//This class handles all of the connections to the database so that injection does not fail
public class RideShareService {
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	public List<Map<String,Object>> selectUsers() {
		return jdbcTemplate.queryForList("select to_json(user_table) from user_table");
		
	}
	
	public List<Map<String,Object>> selectTrips() {
		return jdbcTemplate.queryForList("select to_json(trip_table) from trip_table");
	}
	
	public List<Map<String,Object>> selectUser(int userid) {
		return jdbcTemplate.queryForList("select to_json(user_table) from user_table where userid = ?", userid);
	}
	
	public List<Map<String,Object>> selectTrip(int tripid) {
		return jdbcTemplate.queryForList("select to_json(trip_table) from trip_table where trip_id = " + tripid);
	}
	
	public List<Map<String, Object>> executeSQL (String query) {
		return jdbcTemplate.queryForList(query);
	}
	
	public void sqlInsert (String query) {
		jdbcTemplate.update(query);
	}
	
	
}
