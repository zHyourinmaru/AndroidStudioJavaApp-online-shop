package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.myapplication.db.DrinkDAO;
import com.example.myapplication.db.ServerConnection;
import com.example.myapplication.entity.Drink;
import com.example.myapplication.recviewAdapters.DrinksRecViewAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.neo4j.driver.Driver;

import java.io.IOException;
import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private ImageSlider imageSlider;
    protected Driver driver = null;
    private DrinkDAO drinkDAO = new DrinkDAO();
    private TextView preferenzeTextView;
    private RecyclerView recyclerViewSuggestion, recyclerViewBestBuy;

    @Override
    public void onResume() {
        super.onResume();
        if(Informations.isIsLoggedIn()){
            preferenzeTextView.setText(getResources().getString(R.string.check_this_out_text, Informations.getCurrUser()));
        }
        else{
            preferenzeTextView.setText(R.string.best_buy_text);
        }
        refreshPreferences();
        refreshBestBuy();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, container, false);

        imageSlider = (ImageSlider) root.findViewById(R.id.imageSlider);

        ArrayList<SlideModel> slideModelArrayList = new ArrayList<>();

        slideModelArrayList.add(new SlideModel(R.drawable.banner2, ScaleTypes.CENTER_CROP));
        slideModelArrayList.add(new SlideModel(R.drawable.banner1, ScaleTypes.CENTER_CROP));
        slideModelArrayList.add(new SlideModel(R.drawable.logo_purpleb_fucsia, ScaleTypes.CENTER_CROP));

        imageSlider.setImageList(slideModelArrayList, ScaleTypes.FIT);

        //Preferences
        preferenzeTextView = root.findViewById(R.id.text_suggestion);

        LinearLayoutManager layoutManagerSuggestion = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerSuggestion.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewSuggestion = (RecyclerView) root.findViewById(R.id.recview_suggestion);
        recyclerViewSuggestion.setLayoutManager(layoutManagerSuggestion);

        //BestBuy
        LinearLayoutManager layoutManagerBestBuy = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManagerBestBuy.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewBestBuy = (RecyclerView) root.findViewById(R.id.recview_bestbuy);
        recyclerViewBestBuy.setLayoutManager(layoutManagerBestBuy);

        return root;
    }

    public void refreshPreferences(){

        DrinksRecViewAdapter adapter = new DrinksRecViewAdapter(getContext());

        if(Informations.isIsLoggedIn()){
            ArrayList<String> preferredIngredients;
            preferredIngredients = getPreferences();
            ArrayList<Drink> drinks = drinkDAO.getAllDrinks(preferredIngredients);
            adapter.setDrinks(drinks);
        }
        else{
            ArrayList<Drink> drinks = drinkDAO.getRandomDrinks();
            adapter.setDrinks(drinks);
        }

        recyclerViewSuggestion.setAdapter(adapter);
    }

    private ArrayList<String> getPreferences(){

        ArrayList<String> preferences = new ArrayList<String>();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                Gson gson = new Gson();
                String query = gson.toJson(new com.example.myapplication.db.Query("getPreferences", Informations.getCurrUser()));

                try {
                    ServerConnection.getInstance().sendMessage(query);
                    String result = ServerConnection.getInstance().readMessage();
                    System.out.println(result);

                    //Preleva i primi 3 ingredienti preferiti dall'utente
                    JsonParser parser = new JsonParser();
                    JsonObject object = parser.parse(result).getAsJsonObject();
                    JsonArray jArray = object.getAsJsonArray("rows");

                    for(int i=0;i<3;i++){
                        preferences.add(untrim(jArray.get(i).getAsString()));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        t.start();
        try {
            t.join();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return preferences;
    }

    public void refreshBestBuy(){
        ArrayList<Drink> drinks = drinkDAO.getRandomDrinks();
        DrinksRecViewAdapter adapter = new DrinksRecViewAdapter(getContext());
        adapter.setDrinks(drinks);
        recyclerViewBestBuy.setAdapter(adapter);
    }

    public String untrim(String ref){
        String ret = "";
        ret = ret.concat("\"");
        ret = ret.concat(ref);
        ret = ret.concat("\"");
        return ret;
    }
}