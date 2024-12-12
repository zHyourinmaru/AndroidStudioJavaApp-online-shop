package com.example.myapplication.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.HomeActivity;
import com.example.myapplication.Informations;
import com.example.myapplication.R;
import com.example.myapplication.db.Query;
import com.example.myapplication.db.ServerConnection;
import com.google.gson.Gson;

import java.io.IOException;

public class FragmentSignupTab extends Fragment {

    EditText email;
    EditText password;
    TextView confirmPassword;
    Button signup;
    Handler handler = new Handler(Looper.getMainLooper());

    float v = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_tab_signup, container, false);

        email = root.findViewById(R.id.text_email);
        password = root.findViewById(R.id.text_password);
        confirmPassword = root.findViewById(R.id.text_confirm_password);
        signup = root.findViewById(R.id.button_signup);

        email.setTranslationX(800);
        password.setTranslationX(800);
        confirmPassword.setTranslationX(800);
        signup.setTranslationX(800);

        email.setAlpha(v);
        password.setAlpha(v);
        confirmPassword.setAlpha(v);
        signup.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        confirmPassword.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        signup.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        signup.setOnClickListener(view -> {

            if(Informations.isIsLoggedIn()){
                Toast.makeText(getActivity(), R.string.already_logged_text, Toast.LENGTH_SHORT).show();
                return;
            }

            if(!password.getText().toString().equals(confirmPassword.getText().toString())){
                Toast.makeText(getActivity(), R.string.passwords_not_match, Toast.LENGTH_SHORT).show();
            }
            else if(password.getText().toString().equals("")){
                Toast.makeText(getActivity(), R.string.password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            }
            else{
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Gson gson = new Gson();
                        String query = gson.toJson(new Query("signup", email.getText().toString(), password.getText().toString()));
                        try {
                            ServerConnection.getInstance().sendMessage(query);
                            System.out.println(ServerConnection.getInstance().readMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Informations.setIsLoggedIn(true);
                        Informations.setCurrUser(email.getText().toString());

                        handler.post(()-> Toast.makeText(getContext(), getResources().getString(R.string.signup_success, Informations.getCurrUser()), Toast.LENGTH_SHORT).show());
                        Intent i = new Intent(getContext(), HomeActivity.class);
                        startActivity(i);
                    }
                }).start();
            }

        });


        return root;

    }

}
