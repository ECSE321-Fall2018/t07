package ca.mcgill.ecse321.ecse321_group7;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

public class Trip_Edit extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /////////////
        // Receive the arguments from previous activity here
        // - @params JSONObject trip_data: JSON data for entire row of selected trip
        // - Note: Corresponding row from BOTH trip_table & user_table are combined together so easier to handle

        Intent intent = getIntent();
        String json_str = intent.getStringExtra("json");
        int userID = intent.getIntExtra("userID", -1);

        System.out.println(json_str);
    }
}
