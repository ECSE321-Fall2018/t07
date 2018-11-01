package ca.mcgill.ecse321.ecse321_group7;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Trip_Details extends AppCompatActivity {

    private TextView depDateTV;
    private TextView depLocTV;
    private TextView arrLocTV;
    private TextView vehicleInfoTV;
    private TextView seatsTV;
    private TextView commentsTV;
    private TextView driverNameTV;
    private TextView driverRatingTV;
    private TextView depTimeTV;

    private String depDate;
    private String depLoc;
    private ArrayList<String> destinationArray = new ArrayList<>();
    private String vehicleInfo;
    private String seats;
    private String comments;
    private String firstName;
    private String lastName;
    private String driverRating;
    private int tripID;
    private int userID;
    private String departureTime;
    private ArrayList<String> durationArray = new ArrayList<>();
    private ArrayList<String> priceArray = new ArrayList<>();
    private ArrayList<String> passengerArray = new ArrayList<>();
    private int destinationsLength;

    private ArrayList<CustomDetailsListView> listItems = new ArrayList<>();
    private ListView list_view;

    private Button joinLeaveTrip;
    private boolean alreadyInTrip;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_trip_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        depDateTV = findViewById(R.id.DateText);
        depLocTV = findViewById(R.id.DepartureLoc);
        arrLocTV = findViewById(R.id.ArrivalLoc);
        vehicleInfoTV = findViewById(R.id.vehicleInfo);
        seatsTV = findViewById(R.id.remainingSeatsText);
        commentsTV = findViewById(R.id.driverCommentsField);
        driverNameTV = findViewById(R.id.driverNameField);
        driverRatingTV = findViewById(R.id.driverRatingField);
        depTimeTV = findViewById(R.id.depTimeText);
        list_view = findViewById(R.id.detailsListView);
        joinLeaveTrip = findViewById(R.id.joinTripButton);
        joinLeaveTrip.setText("Join Trip");

        /////////////
        // Receive the arguments from previous activity here
        // - @params JSONObject trip_data: JSON data for entire row of selected trip
        // - Note: Corresponding row from BOTH trip_table & user_table are combined together so easier to handle

        Intent intent = getIntent();
        String json_str = intent.getStringExtra("json");
        userID = intent.getIntExtra("userID", -1);
        alreadyInTrip = false;

        System.out.println(json_str);

        JSONObject trip_data = null;
        try {
            trip_data  = new JSONObject(json_str);

            //Obtain all of the values from the JSON object
            depDate = trip_data.getString("departure_date");      // Departure date for this trip (from trip_table)
            depLoc = trip_data.getString("departure_location");

            //We want the last item from the destinations array to be displayed in the large section at the top
            destinationsLength = trip_data.getJSONArray("destinations").length();
            for (int i=0;i<destinationsLength;i++) {
                destinationArray.add(trip_data.getJSONArray("destinations").getString(i));
                durationArray.add(trip_data.getJSONArray("durations").getString(i));
                priceArray.add(trip_data.getJSONArray("prices").getString(i));
            }

            int passengerLength = trip_data.getJSONArray("passenger_id").length();
            for (int i=0;i<passengerLength;i++) {
                if (trip_data.getJSONArray("passenger_id").getInt(i) == userID) {
                    alreadyInTrip = true;
                    joinLeaveTrip.setText("Leave Trip");
                }
                passengerArray.add(trip_data.getJSONArray("passenger_id").getString(i));
            }

            vehicleInfo = trip_data.getString("vehicle_type");
            seats = trip_data.getString("seats_available");
            comments = trip_data.getString("comments");
            firstName = trip_data.getString("firstname");    // Driver's first name (from user_table)
            lastName = trip_data.getString("lastname");
            driverRating = trip_data.getString("driver_rating");
            tripID = trip_data.getInt("trip_id");
            departureTime = trip_data.getString("departure_time");

        } catch (Exception e) {
            this.finish();  // go back when error
        }
        /////////////

        depDateTV.setText("Departure Date: " + depDate);
        depLocTV.setText(capitalizeFirstLetter(depLoc));
        arrLocTV.setText(capitalizeFirstLetter(destinationArray.get(destinationsLength-1)));
        vehicleInfoTV.setText("Vehicle Info: " + vehicleInfo);
        seatsTV.setText(seats + " Seats Left");
        commentsTV.setText(comments);
        driverNameTV.setText(capitalizeFirstLetter(firstName) + " " + capitalizeFirstLetter(lastName));
        driverRatingTV.setText("Rating: " + driverRating);
        depTimeTV.setText("Departure Time: " + departureTime);


        for(int i=0;i<destinationsLength;i++) {
            CustomDetailsListView item = new CustomDetailsListView(capitalizeFirstLetter(destinationArray.get(i)), durationArray.get(i), priceArray.get(i));
            listItems.add(item);
        }
        CustomDetailsListViewAdapter adapter = new CustomDetailsListViewAdapter(Trip_Details.this, R.layout.trip_details_list_item, listItems);
        list_view.setAdapter(adapter);

        joinLeaveTrip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                RequestQueue mQueue;
                if (!alreadyInTrip) {
                    String url = "https://ecse321-group7.herokuapp.com/trips/join?userID=" + userID
                            + "&tripID=" + tripID;
                    mQueue = Volley.newRequestQueue(Trip_Details.this);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(Trip_Details.this, response, Toast.LENGTH_SHORT).show();
                            joinLeaveTrip.setText("Leave Trip");
                            alreadyInTrip = true;

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Trip_Details.this, "That didn't work!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // Add the request to the RequestQueue.
                    mQueue.add(stringRequest);
                }
                else {
                    String url = "https://ecse321-group7.herokuapp.com/trips/leave?userID=" + userID
                            + "&tripID=" + tripID;
                    mQueue = Volley.newRequestQueue(Trip_Details.this);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(Trip_Details.this, response, Toast.LENGTH_SHORT).show();
                            joinLeaveTrip.setText("Join Trip");
                            alreadyInTrip = false;

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Trip_Details.this, "That didn't work!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // Add the request to the RequestQueue.
                    mQueue.add(stringRequest);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_option, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profilePageMenu:
                Intent GoToProfile = new Intent(Trip_Details.this,Profile_page.class);
                startActivityForResult(GoToProfile, 0);
                return true;

            case R.id.homeMenuButton:
                Intent GoToSearch = new Intent(Trip_Details.this,Trip_Search.class);
                startActivityForResult(GoToSearch, 0);
                //Should send userID when we figure out the global way to do this
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}