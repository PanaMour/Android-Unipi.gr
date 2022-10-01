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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class Announcement extends AppCompatActivity {

    RequestQueue mRequestQueue;
    TextView titleTextView,detailsTextView,authorTextView;
    SharedPreferences sharedPreferences;
    TextView loginText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.announcement);

        sharedPreferences = getSharedPreferences("MyUserPrefs",Context.MODE_PRIVATE);
        int id = sharedPreferences.getInt("News_id",0);
        String username = sharedPreferences.getString("Username","");


        titleTextView = findViewById(R.id.announcement_title);
        detailsTextView = findViewById(R.id.details);
        authorTextView = findViewById(R.id.author);
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.1.14:8080/api/news/"+id;
        mRequestQueue.add(getNewsRequest(url));

        sharedPreferences = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
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
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            });
        } else {    //else redirect to login page
            loginText.setText("ΣΥΝΔΕΣΗ");
            loginText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    kill_activity();
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

        Button button = findViewById(R.id.deleteNewsButton);
        if(!username.isEmpty()) {
            button.setVisibility(View.VISIBLE);
            button.setEnabled(true);
        } else {
            button.setVisibility(View.INVISIBLE);
            button.setEnabled(false);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mRequestQueue.add(getDeleteRequest(url));
                /*
                manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse("https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf");
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                long reference = manager.enqueue(request);
                */
            }
        });
    }

    JsonObjectRequest getNewsRequest(String url) {
        return new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getInt("statusCode")==200) { //http status OK
                                titleTextView.setText(response.getJSONObject("data").getJSONObject("news").getString("title"));
                                detailsTextView.setText(response.getJSONObject("data").getJSONObject("news").getString("message"));
                                authorTextView.setText(response.getJSONObject("data").getJSONObject("news").getString("author"));
                            } else {
                                Toast.makeText(getApplicationContext(),"Σφάλμα κατά το φόρτωμα της ανακοίνωσης",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"Σφάλμα κατά το φόρτωμα της ανακοίνωσης",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Σφάλμα κατά το φόρτωμα της ανακοίνωσης",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );
    }

    JsonObjectRequest getDeleteRequest(String url) {
        return new JsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getInt("statusCode")==204) {    //http status NO CONTENT
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("news_id");
                                editor.apply();
                                Toast.makeText(getApplicationContext(),"Επιτυχής διαγραφή της ανακοίνωσης.",Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(),"Σφάλμα κατά τη διαγραφή της ανακοίνωσης.",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"Σφάλμα κατά τη διαγραφή της ανακοίνωσης.",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Σφάλμα κατά τη διαγραφή της ανακοίνωσης.",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );
    }

    void kill_activity() {
        finish();
    }

}
