package ca.mcgill.ecse321.ecse321_group7;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TripSearchResult extends AppCompatActivity {
    private TextView date;
    private TextView dep_loc;
    private TextView dep_time;
    private TextView arr_loc;
    private TextView arr_time;
    private ListView list_view;

    private ArrayList<CustomListView> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_tripsearchresult);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        date = this.findViewById(R.id.DateText);
        dep_loc = findViewById(R.id.DepartureLoc);
        arr_loc = findViewById(R.id.ArrivalLoc);
        list_view = this.findViewById(R.id.myListView);

        //////////////
        // Receive the arguments from previous activity here
        Intent intent = getIntent();
        String dept_date = intent.getStringExtra("Date");
        String dept_loc = intent.getStringExtra("Dept");
        String dest_loc = intent.getStringExtra("Dest");
        int seats = intent.getIntExtra("Seats", 1);
        /////////////


        // Set up header
        date.setText(dept_date);
        dep_loc.setText(dept_loc);
        arr_loc.setText(dest_loc);


        // Obtain search result using Volley
        ////////////////////

        RequestQueue mQueue;
        String url = "https://ecse321-group7.herokuapp.com/trips/search?dep=" + dept_loc;
                //"&dest=" + dest_loc +
                //"&date=" + dept_date +
                //"&seats=" + seats +
                //"&sortBy=0";

        System.out.println(url);


        mQueue = Volley.newRequestQueue(TripSearchResult.this);

        Response.Listener<JSONArray> JSONListener = new Response.Listener<JSONArray>() {       // listener when connection is succeeded
            @Override
            public void onResponse(JSONArray response) {
                // JSONObject has to be dealt with try-catch else compile error
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject data = response.getJSONObject(i);
                        CustomListView item = new CustomListView(i, "Annoynymous Driver", data.getString("seats_available"), data.getString("departure_time"), data.getJSONArray("durations").getString(0) + " hours");
                        listItems.add(item);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // export stuffs into listview
                CustomListViewAdapter adapter = new CustomListViewAdapter(TripSearchResult.this, R.layout.list_result_item, listItems);
                list_view.setAdapter(adapter);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {       // Listener when connection failed
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Failed with error msg:\t" + error.getMessage());
                System.out.println("Error StackTrace: \t" + error.getStackTrace());
                // edited here
                try {
                    byte[] htmlBodyBytes = error.networkResponse.data;
                    System.out.println(new String(htmlBodyBytes) + error);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        };

        mQueue.add(new JsonArrayRequest(Request.Method.POST, url, null, JSONListener, errorListener));
    }
}