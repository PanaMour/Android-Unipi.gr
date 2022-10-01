package com.example.unipiandroid.ui.communication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.unipiandroid.LoginPage;
import com.example.unipiandroid.MainActivity;
import com.example.unipiandroid.R;
import com.example.unipiandroid.databinding.FragmentCommunicationBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class CommunicationFragment extends Fragment {

    EditText editTextName, editTextPhone, editTextEmail, editTextMessage;
    private RequestQueue mRequestQueue;
    private FragmentCommunicationBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCommunicationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        editTextName = root.findViewById(R.id.editTextTextPersonName);
        editTextPhone = root.findViewById(R.id.editTextPhone);
        editTextEmail = root.findViewById(R.id.editTextTextEmailAddress);
        editTextMessage = root.findViewById(R.id.editTextTextMultiLine);
        mRequestQueue = Volley.newRequestQueue(this.requireContext());

        final Button submitButton = (Button) root.findViewById(R.id.button);

        final TextView loginText = root.findViewById(R.id.loginText2);
        final ImageView unipiLogo = root.findViewById(R.id.unipiLogo2);
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), LoginPage.class));
            }
        });

        unipiLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String name = String.valueOf(editTextName.getText());
                String phone = String.valueOf(editTextPhone.getText());
                String email = String.valueOf(editTextEmail.getText());
                String message = String.valueOf(editTextMessage.getText());
                String phonePattern = "\\d{10}|(?:\\d{3}-){2}\\d{4}";
                Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

                if(name.isEmpty() || email.isEmpty() || message.isEmpty()) {
                    Toast.makeText(getContext(), "Παρακαλώ συμπληρώστε τα υποχρεωτικά πεδία.", Toast.LENGTH_LONG).show();
                } else if (!phone.matches(phonePattern) && !phone.isEmpty()) {
                    Toast.makeText(getContext(), "Παρακαλώ συμπληρώστε σωστά το πεδίο του τηλεφώνου.", Toast.LENGTH_LONG).show();
                } else if (!emailPattern.matcher(email).find()) {
                    Toast.makeText(getContext(), "Παρακαλώ συμπληρώστε σωστά το πεδίο του email.", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject message_data = new JSONObject();
                    try {
                        message_data.put("date", LocalDate.now().toString());
                        message_data.put("fullname",name);
                        message_data.put("phone",phone);
                        message_data.put("email",email);
                        message_data.put("message",message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String url = "http://192.168.1.14:8080/api/messages/";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            message_data,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("Rest response",response.toString());
                                    try {
                                        if(response.getInt("statusCode")==201) {
                                            Toast.makeText(getContext(), "Το μήνυμα στάλθηκε επιτυχώς.", Toast.LENGTH_LONG).show();
                                            kill_activity();
                                            startActivity(new Intent(getContext(), MainActivity.class));
                                        } else {
                                            Toast.makeText(getContext(), "Υπήρξε σφάλμα κατά την αποστολή. Προσπαθήστε ξανά.", Toast.LENGTH_LONG).show();
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(getContext(), "Υπήρξε σφάλμα κατά την αποστολή. Προσπαθήστε ξανά.", Toast.LENGTH_LONG).show();
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getContext(), "Υπήρξε σφάλμα κατά την αποστολή. Προσπαθήστε ξανά.", Toast.LENGTH_LONG).show();
                                    error.printStackTrace();
                                }
                            }
                    );
                    mRequestQueue.add(jsonObjectRequest);
                }

            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    void kill_activity() {
        getActivity().finish();
    }
}
