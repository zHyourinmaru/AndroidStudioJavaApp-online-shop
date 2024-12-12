package com.example.myapplication.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.icu.text.IDNA;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.Informations;
import com.example.myapplication.R;
import com.example.myapplication.db.Query;
import com.example.myapplication.db.ServerConnection;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

public class UserActivity extends AppCompatActivity {

    EditText nome, cognome, phone, address, cap, city, province, oldPassword, newPassword, newPasswordConfirm;
    Button clearAddressButton, updateAddressButton, deleteAccountButton, logOutButton, updatePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Back Button
        ImageButton image_button_back = this.findViewById(R.id.image_button_back);
        image_button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Change Password
        oldPassword = findViewById(R.id.text_old_password);
        newPassword = findViewById(R.id.text_new_password);
        newPasswordConfirm = findViewById(R.id.text_confirm_new_password);
        updatePasswordButton = findViewById(R.id.button_login);

        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newPassword.getText().toString().equals(newPasswordConfirm.getText().toString())){

                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            Handler handler = new Handler(Looper.getMainLooper());
                            Gson gson = new Gson();
                            String query = gson.toJson(new Query("login", Informations.getCurrUser(), oldPassword.getText().toString()));

                            try {
                                ServerConnection.getInstance().sendMessage(query);
                                String result = ServerConnection.getInstance().readMessage();
                                System.out.println(result);

                                //Check se password corrisponde nel database MySql
                                JsonParser parser = new JsonParser();
                                JsonObject object = parser.parse(result).getAsJsonObject();
                                JsonArray jArray = object.getAsJsonArray("rows");

                                if(jArray.get(0).getAsInt() == 1){

                                    Thread t1 = new Thread(new Runnable() {
                                        Handler handler = new Handler(Looper.getMainLooper());
                                        @Override
                                        public void run() {

                                            Gson gson = new Gson();
                                            String query = gson.toJson(new Query("updatePassword", Informations.getCurrUser(), newPassword.getText().toString()));

                                            try {
                                                ServerConnection.getInstance().sendMessage(query);
                                                System.out.println(ServerConnection.getInstance().readMessage());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            oldPassword.getText().clear();
                                            newPassword.getText().clear();
                                            newPasswordConfirm.getText().clear();
                                            handler.post(() -> Toast.makeText(getApplicationContext(), R.string.password_change_success, Toast.LENGTH_SHORT).show());
                                        }
                                    });
                                    t1.start();
                                    try {
                                        t1.join();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                }
                                else{
                                    handler.post(() -> Toast.makeText(getApplicationContext(), R.string.old_password_not_correct, Toast.LENGTH_SHORT).show());
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
                }
                else{
                    Toast.makeText(getApplicationContext(), R.string.passwords_not_match, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Shipping Address
        updateAddressButton = findViewById(R.id.button_shipping_update);
        clearAddressButton = findViewById(R.id.button_shipping_clear);

        nome = findViewById(R.id.text_name);
        cognome = findViewById(R.id.text_surname);
        phone = findViewById(R.id.text_phone_number);
        address = findViewById(R.id.text_shipping_address);
        cap = findViewById(R.id.text_cap);
        city = findViewById(R.id.text_city);
        province = findViewById(R.id.text_province);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                Gson gson = new Gson();
                String query = gson.toJson(new Query("getUserInfo", Informations.getCurrUser()));

                try {
                    ServerConnection.getInstance().sendMessage(query);
                    String result = ServerConnection.getInstance().readMessage();
                    System.out.println(result);

                    //Check se esiste utente nel database MySql
                    JsonParser parser = new JsonParser();
                    JsonObject object = parser.parse(result).getAsJsonObject();
                    JsonArray jArray = object.getAsJsonArray("rows");

                    nome.setText(jArray.get(0).getAsString());
                    cognome.setText(jArray.get(1).getAsString());
                    phone.setText(jArray.get(2).getAsString());
                    address.setText(jArray.get(3).getAsString());
                    cap.setText(jArray.get(4).getAsString());
                    city.setText(jArray.get(5).getAsString());
                    province.setText(jArray.get(6).getAsString());

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

        updateAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Gson gson = new Gson();
                        String query = gson.toJson(new Query("updateUserInfo", Informations.getCurrUser(), nome.getText().toString(), cognome.getText().toString(), phone.getText().toString(),
                                                            address.getText().toString(), cap.getText().toString(), city.getText().toString(), province.getText().toString()));

                        try {
                            ServerConnection.getInstance().sendMessage(query);
                            System.out.println(ServerConnection.getInstance().readMessage());
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
                Toast.makeText(getApplicationContext(), getString(R.string.updated_informations) + Informations.getCurrUser(), Toast.LENGTH_SHORT).show();
            }
        });

        clearAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nome.getText().clear();
                cognome.getText().clear();
                phone.getText().clear();
                address.getText().clear();
                cap.getText().clear();
                city.getText().clear();
                province.getText().clear();
            }
        });

        //Log Out
        logOutButton = findViewById(R.id.button_logout);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
                Toast.makeText(getApplicationContext(), R.string.logout_text, Toast.LENGTH_SHORT).show();
            }
        });

        //Delete Account
        deleteAccountButton = findViewById(R.id.button_delete_account);

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Gson gson = new Gson();
                        String query = gson.toJson(new Query("deleteUser", Informations.getCurrUser()));

                        try {
                            ServerConnection.getInstance().sendMessage(query);
                            System.out.println(ServerConnection.getInstance().readMessage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                t.start();
                try {
                    t.join();
                    logOut();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.delete_user_text, Informations.getCurrUser()), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void logOut(){
        Informations.setIsLoggedIn(false);
        onBackPressed();
    }

}