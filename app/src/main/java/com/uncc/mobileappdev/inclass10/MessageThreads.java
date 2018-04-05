package com.uncc.mobileappdev.inclass10;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.uncc.mobileappdev.inclass10.ChatHttpService.TOKEN_THREADS_BUNDLE_KEY;

public class MessageThreads extends AppCompatActivity {

    TextView userName;
    ImageButton logout;
    TokenResponse tokenResponse;
    ThreadList threadList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_threads);
        setTitle("Message Threads");

        userName = findViewById(R.id.UsernameMessage);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ChatHttpService.TOKEN_RESPONSE_INTENT_KEY);
        tokenResponse = (TokenResponse) bundle.getSerializable(ChatHttpService.TOKEN_RESPONSE_BUNDLE_KEY);
        threadList =(ThreadList) bundle.getSerializable(ChatHttpService.TOKEN_THREADS_BUNDLE_KEY);

        userName.setText(tokenResponse.getUser_fname());

        ArrayList<String> truncatedThreadList = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            truncatedThreadList.add(threadList.getThreads().get(i).getTitle());
        }

        ListView listView = (ListView)findViewById(R.id.threadListView);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                        android.R.id.text1, truncatedThreadList);

        listView.setAdapter(adapter);

        logout = findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteToken();
                Intent intent = new Intent(MessageThreads.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void deleteToken() {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(getString(R.string.preference_file_key));
        editor.commit();
    }
}
