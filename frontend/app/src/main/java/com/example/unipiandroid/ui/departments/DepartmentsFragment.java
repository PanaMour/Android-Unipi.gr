package com.example.unipiandroid.ui.departments;

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
import androidx.lifecycle.ViewModelProvider;

import com.example.unipiandroid.LoginPage;
import com.example.unipiandroid.MainActivity;
import com.example.unipiandroid.R;
import com.example.unipiandroid.databinding.FragmentDepartmentsBinding;

public class DepartmentsFragment extends Fragment {

    private FragmentDepartmentsBinding binding;
    SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDepartmentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView text1 = (TextView) root.findViewById(R.id.textView60);
        TextView text2 = (TextView) root.findViewById(R.id.textView61);
        TextView text3 = (TextView) root.findViewById(R.id.textView62);
        TextView text4 = (TextView) root.findViewById(R.id.textView63);
        TextView text5 = (TextView) root.findViewById(R.id.textView64);
        TextView text6 = (TextView) root.findViewById(R.id.textView65);
        TextView text7 = (TextView) root.findViewById(R.id.textView66);
        TextView text8 = (TextView) root.findViewById(R.id.textView67);
        TextView text9 = (TextView) root.findViewById(R.id.textView68);
        TextView text10 = (TextView) root.findViewById(R.id.textView69);
        TextView text11 = (TextView) root.findViewById(R.id.textView70);
        TextView text12 = (TextView) root.findViewById(R.id.textView71);
        TextView text13 = (TextView) root.findViewById(R.id.textView72);
        text1.setMovementMethod(LinkMovementMethod.getInstance());
        text2.setMovementMethod(LinkMovementMethod.getInstance());
        text3.setMovementMethod(LinkMovementMethod.getInstance());
        text4.setMovementMethod(LinkMovementMethod.getInstance());
        text5.setMovementMethod(LinkMovementMethod.getInstance());
        text6.setMovementMethod(LinkMovementMethod.getInstance());
        text7.setMovementMethod(LinkMovementMethod.getInstance());
        text8.setMovementMethod(LinkMovementMethod.getInstance());
        text9.setMovementMethod(LinkMovementMethod.getInstance());
        text10.setMovementMethod(LinkMovementMethod.getInstance());
        text11.setMovementMethod(LinkMovementMethod.getInstance());
        text12.setMovementMethod(LinkMovementMethod.getInstance());
        text13.setMovementMethod(LinkMovementMethod.getInstance());


        final ImageView unipiLogo = root.findViewById(R.id.unipiLogo2);

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
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
