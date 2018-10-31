package ca.mcgill.ecse321.ecse321_group7;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
}