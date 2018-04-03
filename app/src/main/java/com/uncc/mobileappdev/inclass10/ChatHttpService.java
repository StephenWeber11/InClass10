package com.uncc.mobileappdev.inclass10;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Stephen on 4/2/2018.
 */

public class ChatHttpService {

    private final OkHttpClient client = new OkHttpClient();
    private Activity activity;

    public ChatHttpService(Activity activity) {
        this.activity = activity;
    }

    protected void performRegister(String userName, String password, String firstName, String lastName) {

        RequestBody formBody = new FormBody.Builder()
                .add("email", userName)
                .add("password", password)
                .add("fname", firstName)
                .add("lname", lastName)
                .build();
        final Request request = new Request.Builder()
                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/signup")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Http", "Call Failed!");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("Http", "Register Response: " + responseBody);

                Gson gson = new Gson();
                TokenResponse tokenResponse = gson.fromJson(responseBody, TokenResponse.class);
            }
        });
    }

    protected void performLogin(String userName, String password) {
        RequestBody formBody = new FormBody.Builder()
                .add("email", userName)
                .add("password", password)
                .build();
        final Request request = new Request.Builder()
                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/login")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Http", "Call Failed!");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("Http", "Login Response: " + responseBody);
                parseUser(responseBody);

            }
        });
    }

    protected void getThreadList(String token) {

        RequestBody formBody = new FormBody.Builder()
                .add("email", token)
                .build();

        final Request request = new Request.Builder()
                .addHeader("Authorization","BEARER " + token)
                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/thread")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Http", "Call Failed!");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("Http", "Response: " + responseBody);
            }
        });
    }

    protected void parseUser(String json){
        Gson gson = new Gson();
        TokenResponse tokenResponse = gson.fromJson(json, TokenResponse.class);

        if(!tokenResponse.getStatus().equals("error")){
            getThreadList(tokenResponse.getToken());
            saveToken(tokenResponse.getToken());
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Error Logging In!", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    protected void saveToken(String token) {
        SharedPreferences sharedPref = activity.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(activity.getString(R.string.preference_file_key), token);
        editor.commit();
    }

}
