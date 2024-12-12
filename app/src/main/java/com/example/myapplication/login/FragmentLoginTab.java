package com.example.myapplication.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

public class FragmentLoginTab extends Fragment {

    EditText email;
    EditText password;
    Button login;
    Handler handler = new Handler(Looper.getMainLooper());

    float v = 0;
    boolean flagback = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_tab_login, container, false);

        email = root.findViewById(R.id.text_email);
        password = root.findViewById(R.id.text_password);
        login = root.findViewById(R.id.button_login);

        email.setTranslationX(800);
        password.setTranslationX(800);
        login.setTranslationX(800);

        email.setAlpha(v);
        password.setAlpha(v);
        login.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        login.setOnClickListener(view -> {

            if(Informations.isIsLoggedIn()){
                Toast.makeText(getActivity(), R.string.already_logged_text, Toast.LENGTH_SHORT).show();
                return;
            }

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {

                    Gson gson = new Gson();
                    String query = gson.toJson(new Query("login", email.getText().toString(), password.getText().toString()));

                    try {
                        ServerConnection.getInstance().sendMessage(query);
                        String result = ServerConnection.getInstance().readMessage();
                        System.out.println(result);

                        //Check se esiste utente nel database MySql
                        JsonParser parser = new JsonParser();
                        JsonObject object = parser.parse(result).getAsJsonObject();
                        JsonArray jArray = object.getAsJsonArray("rows");

                        if(jArray.get(0).getAsInt() == 1){
                            //Logged in
                            Informations.setIsLoggedIn(true);
                            Informations.setCurrUser(email.getText().toString());

                            handler.post(() -> Toast.makeText(getContext(), R.string.login_success, Toast.LENGTH_SHORT).show());
                            flagback = true;
                        }
                        else{
                            //Utente non trovato
                            handler.post(() -> Toast.makeText(getContext(), R.string.auth_failed, Toast.LENGTH_SHORT).show());
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (flagback) {
                getActivity().onBackPressed();
            }

        });

        return root;

    }

}
