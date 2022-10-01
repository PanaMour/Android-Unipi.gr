package com.example.unipiandroid.ui.home;

import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.unipiandroid.Announcement;
import com.example.unipiandroid.LoginPage;
import com.example.unipiandroid.MainActivity;
import com.example.unipiandroid.R;
import com.example.unipiandroid.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    SharedPreferences sharedPreferences;
    private List<TextView> announcementList = new ArrayList<TextView>();
    private List<TextView> dateList = new ArrayList<TextView>();
    private Button previousButton,nextButton;
    private TextView currentPageTextView;
    private int currentPage=1;
    private final int pageSize=3;
    private int totalElements=0;
    private int totalPages;
    String currentType="CURRENT_NEWS";
    final String url = "http://192.168.1.14:8080/api/news/";
    private RequestQueue mRequestQueue;
    private int[] news_ids = new int[3];

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final Button buttonepikairotita = root.findViewById(R.id.epikairotita);
        final Button buttonanakoinwseis = root.findViewById(R.id.anakoinwseis);
        final Button buttonekdhlwseis = root.findViewById(R.id.ekdhlwseis);
        final Button buttonfoititikathemata = root.findViewById(R.id.foititikathemata);

        dateList.add(root.findViewById(R.id.announcement_text));    //O NIK TA EBALE EDW
        announcementList.add(root.findViewById(R.id.announcement));
        dateList.add(root.findViewById(R.id.announcement_text2));
        announcementList.add(root.findViewById(R.id.announcement2));
        dateList.add(root.findViewById(R.id.announcement_text3));
        announcementList.add(root.findViewById(R.id.announcement3));
        mRequestQueue = Volley.newRequestQueue(this.requireContext());
        previousButton = (Button) root.findViewById(R.id.previousButton);
        previousButton.setVisibility(View.INVISIBLE);
        nextButton = (Button) root.findViewById(R.id.nextButton);
        nextButton.setVisibility(View.INVISIBLE);
        currentPageTextView = root.findViewById(R.id.currentPage);

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("Username","");
        String name = sharedPreferences.getString("Name","");

        final TextView loginText = root.findViewById(R.id.loginText);

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

        buttonepikairotita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonepikairotita.setBackgroundColor(getResources().getColor(R.color.red_unipi));
                buttonanakoinwseis.setBackgroundResource(R.drawable.button_border);
                buttonfoititikathemata.setBackgroundResource(R.drawable.button_border);
                buttonekdhlwseis.setBackgroundResource(R.drawable.button_border);


                currentPage=1;
                currentPageTextView.setText(Integer.toString(currentPage));
                previousButton.setVisibility(View.INVISIBLE);
                currentType = "CURRENT_NEWS";
                mRequestQueue.add(getNewsPageRequest(url,currentType,currentPage));
            }
        });
        buttonanakoinwseis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonepikairotita.setBackgroundResource(R.drawable.button_border);
                buttonanakoinwseis.setBackgroundColor(getResources().getColor(R.color.red_unipi));
                buttonfoititikathemata.setBackgroundResource(R.drawable.button_border);
                buttonekdhlwseis.setBackgroundResource(R.drawable.button_border);

                currentPage=1;
                previousButton.setVisibility(View.INVISIBLE);
                currentPageTextView.setText(Integer.toString(currentPage));
                currentType = "ANNOUNCEMENT";
                mRequestQueue.add(getNewsPageRequest(url,currentType,currentPage));
            }
        });
        buttonekdhlwseis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonepikairotita.setBackgroundResource(R.drawable.button_border);
                buttonanakoinwseis.setBackgroundResource(R.drawable.button_border);
                buttonfoititikathemata.setBackgroundResource(R.drawable.button_border);
                buttonekdhlwseis.setBackgroundColor(getResources().getColor(R.color.red_unipi));

                currentPage=1;
                previousButton.setVisibility(View.INVISIBLE);
                currentPageTextView.setText(Integer.toString(currentPage));
                currentType = "EVENT";
                mRequestQueue.add(getNewsPageRequest(url,currentType,currentPage));
            }
        });
        buttonfoititikathemata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonepikairotita.setBackgroundResource(R.drawable.button_border);
                buttonanakoinwseis.setBackgroundResource(R.drawable.button_border);
                buttonfoititikathemata.setBackgroundColor(getResources().getColor(R.color.red_unipi));
                buttonekdhlwseis.setBackgroundResource(R.drawable.button_border);

                currentPage=1;
                previousButton.setVisibility(View.INVISIBLE);
                currentType = "STUDENT_NEWS";
                currentPageTextView.setText(Integer.toString(currentPage));
                mRequestQueue.add(getNewsPageRequest(url,currentType,currentPage));
            }
        });




        //default request for EPIKAIROTHTA
        currentPage=1;
        currentPageTextView.setText(Integer.toString(currentPage));
        previousButton.setVisibility(View.INVISIBLE);
        mRequestQueue.add(getNewsPageRequest(url,currentType,currentPage));

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPage-1>1) {
                    currentPage-=1;
                    currentPageTextView.setText(Integer.toString(currentPage));
                    mRequestQueue.add(getNewsPageRequest(url,currentType,currentPage));
                } else if(currentPage-1==1) {
                    currentPage-=1;
                    currentPageTextView.setText(Integer.toString(currentPage));
                    previousButton.setVisibility(View.INVISIBLE);
                    mRequestQueue.add(getNewsPageRequest(url,currentType,currentPage));
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPage+1<totalPages) {
                    currentPage+=1;
                    currentPageTextView.setText(Integer.toString(currentPage));
                    mRequestQueue.add(getNewsPageRequest(url,currentType,currentPage));
                } else if (currentPage+1==totalPages) {
                    currentPage+=1;
                    nextButton.setVisibility(View.INVISIBLE);
                    currentPageTextView.setText(Integer.toString(currentPage));
                    mRequestQueue.add(getNewsPageRequest(url,currentType,currentPage));
                }


            }
        });

        final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private JsonObjectRequest getNewsPageRequest(String url, String newsType, int page) {
        return new JsonObjectRequest(
                Request.Method.GET,
                url+newsType+"/"+Integer.toString(page-1)+"/3",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getInt("statusCode")==200) {    //Http status code OK
                                JSONObject news_response = response.getJSONObject("data").getJSONObject("news");
                                JSONArray news = news_response.getJSONArray("content");
                                totalPages = news_response.getInt("totalPages");
                                if(page<totalPages) {
                                    nextButton.setVisibility(View.VISIBLE);
                                } else {
                                    nextButton.setVisibility(View.INVISIBLE);
                                }
                                if(page>1) {
                                    previousButton.setVisibility(View.VISIBLE);
                                }
                                totalElements = news_response.getInt("totalElements");
                                int elements = news_response.getInt("numberOfElements");
                                if(elements==pageSize) {
                                    for(int i=0;i<pageSize;i++) {
                                        String bd = news.getJSONObject(i).getString("message");
                                        if(bd.length()>30) {
                                            announcementList.get(i).setText(news.getJSONObject(i).getString("title")+"\n"+bd.substring(0,30)+"...");
                                        } else {
                                            announcementList.get(i).setText(news.getJSONObject(i).getString("title")+"\n"+bd);
                                        }
                                        news_ids[i] = news.getJSONObject(i).getInt("id");
                                        int id_ix = i;
                                        announcementList.get(i).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putInt("News_id",news_ids[id_ix]);
                                                editor.apply();
                                                kill_activity();
                                                startActivity(new Intent(getContext(), Announcement.class));
                                            }
                                        });
                                        String dt = news.getJSONObject(i).getString("date");
                                        dt = dt.substring(5)+"\n\n"+dt.substring(0,4);
                                        dateList.get(i).setText(dt);
                                    }
                                } else {
                                    for(int i=0;i<pageSize;i++) {
                                        if(i<elements) {
                                            String bd = news.getJSONObject(i).getString("message");
                                            if(bd.length()>30) {
                                                announcementList.get(i).setText(news.getJSONObject(i).getString("title")+"\n"+bd.substring(0,30)+"...");
                                            } else {
                                                announcementList.get(i).setText(news.getJSONObject(i).getString("title")+"\n"+bd);
                                            }
                                            news_ids[i] = news.getJSONObject(i).getInt("id");
                                            int id_ix = i;
                                            announcementList.get(i).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putInt("News_id",news_ids[id_ix]);
                                                    editor.apply();
                                                    kill_activity();
                                                    startActivity(new Intent(getContext(), Announcement.class));
                                                }
                                            });
                                            String dt = news.getJSONObject(i).getString("date");
                                            dt = dt.substring(5)+"\n\n"+dt.substring(0,4);
                                            dateList.get(i).setText(dt);
                                        } else {
                                            announcementList.get(i).setText("-");
                                            announcementList.get(i).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    //do nothing
                                                }
                                            });
                                            dateList.get(i).setText("-");
                                            news_ids[i] = -1;
                                        }
                                    }
                                }
                            } else {    //bad response
                                Toast.makeText(getContext(),"Πρόβλημα στη φόρτωση των ανακοινώσεων",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(),"Πρόβλημα στη φόρτωση των ανακοινώσεων",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
    }

    void kill_activity() {
        getActivity().finish();
    }

}