package com.example.unipiandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginPage extends AppCompatActivity {

    EditText editTextUsername, editTextPassword;
    private RequestQueue mRequestQueue;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login_page);

        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        editTextUsername = findViewById(R.id.Username);
        editTextPassword = findViewById(R.id.Password);
        sharedPreferences = getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);

        final ImageView home = (ImageView) findViewById(R.id.unipiloginlogo);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(LoginPage.this, MainActivity.class));
            }
        });

        final Button loginButton = (Button) findViewById(R.id.LoginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = String.valueOf(editTextUsername.getText());
                String password = String.valueOf(editTextPassword.getText());

                if(!username.isEmpty() && !password.isEmpty()) {
                    String url = "http://192.168.1.14:8080/api/admin/login";
                    JsonObjectRequest loginRequest = createLoginRequest(url,username,password);
                    mRequestQueue.add(loginRequest);
                } else {
                    Toast.makeText(getApplicationContext(),"Παρακαλώ εισάγετε τα στοιχεία σας.",Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    private JsonObjectRequest createLoginRequest(String url,String username, String password) {
        JSONObject login_data = new JSONObject();
        try {
            login_data.put("username",username);
            login_data.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                login_data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("Rest response:",response.toString());

                            if(response.getInt("statusCode")==200) {
                                JSONObject admin = response.getJSONObject("data").getJSONObject("admin");
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("Username",admin.getString("username"));
                                editor.putString("Name",admin.getString("firstname")+" "+admin.getString("lastname"));
                                editor.apply();

                                Toast.makeText(getApplicationContext(), "Επιτυχής σύνδεση!", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(LoginPage.this, MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), "Λάθος στοιχεία!", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Internal server error. Please try again.", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Internal server error. Please try again.", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );
        return jsonObjectRequest;
    }

}