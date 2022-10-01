package com.example.unipiandroid;

import static com.example.unipiandroid.MyApp.getContext;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddAnnouncement extends AppCompatActivity {

    EditText editTextTitle,editTextBody;
    Spinner newsTypeDD;
    String[] newsTypes = new String[] {"ΕΠΙΚΑΙΡΟΤΗΤΑ","ΑΝΑΚΟΙΝΩΣΕΙΣ","ΕΚΔΗΛΩΣΕΙΣ","ΦΟΙΤΗΤΙΚΑ ΘΕΜΑΤΑ"};
    Button submitButton;
    RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.add_announcement);

        final TextView loginText = findViewById(R.id.loginText2);

        SharedPreferences sharedPreferences = getSharedPreferences("MyUserPrefs",Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("Username","");
        String fullname = sharedPreferences.getString("Name","");
        //if user is logged in
        if(!username.isEmpty() && !fullname.isEmpty()) {
            loginText.setText("ΑΠΟΣΥΝΔΕΣΗ");
            loginText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    finish();
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            });
        } else {    //else redirect to login page
            loginText.setText("ΣΥΝΔΕΣΗ");
            loginText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginPage.class));
                }
            });
        }

        final ImageView unipiLogo = findViewById(R.id.unipiLogo2);
        unipiLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });


        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        submitButton = (Button) findViewById(R.id.submit_news);
        editTextTitle = findViewById(R.id.editTitle);
        editTextBody = findViewById(R.id.editTextTextMultiLine);
        newsTypeDD = findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, newsTypes);
        newsTypeDD.setAdapter(adapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String url = "http://192.168.1.14:8080/api/news/";
                String newsType = newsTypeDD.getSelectedItem().toString();
                if ("ΕΠΙΚΑΙΡΟΤΗΤΑ".equals(newsType)) {
                    newsType = "CURRENT_NEWS";
                    mRequestQueue.add(makePostRequest(url, fullname, newsType));
                } else if ("ΑΝΑΚΟΙΝΩΣΕΙΣ".equals(newsType)) {
                    newsType = "ANNOUNCEMENT";
                    mRequestQueue.add(makePostRequest(url, fullname, newsType));
                } else if ("ΕΚΔΗΛΩΣΕΙΣ".equals(newsType)) {
                    newsType = "EVENT";
                    mRequestQueue.add(makePostRequest(url, fullname, newsType));
                } else if ("ΦΟΙΤΗΤΙΚΑ ΘΕΜΑΤΑ".equals(newsType)) {
                    newsType = "STUDENT_NEWS";
                    mRequestQueue.add(makePostRequest(url, fullname, newsType));
                } else {
                    Toast.makeText(getApplicationContext(), "Επιλέξτε είδος ανακοίνωσης.", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    JsonObjectRequest makePostRequest(String url, String fullname, String newsType) {
        String title = String.valueOf(editTextTitle.getText());
        String message = String.valueOf(editTextBody.getText());
        JSONObject news_data = new JSONObject();
        try {
            news_data.put("date",LocalDate.now());
            news_data.put("title",title);
            news_data.put("author",fullname);
            news_data.put("message",message);
            news_data.put("type",newsType);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new JsonObjectRequest(
                Request.Method.POST,
                url,
                news_data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                           if(response.getInt("statusCode")==201) { //http status CREATED
                               Toast.makeText(getApplicationContext(), "Επιτυχής δημιουργία ανακοίνωσης", Toast.LENGTH_LONG).show();
                               finish();
                               startActivity(new Intent(AddAnnouncement.this, MainActivity.class));
                           } else {
                               //toast error msg
                               Toast.makeText(getApplicationContext(), "Σφάλμα κατά τη δημιουργία της ανακοίνωσης! Δοκιμάστε ξανά", Toast.LENGTH_LONG).show();
                           }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Σφάλμα κατά τη δημιουργία της ανακοίνωσης! Δοκιμάστε ξανά", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Σφάλμα κατά τη δημιουργία της ανακοίνωσης! Δοκιμάστε ξανά", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );
    }

}
