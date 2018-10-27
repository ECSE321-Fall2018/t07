package ca.mcgill.ecse321.ecse321_group7;

import android.hardware.camera2.CameraManager;
import android.icu.text.UnicodeSetSpanner;
import android.media.effect.Effect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.mcgill.ecse321.ecse321_group7.User_login;

public class User_Signup extends AppCompatActivity {

    private EditText FirstName;
    private EditText LastName;
    private EditText Email;
    //datatype of phone number in user class, backend, is string
    private EditText Phone_no;
    private EditText Password;
    //avoid repeating names of signup button, but doesn't really matter as they are private
    private Button FormalSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirstName = findViewById(R.id.firstname);
        LastName = findViewById(R.id.lastname);
        Email = findViewById(R.id.Email);
        Phone_no = findViewById(R.id.phone_no);
        Password = findViewById(R.id.Password);

        FormalSignUp = findViewById(R.id.SignUp);
        FormalSignUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(ToReject(FirstName)||ToReject(LastName)||ToReject(Email)||ToReject(Phone_no)||ToReject(Password))
                    Toast.makeText(User_Signup.this,"Please fill in all the information", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //!!Have problem import user_login class
    public boolean ToReject(EditText check){
        boolean rejected = false;
        if(check.getText().toString().equals("")) rejected = true;
        return rejected;
    }

}
