package ca.mcgill.ecse321.ecse321_group7;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Trip_Create extends AppCompatActivity {
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

    private ArrayList<DestinationListView> listItems = new ArrayList<>();
    private ListView list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_trip_create);
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

        //////////////
        // Receive the userid value from User_login here

        Intent i = getIntent();
        myUserid = i.getIntExtra("userid", -1);
        //Toast.makeText(Trip_Create.this, "My User ID: " + myUserid, Toast.LENGTH_SHORT).show();
        //System.out.println("My Userid: " + myUserid);
        /////////////

        //Give an initial destination field
        DestinationListView item = new DestinationListView();
        listItems.add(item);
        final DestinationListViewAdapter adapter = new DestinationListViewAdapter(Trip_Create.this, R.layout.new_destination_list_item, listItems);
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
                            Toast.makeText(Trip_Create.this, "Please ensure destination fields are filled", Toast.LENGTH_SHORT).show();
                            broken = true;
                            break;
                        }
                    }
                    if (!broken) {
                        addTripToDB(destStr, durStr, priceStr);
                    }
                }
                else {
                    Toast.makeText(Trip_Create.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
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
        String url = "https://ecse321-group7.herokuapp.com/trips/create?driverID=" + myUserid + "&driverEmail=NA&driverPhone=NA&date=" + depart_date.getText().toString()
                + "&depTime=" + depart_time.getText().toString() + "&depLocation=" + departure.getText().toString() + "&destinations=" + destStr
                + "&tripDurations=" + durStr + "&prices=" + priceStr + "&seats=" + availableSeats.getText().toString() + "&vehicleType=" + vehicleInfo.getText().toString()
                + "&licensePlate=" + licensePlate.getText().toString() + "&comments=" + comments.getText().toString().replaceAll("\'","");
        mQueue = Volley.newRequestQueue(Trip_Create.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(Trip_Create.this, response, Toast.LENGTH_SHORT).show();
                //GO TO THE SPECIFIC DRIVER TRIP DETAILS PAGE
                Intent GoToProfile = new Intent(Trip_Create.this, Profile_page.class);
                GoToProfile.putExtra("userid",myUserid);
                startActivity(GoToProfile);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Trip_Create.this, "That didn't work!", Toast.LENGTH_SHORT).show();
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
                Intent GoToProfile = new Intent(Trip_Create.this,Profile_page.class);
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

        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        TextView tf = (TextView) v;
        Bundle args = getTimeFromLabel(tf.getText().toString());
        args.putInt("id", v.getId());

        TimePickerFragment newFragment = new TimePickerFragment();
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
}
