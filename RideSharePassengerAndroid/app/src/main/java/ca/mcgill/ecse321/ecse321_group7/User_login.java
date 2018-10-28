package ca.mcgill.ecse321.ecse321_group7;

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

import static android.widget.Toast.LENGTH_SHORT;

public class User_login extends AppCompatActivity {

    //Jump to user profile page
    private Button login;
    //Jump to singup page
    private Button Signup;

    private EditText Email;
    private EditText Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                    Intent OpenProfile = new Intent(User_login.this, Profile_page.class);
                    startActivity(OpenProfile);
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
