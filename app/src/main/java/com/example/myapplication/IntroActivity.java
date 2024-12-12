package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.myapplication.db.Neo4jConnection;
import com.example.myapplication.db.ServerConnection;

import org.json.JSONException;
import org.neo4j.driver.Result;

import java.io.IOException;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        VideoView videoView = (VideoView) findViewById(R.id.videoView);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.logofull_purpleb_fucsia);

        MediaController mediaController = new MediaController(this);

        mediaController.setAnchorView(videoView);

        videoView.setMediaController(mediaController);
        videoView.start();
        videoView.setMediaController(null);

        Thread t = new Thread(
                () -> {
                    try {
                        Context context = getApplicationContext();

                        //WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                        //String IP = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());

                        ServerConnection.getInstance().startConnection("10.0.2.2", 80);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
        );
        t.start();

        Thread t2 = new Thread(() -> {
            Neo4jConnection.getInstance().startConnection("bolt://10.0.2.2:11003", "neo4j", "password");
            // Query degli ingredienti
            Result res = Neo4jConnection.getInstance().runQuery("MATCH (n) WHERE (n:BEVERAGE_ALCOHOLIC OR n:BEVERAGE OR n:BEVERAGE_CAFFEINATED) RETURN DISTINCT n.naturalSource");
            while (res.hasNext()){
                Informations.allIngredients.add(res.next().get(0).toString());
            }
            res =  Neo4jConnection.getInstance().runQuery("MATCH (n) <-[:BELONGS_TO]-(y), (y) -[:CONTAINS]->(z) WHERE (n:COCKTAIL) OR (n:SMOOTHIE) RETURN DISTINCT z.namesIT");
            while (res.hasNext()){
                Informations.allIngredients.add(res.next().get(0).toString());
            }
        });
        t2.start();


        try {
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Handler handler = new Handler();
        handler.postDelayed(() -> {

            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
            //overridePendingTransition();

            finish();

        }, 2000);   //2 seconds




    }

    @NonNull
    private String trim(String input){
        input = input.substring(1, input.length()-1);
        return input;
    }
    @Override
    public void onBackPressed() {}
}