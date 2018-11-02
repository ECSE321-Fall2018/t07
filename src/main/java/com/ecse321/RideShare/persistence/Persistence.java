package com.ecse321.RideShare.persistence;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ecse321.RideShare.model.Trip;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

public class Persistence{
	
	//1. Connect database to code
	//2. Update Trip info in terms of changing in trip_table
	
	private static Connection GetConnection() throws URISyntaxException, SQLException {
		
		/*https://devcenter.heroku.com/articles/connecting-to-relational-databases-on-heroku-with-java*/
		
	    URI dbUri = new URI(System.getenv("DATABASE_URL"));

	    String username = dbUri.getUserInfo().split(":")[0];
	    String password = dbUri.getUserInfo().split(":")[1];
	    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

	    return DriverManager.getConnection(dbUrl, username, password);
	}
		
	public Trip Update() {
		Trip updated = null;
		
		//1. store updates in dynamic result set
		//2. store each variable in local var
		//3. return Trip "updated"
		
		Connection c = null;
		Statement st = null;
		
		try {
			c = GetConnection();
			c.setAutoCommit(true); //trip info should be updated as long as change made in Trip database
			
			st = c.createStatement();
			ResultSet rs = st.executeQuery("Select * from trip_table");
			
			while(rs.next()) {
				//trip id is determined by code, which is not part of Trip constructor
				int driverID = rs.getInt("driver_id");

				//driver email
				
				Date departureDate = rs.getDate("departure_date");
				
				//L.H.S=string; R.H.S=text
				String departureLocation = rs.getString("departure_location");
				String vihecleType = rs.getString("vihecle_type");
				String licensePlate = rs.getString("license_plate");
				String otherComments = rs.getString("comments");
				String driverPhoneNumber = rs.getString("contact_no");
				
				/*Arraylist required*/
				//destinations
				//tripDurations /*LHS=ArrayList<Float>; RHS=text*/
				//prices
				//passenger ID list
				
				int availableSeats = rs.getInt("seats_available");
				
			}
			
			//use the above variables to construct Trip "updated"
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return updated;
	}
	
	
	//ref:https://www.tutorialspoint.com/postgresql/postgresql_java.htm
	
}
