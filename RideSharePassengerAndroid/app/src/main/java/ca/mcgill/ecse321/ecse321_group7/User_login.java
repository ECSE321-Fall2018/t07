package ca.mcgill.ecse321.ecse321_group7;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

import static android.widget.Toast.LENGTH_SHORT;

public class User_login extends AppCompatActivity {
    //Jump to user profile page
    private Button login;
    //Jump to singup page
    private Button Signup;

    private EditText Email;
    private EditText Password;

    private int userid = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Signup = findViewById(R.id.signupbutton);
        login = findViewById(R.id.LogIn);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);

        Signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent JumpToSignUp = new Intent(User_login.this, User_Signup.class);
                startActivity(JumpToSignUp);
            }
        });

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(ToReject(Email))
                    Toast.makeText(User_login.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                else if(ToReject(Password))
                    Toast.makeText(User_login.this, "Please enter correct password", Toast.LENGTH_SHORT).show();
                else{


                    // Handling Login using Volley
                    ////////////////////
                    RequestQueue mQueue;
                    String url = "https://ecse321-group7.herokuapp.com/users/auth?email=" + Email.getText().toString() + "&password=" + Password.getText().toString();
                    mQueue = Volley.newRequestQueue(User_login.this);

                    Response.Listener <JSONArray> JSONListener = new Response.Listener<JSONArray>() {       // listener when connection is succeeded
                        @Override
                        public void onResponse(JSONArray response) {
                            // JSONObject has to be dealt with try-catch else compile error
                            try {
                                JSONObject data = response.getJSONObject(0);    // Just assuming there's only 1 element
                                userid = data.getInt("userid"); // obtain the field "userid"

                                if (userid == -1) {     // if email/pwd did not match or doesn't exist
                                    // spit out error
                                    Toast.makeText(User_login.this, "Wrong credentials, or the user does not exist. ", Toast.LENGTH_SHORT).show();
                                } else {
                                    // make a transition to Profile page with an argument passed over
                                    Intent OpenSearch = new Intent(User_login.this, Trip_Search.class);
                                    OpenSearch.putExtra("userid",userid);  // passing the argument
                                    startActivity(OpenSearch);
                                }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
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
        });

    }

    //TODO: complete rejection conditions
    //Here only checks whether the bar is filled
    //Should be able to check authentication
    public boolean ToReject(EditText check){
        boolean rejected = false;
        if(check.getText().toString().equals("")) rejected = true;
        return rejected;
    }

}
