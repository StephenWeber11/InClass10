package com.uncc.mobileappdev.inclass10;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.uncc.mobileappdev.inclass10.ChatHttpService.TOKEN_THREADS_BUNDLE_KEY;

public class MessageThreads extends AppCompatActivity {

    TextView userName;
    ImageButton logout;
    TokenResponse tokenResponse;
    ThreadList threadList;
    ImageButton addButton;
    EditText inputTopic;
    MessageThreadsAdapter adapter;
    ListView listView;
    ChatHttpService chatHttpService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_threads);
        setTitle("Message Threads");

        chatHttpService = new ChatHttpService(this);

        userName = findViewById(R.id.UsernameMessage);
        addButton = findViewById(R.id.imageButtonAdd);
        inputTopic = findViewById(R.id.inputTopic);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ChatHttpService.TOKEN_RESPONSE_INTENT_KEY);
        tokenResponse = (TokenResponse) bundle.getSerializable(ChatHttpService.TOKEN_RESPONSE_BUNDLE_KEY);
        threadList =(ThreadList) bundle.getSerializable(ChatHttpService.TOKEN_THREADS_BUNDLE_KEY);

        userName.setText(tokenResponse.getUser_fname());

        final ArrayList<Thread> truncatedThreadList = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            Thread threadToAdd = threadList.getThreads().get(i);
            threadToAdd.setUserAdded(false);
            truncatedThreadList.add(threadToAdd);
        }

        listView = findViewById(R.id.threadListView);
        adapter = new MessageThreadsAdapter(this, R.layout.activity_message_threads, truncatedThreadList, tokenResponse);
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

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputTopic.getText().toString() != null && inputTopic.getText().toString() != "") {
                    int size = threadList.getThreads().size();

                    Thread thread = new Thread();
                    thread.setUser_fname(tokenResponse.getUser_fname());
                    thread.setUser_lname(tokenResponse.getUser_lname());
                    thread.setUserAdded(true);
                    thread.setTitle(inputTopic.getText().toString());
                    thread.setUser_id(tokenResponse.getUser_email());
                    thread.setId(threadList.getThreads().get(size-1).getId()+1);
                    thread.setCreated_at("SomeDate");

                    chatHttpService.addThread(thread, tokenResponse.getToken());

                    truncatedThreadList.add(thread);

                    adapter = new MessageThreadsAdapter(MessageThreads.this, R.layout.activity_message_threads, truncatedThreadList, tokenResponse);
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(MessageThreads.this, "Please enter a topic to continue!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
