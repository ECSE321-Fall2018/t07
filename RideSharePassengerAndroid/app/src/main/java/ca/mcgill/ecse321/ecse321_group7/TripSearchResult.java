package ca.mcgill.ecse321.ecse321_group7;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TripSearchResult extends AppCompatActivity {
    private TextView date;
    private TextView dep_loc;
    private TextView dep_time;
    private TextView arr_loc;
    private TextView arr_time;
    private ListView list_view;

    private int userID;

    private ArrayList<CustomListView> listItems = new ArrayList<>();

    private List<String> sortSpinnerOptions = new ArrayList<>(Arrays.asList("Departure Time", "Price", "Available Seats", "Total Duration"));
    private ArrayAdapter<String> sortSpinnerAdapter;
    private Spinner sortSpinner;

    private String dept_date;
    private String dept_loc;
    private String dest_loc;
    private int seats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_tripsearchresult);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        date = this.findViewById(R.id.DateText);
        dep_loc = findViewById(R.id.DepartureLoc);
        arr_loc = findViewById(R.id.ArrivalLoc);
        list_view = this.findViewById(R.id.myListView);

        sortSpinner = (Spinner) findViewById(R.id.sortSpinner);
        sortSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortSpinnerOptions);
        sortSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(sortSpinnerAdapter);

        //////////////
        // Receive the arguments from previous activity here
        Intent intent = getIntent();
        dept_date = intent.getStringExtra("Date");
        dept_loc = intent.getStringExtra("Dept");
        dest_loc = intent.getStringExtra("Dest");
        seats = intent.getIntExtra("Seats", 1);
        userID = intent.getIntExtra("userID", -1);
        /////////////


        // Set up header
        dep_loc.setText(dept_loc);

        if (dept_date.isEmpty()) {
            date.setText("Any departure date");
        }
        else {
            date.setText(dept_date);
        }

        if (dest_loc.isEmpty()) {
            arr_loc.setText("Anywhere");
        }
        else {
            arr_loc.setText(dest_loc);
        }


        // Obtain search result using Volley
        ////////////////////
        //generateSearchResults(0);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Departure Time"))
                {
                    listItems.clear();
                    list_view.setAdapter(null);
                    generateSearchResults(0);
                }
                else if(selectedItem.equals("Price")) {
                    listItems.clear();
                    list_view.setAdapter(null);
                    generateSearchResults(1);
                }
                else if(selectedItem.equals("Available Seats")) {
                    listItems.clear();
                    list_view.setAdapter(null);
                    generateSearchResults(2);
                }
                else if(selectedItem.equals("Total Duration")) {
                    listItems.clear();
                    list_view.setAdapter(null);
                    generateSearchResults(3);
                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


    }


    private void generateSearchResults(int sort) {
        RequestQueue mQueue;
        //String url = "http://192.168.0.141:8080/trips/search"
        String url = "https://ecse321-group7.herokuapp.com/trips/search"
                + "?dep=" + dept_loc
                + "&dest=" + dest_loc
                + "&date=" + dept_date
                + "&seats=" + seats
                + "&sortBy=" + sort;

        mQueue = Volley.newRequestQueue(TripSearchResult.this);

        Response.Listener<JSONArray> JSONListener = new Response.Listener<JSONArray>() {       // listener when connection is succeeded
            @Override
            public void onResponse(JSONArray response) {
                // JSONObject has to be dealt with try-catch else compile error
                try {
                    System.out.println("Response Length:" + response.length());

                    if (response.isNull(0)) {   // If null then show dialog for not found
                        myDialogFragment dialog = new myDialogFragment();
                        dialog.show(getFragmentManager(), "notfound");
                    }
                    else {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject data = response.getJSONObject(i);
                            int arrLength = data.getJSONArray("durations").length();
                            JSONArray destinations = data.getJSONArray("destinations");
                            CustomListView item = new CustomListView(i, capitalizeFirstLetter(data.getString("firstname")) + " " + capitalizeFirstLetter(data.getString("lastname")),
                                    data.getString("seats_available") + " seats available\n to " + destinations.getString(destinations.length()-1),
                                    data.getString("departure_date") + "\n" + data.getString("departure_time"),
                                    data.getJSONArray("durations").getString(arrLength-1) + " hours",
                                    data.toString()
                            );
                            listItems.add(item);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // export stuffs into listview
                CustomListViewAdapter adapter = new CustomListViewAdapter(TripSearchResult.this, R.layout.list_result_item, listItems);
                list_view.setAdapter(adapter);
                list_view.setOnItemClickListener(onItemClickListener);
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

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // retrieve the item
            ListView listView = (ListView)parent;
            CustomListView item = (CustomListView)listView.getItemAtPosition(position);

            Intent OpenDetails = new Intent(TripSearchResult.this, Trip_Details.class);
            OpenDetails.putExtra("json",item.getJSON());
            OpenDetails.putExtra("userID", userID);
            startActivity(OpenDetails);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_option, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profilePageMenu:
                Intent GoToProfile = new Intent(TripSearchResult.this,Profile_page.class);
                GoToProfile.putExtra("userid",userID);
                startActivityForResult(GoToProfile, 0);
                return true;

            case R.id.homeMenuButton:
                Intent GoToSearch = new Intent(TripSearchResult.this,Trip_Search.class);
                GoToSearch.putExtra("userid",userID);
                startActivityForResult(GoToSearch, 0);
                //Should send userID when we figure out the global way to do this
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Quick method that capitlizes the first letter of the string so we can take our entirely lower case data and display it nicely
    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

}