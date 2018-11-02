package ca.mcgill.ecse321.ecse321_group7;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class Trip_Edit extends AppCompatActivity {
    private EditText departure;
    private TextView depart_date;
    private TextView depart_time;
    private EditText availableSeats;
    private EditText comments;
    private EditText vehicleInfo;
    private EditText licensePlate;

    private Button createButton;
    private ImageButton destinationButton;
    private ImageButton removeDestButton;

    private int myUserid = 0;
    private String odepDate;
    private String odepLoc;
    private ArrayList<String> odestinationArray = new ArrayList<>();
    private String ovehicleInfo;
    private String olicence;
    private String oseats;
    private String ocomments;
    private String ofirstName;
    private String olastName;
    private String odriverRating;
    private int otripID;
    private String odepartureTime;
    private ArrayList<String> odurationArray = new ArrayList<>();
    private ArrayList<String> opriceArray = new ArrayList<>();
    private ArrayList<String> opassengerArray = new ArrayList<>();
    private int odestinationsLength;

    private ArrayList<DestinationListView> listItems = new ArrayList<>();
    private ListView list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_trip_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        departure = findViewById(R.id.departure_input);
        availableSeats = findViewById(R.id.seats_input);
        depart_date = this.findViewById(R.id.departure_date_input);
        depart_time = this.findViewById(R.id.time_input);
        comments = this.findViewById(R.id.comments_input_box);
        vehicleInfo = this.findViewById(R.id.vehicleInfoBox);
        licensePlate = this.findViewById(R.id.licensePlateBox);

        createButton = findViewById(R.id._create);
        destinationButton = findViewById(R.id.addDestinationButton);
        removeDestButton = findViewById(R.id.removeDestinationButton);

        list_view = findViewById(R.id.destinationsListView);



        /////////////
        // Receive the arguments from previous activity here
        // - @params JSONObject trip_data: JSON data for entire row of selected trip
        // - Note: Corresponding row from BOTH trip_table & user_table are combined together so easier to handle

        Intent intent = getIntent();
        String json_str = intent.getStringExtra("json");
        myUserid = intent.getIntExtra("userID", -1);
        JSONObject trip_data = null;
        try {
            trip_data  = new JSONObject(json_str);

            //Obtain all of the values from the JSON object
            odepDate = trip_data.getString("departure_date");      // Departure date for this trip (from trip_table)
            odepLoc = trip_data.getString("departure_location");

            //We want the last item from the destinations array to be displayed in the large section at the top
            odestinationsLength = trip_data.getJSONArray("destinations").length();
            for (int i=0;i<odestinationsLength;i++) {
                odestinationArray.add(trip_data.getJSONArray("destinations").getString(i));
                odurationArray.add(trip_data.getJSONArray("durations").getString(i));
                opriceArray.add(trip_data.getJSONArray("prices").getString(i));
            }

            ovehicleInfo = trip_data.getString("vehicle_type");
            olicence = trip_data.getString("licence_plate");
            oseats = trip_data.getString("seats_available");
            ocomments = trip_data.getString("comments");
            otripID = trip_data.getInt("trip_id");
            odepartureTime = trip_data.getString("departure_time");

        } catch (Exception e) {
            this.finish();  // go back when error
        }

        /*
         * Setting up the initial value
         */

        // Header
        departure.setText(capitalizeFirstLetter(odepLoc));
        depart_date.setText(odepDate);
        depart_time.setText(odepartureTime);
        availableSeats.setText(oseats);
        vehicleInfo.setText(ovehicleInfo);
        licensePlate.setText(olicence);
        comments.setText(ocomments);

        // Destination, Duration, and Price
        for (int i=0; i<odestinationArray.size(); i++) {
            DestinationListView item = new DestinationListView();
            item.setDest(capitalizeFirstLetter(odestinationArray.get(i)));
            item.setDur(odurationArray.get(i));
            item.setPrice(opriceArray.get(i));
            listItems.add(item);

            System.out.println(item.toString());
        }

        final DestinationListViewAdapter adapter = new DestinationListViewAdapter(Trip_Edit.this, R.layout.new_destination_list_item, listItems);
        list_view.setAdapter(adapter);

        //Cicking the button adds more destination fields
        destinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                DestinationListView item = new DestinationListView();
                listItems.add(item);
                adapter.notifyDataSetChanged();
            }
        });
        removeDestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (listItems.size()>0) {
                    listItems.remove(listItems.size()-1);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        createButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if (!ToReject(departure.getText().toString()) && !ToReject(availableSeats.getText().toString()) && !ToReject(depart_date.getText().toString()) && !ToReject(depart_time.getText().toString())) {       // Checking departure only; just list them all
                    String destStr = "";
                    String durStr = "";
                    String priceStr = "";
                    boolean first = true;
                    boolean broken = false;
                    System.out.println(list_view.getAdapter().getCount());

                    for (int i=0; i < list_view.getAdapter().getCount(); i++) {
                        if (!first) {
                            destStr += ", ";
                            durStr += ", ";
                            priceStr += ", ";
                        }

                        DestinationListView item = (DestinationListView)list_view.getItemAtPosition(i);
                        String dest = item.getDest();
                        String dur = item.getDur();
                        String price = item.getPrice();


                        if (!ToReject(dest) && !ToReject(dur) && !ToReject(price)) {
                            destStr += dest;
                            durStr += dur;
                            priceStr += price;
                            first = false;
                        }
                        else {
                            Toast.makeText(Trip_Edit.this, "Please ensure destination fields are filled", Toast.LENGTH_SHORT).show();
                            broken = true;
                            break;
                        }
                    }
                    if (!broken) {
                        addTripToDB(destStr, durStr, priceStr);
                    }
                }
                else {
                    Toast.makeText(Trip_Edit.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addTripToDB(String destStr, String durStr, String priceStr) {
        // Handling Login using Volley
        ////////////////////
        RequestQueue mQueue;
        ///trips/create?driverID={Driver ID}&driverEmail={Driver Email}&driverPhone={Driver Phone Number}&date={yyyy-mm-dd}
        // &depTime={HH:MM:SS}&depLocation={Departure Location}&destinations={Destination1, Destination2, etc}
        // &tripDurations={Duration1, Duration2, etc}&prices={Price1, Price2, etc}&seats={Available Seats}&vehicleType={Vehicle Type}
        // &licensePlate={License Plate Number}&comments={Additional Comments}
        String url = "https://ecse321-group7.herokuapp.com/trips/modify?tripID=" + otripID + "&driverEmail=NA&driverPhone=NA&date=" + depart_date.getText().toString()
                + "&depTime=" + depart_time.getText().toString() + "&depLocation=" + departure.getText().toString() + "&destinations=" + destStr
                + "&tripDurations=" + durStr + "&prices=" + priceStr + "&seats=" + availableSeats.getText().toString() + "&vehicleType=" + vehicleInfo.getText().toString()
                + "&licensePlate=" + licensePlate.getText().toString() + "&comments=" + comments.getText().toString().replaceAll("\'","");
        mQueue = Volley.newRequestQueue(Trip_Edit.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(Trip_Edit.this, response, Toast.LENGTH_SHORT).show();
                //GO TO THE SPECIFIC DRIVER TRIP DETAILS PAGE
                Intent GoToProfile = new Intent(Trip_Edit.this, Profile_page.class);
                GoToProfile.putExtra("userid",myUserid);
                startActivity(GoToProfile);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Trip_Edit.this, "That didn't work!", Toast.LENGTH_SHORT).show();
            }
        });
        // Add the request to the RequestQueue.
        mQueue.add(stringRequest);
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
                Intent GoToProfile = new Intent(Trip_Edit.this,Profile_page.class);
                GoToProfile.putExtra("userid",myUserid);  // passing the argument
                startActivityForResult(GoToProfile, 0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean ToReject(String check){
        boolean rejected = false;
        if(check.equals("")) rejected = true;
        return rejected;
    }

    private Bundle getDateFromLabel(String text) {
        Bundle rtn = new Bundle();
        String comps[] = text.toString().split("-");

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        //int day = 5;
        //int month = 5;
        //int year = 5;

        if (comps.length == 3) {
            day = Integer.parseInt(comps[0]);
            month = Integer.parseInt(comps[1]);
            year = Integer.parseInt(comps[2]);
        }

        rtn.putInt("day", day);
        rtn.putInt("month", month-1);
        rtn.putInt("year", year);

        return rtn;
    }

    private Bundle getTimeFromLabel(String text) {
        Bundle rtn = new Bundle();
        String comps[] = text.toString().split(":");
        int hour = 12;
        int minute = 0;

        if (comps.length == 2) {
            hour = Integer.parseInt(comps[0]);
            minute = Integer.parseInt(comps[1]);
        }

        rtn.putInt("hour", hour);
        rtn.putInt("minute", minute);

        return rtn;
    }

    public void showDatePickerDialog(View v) {
        TextView tf = (TextView) v;
        Bundle args = getDateFromLabel(tf.getText().toString());
        args.putInt("id", v.getId());

        DatePickerFragmentEDIT newFragment = new DatePickerFragmentEDIT();
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        TextView tf = (TextView) v;
        Bundle args = getTimeFromLabel(tf.getText().toString());
        args.putInt("id", v.getId());

        TimePickerFragmentEDIT newFragment = new TimePickerFragmentEDIT();
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void setDate(int id, int d, int m, int y) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(String.format("%04d-%02d-%02d", y, m + 1, d));
    }

    public void setTime(int id, int h, int m) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(String.format("%02d:%02d:00", h, m));
    }

    private String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}

