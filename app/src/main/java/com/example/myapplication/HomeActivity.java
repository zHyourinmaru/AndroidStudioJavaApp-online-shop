package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myapplication.db.Query;
import com.example.myapplication.db.ServerConnection;
import com.example.myapplication.login.LoginActivity;
import com.example.myapplication.login.UserActivity;
import com.google.gson.Gson;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {

    private boolean isBackPressedOnce = false;
    ChipNavigationBar nav_menu;

    private Fragment fragmentDashboard, fragmentSearch, fragmentCart = null;;
    boolean flagAccount = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
			.build();
        ImageLoader.getInstance().init(config);

        // Gestione della navigation top bar
        nav_menu = findViewById(R.id.nav_menu);
        nav_menu.setItemSelected(R.id.nav_dashboard, true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new DashboardFragment()).commit();
        navMenu();

        // ---------------------------

    }

    private void navMenu() {


        nav_menu.setOnItemSelectedListener(i -> {
            switch (i) {
                case R.id.nav_dashboard:
                    if (fragmentDashboard == null) {
                        fragmentDashboard = new DashboardFragment();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentDashboard).commit();
                    break;
                case R.id.nav_search:

                    if (fragmentSearch == null) {
                        fragmentSearch = new SearchManagerFragment();
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentSearch).commit();
                    break;
                case R.id.nav_cart:

                    if (fragmentCart == null) {
                        fragmentCart = new CartFragment();
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentCart).commit();
                    break;
                case R.id.nav_account:

                    flagAccount = true;
                    Intent intent;
                    if (Informations.isIsLoggedIn()) {
                        intent = new Intent(getApplicationContext(), UserActivity.class);
                        startActivity(intent);
                    } else {
                        intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }


            }

        });

    }

    @Override
    public void onBackPressed() {

        if (isBackPressedOnce) {
            super.onBackPressed();
            finishAndRemoveTask();
        }

        Toast.makeText(this, R.string.exit_text, Toast.LENGTH_SHORT).show();
        isBackPressedOnce = true;

        new Handler().postDelayed(() -> isBackPressedOnce = false, 2000);


    }

    @Override
    protected void onResume() {

        super.onResume();

        if (flagAccount) {

            if (fragmentDashboard == null) {
                fragmentDashboard = new DashboardFragment();
            }

            flagAccount = false;
            nav_menu.setItemSelected(R.id.nav_dashboard, true);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentDashboard).commit();
        }

    }

    @Override
    public void finishAndRemoveTask() {
        super.finishAndRemoveTask();
        System.out.println("App destroyed");
        new Thread(() -> {
            try {
                Gson gson = new Gson();
                String query = gson.toJson(new Query("exit"));
                ServerConnection.getInstance().sendMessage(query);
                ServerConnection.getInstance().stopConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }
}