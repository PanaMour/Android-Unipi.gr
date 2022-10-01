package com.example.unipiandroid.ui.communication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.example.unipiandroid.UserRequest;
import com.example.unipiandroid.databinding.FragmentAdminInboxBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminInboxFragment extends Fragment {
    private FragmentAdminInboxBinding binding;

    RequestQueue mRequestQueue;
    SharedPreferences sharedPreferences;
    List<TextView> messageList = new ArrayList<TextView>();
    List<TextView> dateList = new ArrayList<TextView>();
    CheckBox cb1,cb2,cb3;
    private Button previousButton,nextButton;
    private TextView currentPageTextView;
    private int currentPage=1;
    private final int pageSize=3;
    private int totalElements=0;
    private int totalPages;
    int[] msg_ids = {-1,-1,-1};
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAdminInboxBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView loginText = root.findViewById(R.id.loginText2);
        final ImageView unipiLogo = root.findViewById(R.id.unipiLogo2);
        final Button deleteButton = root.findViewById(R.id.deleteButton);
        final Button readButton = root.findViewById(R.id.readButton);
        deleteButton.setEnabled(false);
        readButton.setEnabled(false);

        messageList.add(root.findViewById(R.id.request));
        messageList.add(root.findViewById(R.id.request2));
        messageList.add(root.findViewById(R.id.request3));
        dateList.add(root.findViewById(R.id.request_text));
        dateList.add(root.findViewById(R.id.request_text2));
        dateList.add(root.findViewById(R.id.request_text3));
        cb1 = root.findViewById(R.id.requestCheckBox);
        cb2 = root.findViewById(R.id.requestCheckBox2);
        cb3 = root.findViewById(R.id.requestCheckBox3);
        previousButton = (Button) root.findViewById(R.id.previousButton2);
        previousButton.setVisibility(View.INVISIBLE);
        nextButton = (Button) root.findViewById(R.id.nextButton2);
        currentPageTextView = root.findViewById(R.id.pageNumber);


        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && msg_ids[0]>0) {
                    cb2.setChecked(false);
                    cb3.setChecked(false);
                    deleteButton.setEnabled(true);
                    readButton.setEnabled(true);
                } else if(isChecked){
                    deleteButton.setEnabled(false);
                    readButton.setEnabled(false);
                    cb2.setChecked(false);
                    cb3.setChecked(false);
                } else {
                    deleteButton.setEnabled(false);
                    readButton.setEnabled(false);
                }
            }
        });

        cb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && msg_ids[1]>0) {
                    cb1.setChecked(false);
                    cb3.setChecked(false);
                    deleteButton.setEnabled(true);
                    readButton.setEnabled(true);
                } else if(isChecked){
                    deleteButton.setEnabled(false);
                    readButton.setEnabled(false);
                    cb1.setChecked(false);
                    cb3.setChecked(false);
                } else {
                    deleteButton.setEnabled(false);
                    readButton.setEnabled(false);
                }
            }
        });

        cb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && msg_ids[2]>0) {
                    cb2.setChecked(false);
                    cb1.setChecked(false);
                    deleteButton.setEnabled(true);
                    readButton.setEnabled(true);
                } else if(isChecked) {
                    cb2.setChecked(false);
                    cb1.setChecked(false);
                } else {
                    deleteButton.setEnabled(false);
                    readButton.setEnabled(false);
                }
            }
        });


        sharedPreferences = getActivity().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        mRequestQueue = Volley.newRequestQueue(requireContext());
        String url = "http://192.168.1.14:8080/api/messages/page/";
        mRequestQueue.add(getMessagePageRequest(url,1));

        currentPage=1;
        currentPageTextView.setText(Integer.toString(currentPage));
        previousButton.setVisibility(View.INVISIBLE);

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPage-1>1) {
                    currentPage-=1;
                    currentPageTextView.setText(Integer.toString(currentPage));
                    mRequestQueue.add(getMessagePageRequest(url,currentPage));
                } else if(currentPage-1==1) {
                    currentPage-=1;
                    currentPageTextView.setText(Integer.toString(currentPage));
                    previousButton.setVisibility(View.INVISIBLE);
                    mRequestQueue.add(getMessagePageRequest(url,currentPage));
                }
                cb1.setChecked(false);
                cb2.setChecked(false);
                cb3.setChecked(false);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentPage+1<totalPages) {
                    currentPage+=1;
                    currentPageTextView.setText(Integer.toString(currentPage));
                    mRequestQueue.add(getMessagePageRequest(url,currentPage));
                } else if (currentPage+1==totalPages) {
                    currentPage+=1;
                    nextButton.setVisibility(View.INVISIBLE);
                    currentPageTextView.setText(Integer.toString(currentPage));
                    mRequestQueue.add(getMessagePageRequest(url,currentPage));
                }
                cb1.setChecked(false);
                cb2.setChecked(false);
                cb3.setChecked(false);
            }
        });


        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(cb1.isChecked() && msg_ids[0]>0) {
                    editor.putInt("msg_id",msg_ids[0]);
                    editor.apply();
                    startActivity(new Intent(getContext(), UserRequest.class));
                } else if (cb2.isChecked() && msg_ids[1]>0) {
                    editor.putInt("msg_id",msg_ids[1]);
                    editor.apply();
                    startActivity(new Intent(getContext(), UserRequest.class));
                } else if (cb3.isChecked() && msg_ids[2]>0) {
                    editor.putInt("msg_id",msg_ids[2]);
                    editor.apply();
                    startActivity(new Intent(getContext(), UserRequest.class));
                } else {
                    //Toast.makeText();
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url2 = "http://192.168.1.14:8080/api/messages/";
                if(cb1.isChecked() && msg_ids[0]>0) {
                    mRequestQueue.add(createDeleteRequest(url2,msg_ids[0]));
                } else if (cb2.isChecked() && msg_ids[1]>0) {
                    mRequestQueue.add(createDeleteRequest(url2,msg_ids[1]));
                } else if (cb3.isChecked() && msg_ids[2]>0) {
                    mRequestQueue.add(createDeleteRequest(url2,msg_ids[2]));
                }
            }
        });

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("Username","");
        String name = sharedPreferences.getString("Name","");

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

        unipiLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    JsonObjectRequest createDeleteRequest(String url,int id) {
        return new JsonObjectRequest(
                Request.Method.DELETE,
                url+id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("statusCode")==204) {   //NO CONTENT
                                Toast.makeText(getContext(),"Επιτυχής διαγραφή",Toast.LENGTH_LONG).show();
                                kill_activity();
                                startActivity(new Intent(getContext(), MainActivity.class));
                            } else {
                                Toast.makeText(getContext(),"Σφάλμα κατά τη διαγραφή",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(),"Σφάλμα κατά τη διαγραφή",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Σφάλμα κατά τη διαγραφή",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private JsonObjectRequest getMessagePageRequest(String url,int page) {
        return new JsonObjectRequest(
                Request.Method.GET,
                url+Integer.toString(page-1)+"/3",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.getInt("statusCode")==200) {
                                JSONObject msg_response = response.getJSONObject("data").getJSONObject("user_messages");
                                JSONArray msgs = msg_response.getJSONArray("content");
                                totalPages = msg_response.getInt("totalPages");
                                if(page<totalPages) {
                                    nextButton.setVisibility(View.VISIBLE);
                                } else {
                                    nextButton.setVisibility(View.INVISIBLE);
                                }
                                if(page>1) {
                                    previousButton.setVisibility(View.VISIBLE);
                                }
                                totalElements = msg_response.getInt("totalElements");
                                int elements = msg_response.getInt("numberOfElements");
                                if(elements==pageSize) {
                                    for(int i=0;i<pageSize;i++) {
                                        messageList.get(i).setText(msgs.getJSONObject(i).getString("fullname"));
                                        msg_ids[i] = msgs.getJSONObject(i).getInt("id");
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putInt("msg_id",msg_ids[i]);
                                        editor.apply();
                                        dateList.get(i).setText(msgs.getJSONObject(i).getString("date"));
                                    }
                                } else {
                                    for(int i=0;i<pageSize;i++) {
                                        if(i<elements) {
                                            messageList.get(i).setText(msgs.getJSONObject(i).getString("fullname"));
                                            msg_ids[i] = msgs.getJSONObject(i).getInt("id");
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putInt("msg_id",msg_ids[i]);
                                            editor.apply();
                                            dateList.get(i).setText(msgs.getJSONObject(i).getString("date"));
                                        } else {
                                            messageList.get(i).setText("-");
                                            dateList.get(i).setText("-");
                                            msg_ids[i] = -1;
                                        }
                                    }
                                }
                            } else { //bad response
                                Toast.makeText(getContext(),"Σφάλμα κατά την φόρτωση των αιτημάτων.",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(),"Σφάλμα κατά την φόρτωση των αιτημάτων.",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Σφάλμα κατά την φόρτωση των αιτημάτων.",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }
        );
    }

    void kill_activity() {
        getActivity().finish();
    }


}
