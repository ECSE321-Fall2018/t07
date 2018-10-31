package ca.mcgill.ecse321.ecse321_group7;

import android.app.Activity;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Trip_Search extends AppCompatActivity {
    private List<String> availableSeatsSpinnerOptions = new ArrayList<>(Arrays.asList("1", "2", "3", "4+"));
    private ArrayAdapter<String> seatsSpinnerAdapter;

    private EditText departure;
    private EditText destination;

    private Button searchButton;
    //private ImageButton profileButton;

    private int myUserid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Spinner seatsSpinner = (Spinner) findViewById(R.id.availableSeatsSpinner);
        seatsSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, availableSeatsSpinnerOptions);
        seatsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seatsSpinner.setAdapter(seatsSpinnerAdapter);

        departure = findViewById(R.id.departure_input);
        destination = findViewById(R.id.destination_input);
        searchButton = findViewById(R.id.button);

        /*
        profileButton = findViewById(R.id.profileButton);

        profileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent JumpToProfilePage = new Intent(Trip_Search.this, Profile_page.class);
                startActivity(JumpToProfilePage);
            }
        });
        */

        //////////////
        // Receive the userid value from User_login here
        Intent i = getIntent();
        myUserid = i.getIntExtra("userid", -1);
        Toast.makeText(Trip_Search.this, "My User ID: " + myUserid, Toast.LENGTH_SHORT).show();
        //System.out.println("My Userid: " + myUserid);
        /////////////

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (!ToReject(departure) && !ToReject(destination)) {
                    //Execute the search code and go to search page layout
                }
                else {
                    Toast.makeText(Trip_Search.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
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
            case R.id.ProfilePage:
                Intent launchNewIntent = new Intent(Trip_Search.this,Profile_page.class);
                startActivityForResult(launchNewIntent, 0);
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

    public void showDatePickerDialog(View v) {
        TextView tf = (TextView) v;
        Bundle args = getDateFromLabel(tf.getText().toString());
        args.putInt("id", v.getId());

        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void setDate(int id, int d, int m, int y) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(String.format("%04d-%02d-%02d", y, m + 1, d));
    }

}
