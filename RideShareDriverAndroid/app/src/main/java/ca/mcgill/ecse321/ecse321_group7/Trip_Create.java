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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Trip_Create extends AppCompatActivity {
    private EditText departure;
    private TextView depart_date;
    private TextView depart_time;
    private EditText availableSeats;

    private Button createButton;

    private int myUserid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_trip_create);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        departure = findViewById(R.id.departure_input);
        availableSeats = findViewById(R.id.seats_input);
        createButton = findViewById(R.id._create);

        depart_date = this.findViewById(R.id.departure_date_input);
        depart_time = this.findViewById(R.id.time_input);

        //////////////
        // Receive the userid value from User_login here

        Intent i = getIntent();
        myUserid = i.getIntExtra("userid", -1);
        Toast.makeText(Trip_Create.this, "My User ID: " + myUserid, Toast.LENGTH_SHORT).show();
        //System.out.println("My Userid: " + myUserid);
        /////////////

        createButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (!ToReject(departure)/* && !ToReject(destination)*/) {       // Checking departure only; just list them all
                    //Execute the search code and go to search page layout
                    /*
                    Intent OpenResult = new Intent(Trip_Create.this, TripSearchResult.class);
                    OpenResult.putExtra("Date",depart_date.getText().toString());
                    OpenResult.putExtra("Dept",departure.getText().toString());  // passing the argument
                    OpenResult.putExtra("Dest",destination.getText().toString());  // passing the argument
                    OpenResult.putExtra("Seats",seatsSpinner.getSelectedItemPosition()+1);
                    OpenResult.putExtra("userID", myUserid);

                    startActivity(OpenResult);
                    */
                }
                else {
                    Toast.makeText(Trip_Create.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
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
                Intent GoToProfile = new Intent(Trip_Create.this,Profile_page.class);
                GoToProfile.putExtra("userid",myUserid);  // passing the argument
                startActivityForResult(GoToProfile, 0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean ToReject(EditText check){
        boolean rejected = false;
        if(check.getText().toString().equals("")) rejected = true;
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
