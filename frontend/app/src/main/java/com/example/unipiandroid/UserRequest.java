package com.example.unipiandroid;

import static com.example.unipiandroid.MyApp.getContext;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.unipiandroid.ui.communication.AdminInboxFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRequest extends AppCompatActivity {

    TextView textViewName,textViewPhone,textViewEmail,textViewMessage;
    SharedPreferences sharedPreferences;
    RequestQueue mRequestQueue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.user_request);

        Button back = findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        final ImageView unipiLogo = findViewById(R.id.unipiLogo2);
        unipiLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

        sharedPreferences = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("Username","");
        String name = sharedPreferences.getString("Name","");

        final TextView loginText = findViewById(R.id.loginText2);

        //if user is logged in
        if(!username.isEmpty() && !name.isEmpty()) {
            loginText.setText("ΑΠΟΣΥΝΔΕΣΗ");
            loginText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    kill_activity();
                    startActivity(new Intent(getContext(),MainActivity.class));
                }
            });
        } else {    //else redirect to login page
            loginText.setText("ΣΥΝΔΕΣΗ");
            loginText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kill_activity();
                    startActivity(new Intent(getContext(), LoginPage.class));
                }
            });
        }

        sharedPreferences = getSharedPreferences("MyUserPrefs",MODE_PRIVATE);
        textViewName = findViewById(R.id.inboxUsername);
        textViewEmail = findViewById(R.id.inboxEmail);
        textViewPhone = findViewById(R.id.inboxPhone);
        textViewMessage = findViewById(R.id.inboxText);

        int id = sharedPreferences.getInt("msg_id",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("msg_id");
        editor.apply();

        String url = "http://192.168.1.14:8080/api/messages/";

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(getMessageData(url,id));


    }

    JsonObjectRequest getMessageData(String url, int id) {
        return new JsonObjectRequest(
                Request.Method.GET,
                url + id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getInt("statusCode")==200) {    //STATUS OK
                                JSONObject message = response.getJSONObject("data").getJSONObject("user_messages");
                                textViewName.setText(message.getString("fullname"));
                                textViewName.setClickable(false);
                                textViewPhone.setText(message.getString("phone"));
                                textViewPhone.setClickable(false);
                                textViewEmail.setText(message.getString("email"));
                                textViewEmail.setClickable(false);
                                textViewMessage.setText(message.getString("message"));
                                textViewMessage.setClickable(false);
                            } else {
                                Toast.makeText(getApplicationContext(),"Υπήρξε πρόβλημα στο σέρβερ, δοκιμάστε ξανά.",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"Υπήρξε πρόβλημα στο σέρβερ, δοκιμάστε ξανά.",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Υπήρξε πρόβλημα στο σέρβερ, δοκιμάστε ξανά.",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    void kill_activity() {
        finish();
    }


}
