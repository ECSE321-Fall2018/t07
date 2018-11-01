package ca.mcgill.ecse321.ecse321_group7;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class Profile_page extends AppCompatActivity {

    private int myUserid = 0;
    private TextView FULLNAME;
    private TextView EMAIL;
    private TextView PHONE;
    private TextView RATING;

    private String Fullname;
    private String Profile_email;
    private String Profile_phone;
    private String Rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //////////////
        // Receive the userid value from User_login here
        /*Intent i = getIntent();
        myUserid = i.getIntExtra("userid", -1);
        Toast.makeText(Profile_page.this, "My User ID: " + myUserid, Toast.LENGTH_SHORT).show();*/
        //System.out.println("My Userid: " + myUserid);
        /////////////

        //User ID passed successfully
        //show information returned by user search in textviews
        FULLNAME = findViewById(R.id.fullName);
        EMAIL = findViewById(R.id.Profile_email);
        PHONE = findViewById(R.id.profile_phone);
        RATING = findViewById(R.id.Rating);

        RequestQueue ProfilePage;
        String url = "https://ecse321-group7.herokuapp.com/users/search?id=" + myUserid;
        ProfilePage = Volley.newRequestQueue(Profile_page.this);

        Response.Listener <JSONArray> JSONListener = new Response.Listener<JSONArray>() {       // listener when connection is succeeded
            @Override
            public void onResponse(JSONArray response) {
                // JSONObject has to be dealt with try-catch else compile error
                try {
                    JSONObject data = response.getJSONObject(0);    // Just assuming there's only 1 element

                    //get fullname, email, phone number
                    Fullname = data.getString("firstname") + " " + data.getString("lastname");
                    Profile_email = "Email: " + data.getString("email");
                    Profile_phone = "Phone:" + data.getString("phone");
                    Rating =  "Rating: " + data.getString("passenger_rating");

                    if (Fullname==null || Profile_phone==null || Profile_email==null) {
                        //if any of them is null
                        Toast.makeText(Profile_page.this, "wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        //show returned result in textview
                        FULLNAME.setText(Fullname);
                        //FULLNAME.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                        EMAIL.setText(Profile_email);
                        PHONE.setText(Profile_phone);
                        RATING.setText(Rating);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Profile_page.this, "exception caught", Toast.LENGTH_SHORT).show();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {       // Listener when connection failed
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Profile_page.this, "error", Toast.LENGTH_SHORT).show();
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

        ProfilePage.add(new JsonArrayRequest(Request.Method.POST, url, null,JSONListener, errorListener));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_option, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeMenuButton:
                Intent GoToSearch = new Intent(Profile_page.this,Trip_Search.class);
                startActivityForResult(GoToSearch, 0);
                //Should send userID when we figure out the global way to do this
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
