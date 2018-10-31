package ca.mcgill.ecse321.ecse321_group7;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Trip_Details extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_tripsearchresult);

        /////////////
        // Receive the arguments from previous activity here
        // - @params JSONObject trip_data: JSON data for entire row of selected trip
        // - Note: Corresponding row from BOTH trip_table & user_table are combined together so easier to handle

        Intent intent = getIntent();
        String json_str = intent.getStringExtra("json");

        System.out.println(json_str);

        JSONObject trip_data = null;
        try {
            trip_data  = new JSONObject(json_str);
            String firstname = trip_data.getString("firstname");    // Driver's first name (from user_table)
            String date = trip_data.getString("departure_date");      // Departure date for this trip (from trip_table)

        } catch (Exception e) {
            this.finish();  // go back when error
        }
        /////////////

    }
}