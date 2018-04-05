package com.uncc.mobileappdev.inclass10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUp extends AppCompatActivity {

    private ChatHttpService chatHttpService;

    private Button cancel;
    private Button signup;

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private EditText passwordConfirm;

    private String fName;
    private String lName;
    private String user;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Signup");
        chatHttpService = new ChatHttpService(this);

        cancel = findViewById(R.id.CancelButton);
        signup = findViewById(R.id.SignupButton);
        firstName = findViewById(R.id.FirstName);
        lastName = findViewById(R.id.LastName);
        email = findViewById(R.id.SignupEmail);
        password = findViewById(R.id.SignupPassword);
        passwordConfirm = findViewById(R.id.SignupRePassword);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fName = firstName.getText().toString();
                lName = lastName.getText().toString();
                user = email.getText().toString();

                if(doPasswordsMatch(password.getText().toString(), passwordConfirm.getText().toString())) {
                    pass = password.getText().toString();
                    chatHttpService.performRegister(user, pass, fName, lName);

                } else {
                    Toast.makeText(SignUp.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }

    protected boolean doPasswordsMatch(String passOne, String passTwo){
        return passOne.equals(passTwo);
    }
}
