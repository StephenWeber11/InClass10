package com.uncc.mobileappdev.inclass10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

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
    private ThreadList threadList;
    private TokenResponse tokenResponse;
    private Thread thread;

    public static final String TOKEN_RESPONSE_BUNDLE_KEY = "tokenResponseBundleKey";
    public static final String TOKEN_RESPONSE_INTENT_KEY = "tokenResponseIntentKey";
    public static final String TOKEN_THREADS_BUNDLE_KEY = "threadListBundleKey";

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
                parseUserForRegister(responseBody);
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
                parseUserForLogin(responseBody);

            }
        });
    }

    protected void getThreadList(String token) {

        if (tokenIsAuthenticated(token)) {

            final Request request = new Request.Builder()
                    .addHeader("Authorization", "BEARER " + token)
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
                    parseThreadList(responseBody);
                }
            });
        }  else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Authentication Issue. Please Login Again.", Toast.LENGTH_SHORT).show();;
                }
            });
        }
    }

    protected void addThread(Thread thread, String token) {
        if (tokenIsAuthenticated(token)) {

            RequestBody formBody = new FormBody.Builder()
                    .add("user_fname", thread.getUser_fname())
                    .add("user_lname", thread.getUser_lname())
                    .add("user_id", thread.getUser_id())
                    .add("id", thread.getId())
                    .add("title", thread.getTitle())
                    .add("created_at", thread.created_at)
                    .build();

            final Request request = new Request.Builder()
                    .addHeader("Authorization", "BEARER " + token)
                    .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/thread/add")
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
                    Log.d("Http", "Response: " + responseBody);
                }
            });
        }  else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Authentication Issue. Please Login Again.", Toast.LENGTH_SHORT).show();;
                }
            });
        }
    }

    protected void deleteThread(Thread thread, String token) {
        if (tokenIsAuthenticated(token)) {

            final Request request = new Request.Builder()
                    .addHeader("Authorization", "BEARER " + token)
                    .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/thread/delete/" + thread.getId())
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
        }  else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Authentication Issue. Please Login Again.", Toast.LENGTH_SHORT).show();;
                }
            });
        }
    }

//    protected void getMessages(String token, Message message) {
//
//        Request request = new Request.Builder()
//                .url("http://ec2-54-166-14-133.compute-1.amazonaws.com/api/file/" + message.getFileThumbnailId())
//                .header("Authorization", "BEARER " + token)
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                (activity).runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(activity, "Invalid Id.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    InputStream responseStr = response.body().byteStream();
//                    final Bitmap btmp = BitmapFactory.decodeStream(responseStr);
//
//                    (activity).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            // imageMessage.setImageBitmap(btmp);
//                        }
//                    });
//
//                } else {
//                    (activity).runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(activity, "Unable to fetch Image", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            }
//        });
//    }

    protected void parseUserForLogin(String json){
        Gson gson = new Gson();
        tokenResponse = gson.fromJson(json, TokenResponse.class);

        if(!tokenResponse.getStatus().equals("error")){
            saveToken(tokenResponse.getToken());
            getThreadList(tokenResponse.getToken());

        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Error Logging In!", Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    protected void parseUserForRegister(String json){
        Gson gson = new Gson();
        tokenResponse = gson.fromJson(json, TokenResponse.class);

        if(!tokenResponse.getStatus().equals("error")){
            saveToken(tokenResponse.getToken());
            getThreadList(tokenResponse.getToken());

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Account Created", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "UhOh! " + tokenResponse.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    protected void parseThreadList(String json){
        Gson gson = new Gson();
        threadList = gson.fromJson(json, ThreadList.class);

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity, MessageThreads.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(TOKEN_RESPONSE_BUNDLE_KEY, tokenResponse);
                bundle.putSerializable(TOKEN_THREADS_BUNDLE_KEY, threadList);
                intent.putExtra(TOKEN_RESPONSE_INTENT_KEY, bundle);
                activity.startActivity(intent);
            }
        });

        Log.d("Http", "ThreadList size: " + threadList.getThreads().size());
    }

    protected void saveToken(String token) {
        SharedPreferences sharedPref = activity.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(activity.getString(R.string.preference_file_key), token);
        editor.commit();
    }

    protected boolean tokenIsAuthenticated(String token) {
        SharedPreferences sharedPref = activity.getSharedPreferences(activity.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String savedToken = sharedPref.getString(activity.getString(R.string.preference_file_key), null);

        return token.equals(savedToken);

    }

}
