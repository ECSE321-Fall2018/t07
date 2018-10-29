package ca.mcgill.ecse321.ecse321_group7;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class Trip_Search extends AppCompatActivity {
    private List<String> availableSeatsSpinnerOptions = new ArrayList<>(Arrays.asList("1", "2", "3", "4+"));
    private ArrayAdapter<String> seatsSpinnerAdapter;

    private EditText departure;
    private EditText destination;

    private Button searchButton;

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
