package com.example.unipiandroid.ui.support;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.unipiandroid.LoginPage;
import com.example.unipiandroid.MainActivity;
import com.example.unipiandroid.R;
import com.example.unipiandroid.databinding.FragmentStudentslifeBinding;

public class StudentsLifeFragment extends Fragment {
    private FragmentStudentslifeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStudentslifeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView text = (TextView) root.findViewById(R.id.textView35);
        text.setMovementMethod(LinkMovementMethod.getInstance());

        SharedPreferences sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("Username","");
        String name = sharedPreferences.getString("Name","");

        final TextView loginText = root.findViewById(R.id.loginText2);

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

        final ImageView unipiLogo = root.findViewById(R.id.unipiLogo2);

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

    void kill_activity() {
        getActivity().finish();
    }
}
