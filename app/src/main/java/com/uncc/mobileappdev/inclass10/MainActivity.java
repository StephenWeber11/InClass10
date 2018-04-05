package com.uncc.mobileappdev.inclass10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ChatHttpService chatHttpService;

    private EditText userName;
    private EditText password;
    private Button login;
    private Button register;

    private String userParam;
    private String passParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Login");
        chatHttpService = new ChatHttpService(this);

        userName = findViewById(R.id.LoginEmail);
        password = findViewById(R.id.LoginPassword);
        login = findViewById(R.id.Loginbutton);
        register = findViewById(R.id.SignupButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userParam = userName.getText().toString();
                passParam = password.getText().toString();

                chatHttpService.performLogin(userParam, passParam);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });

    }
}
