package ca.mcgill.ecse321.ecse321_group7;

import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.icu.text.UnicodeSetSpanner;
import android.media.effect.Effect;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ca.mcgill.ecse321.ecse321_group7.User_login;

public class User_Signup extends AppCompatActivity {

    private EditText FirstName;
    private EditText LastName;
    private EditText Email;
    //datatype of phone number in user class, backend, is string
    private EditText Phone_no;
    private EditText Password;

    private int userid = -1;

    //avoid repeating names of signup button, but doesn't really matter as they are private
    private Button FormalSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__signup);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FirstName = findViewById(R.id.firstname);
        LastName = findViewById(R.id.lastname);
        Email = findViewById(R.id.Email);
        Phone_no = findViewById(R.id.phone_no);
        Password = findViewById(R.id.Password);

        FormalSignUp = findViewById(R.id.SignUp);
        FormalSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(ToReject(Email) || ToReject(FirstName) || ToReject(LastName) || ToReject(Phone_no) || ToReject(Password))
                    Toast.makeText(User_Signup.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                else if(checkPhoneNumber(Phone_no)) {
                    Toast.makeText(User_Signup.this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Handling Login using Volley
                    ////////////////////
                    RequestQueue mQueue;
                    String url = "https://ecse321-group7.herokuapp.com/users/create?firstName=" + FirstName.getText().toString() + "&lastName=" + LastName.getText().toString()
                            + "&email=" + Email.getText().toString() + "&phoneNumber=" + Phone_no.getText().toString().replace("-", "") + "&password=" + Password.getText().toString();
                    mQueue = Volley.newRequestQueue(User_Signup.this);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                Toast.makeText(User_Signup.this, response, Toast.LENGTH_SHORT).show();
                                Intent GoToLogin = new Intent(User_Signup.this, User_login.class);
                                startActivity(GoToLogin);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(User_Signup.this, "That didn't work!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    // Add the request to the RequestQueue.
                    mQueue.add(stringRequest);
                }
            }
        });
    }

    //!!Have problem import user_login class
    public boolean ToReject(EditText check){
        boolean rejected = false;
        if(check.getText().toString().equals("")) rejected = true;
        return rejected;
    }

    public boolean checkPhoneNumber(EditText number) {
        boolean rejected = false;
        String str = number.getText().toString();
        str = str.replace("-", "");
        if (str.length() != 10) rejected = true;
        if (!str.matches("[0-9]+")) rejected = true;
        return rejected;
    }

}
