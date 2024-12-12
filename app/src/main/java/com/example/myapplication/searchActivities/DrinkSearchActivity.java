package com.example.myapplication.searchActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Informations;
import com.example.myapplication.R;
import com.example.myapplication.db.DrinkDAO;
import com.example.myapplication.dialogs.FilterDialog;
import com.example.myapplication.entity.Drink;
import com.example.myapplication.recviewAdapters.DrinksRecViewAdapter;

import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

public class DrinkSearchActivity extends AppCompatActivity {

    protected RecyclerView drinksRecView = null;

    ArrayList<Drink> drinks = new ArrayList<>();
    ArrayList<Drink> drinkShowed = new ArrayList<>();

    DrinkDAO drinkDAO = new DrinkDAO();

    DrinksRecViewAdapter adapter = new DrinksRecViewAdapter(this);

    TextView textViewTitle;

    public ArrayList<String> activeFilters = new ArrayList<>();
    public boolean[] checkedItems = new boolean[Informations.allIngredients.size()];
    String drinkType = "All";

    String searchText = "";

    protected Intent myIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinks);

        SearchView searchView = (SearchView) findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText.toLowerCase(Locale.ROOT);
                showDrinks();
                return true;
            }
        });

        drinksRecView = findViewById(R.id.drinksRecView);

        textViewTitle = findViewById(R.id.text_view_title);

        myIntent = getIntent();
        String type = myIntent.getStringExtra("drinkType");
        setDrinkType(type);

        drinksRecView.setLayoutManager(new GridLayoutManager(this, 2 ));
        showDrinks();

        ImageButton image_button_back = this.findViewById(R.id.image_button_back);

        image_button_back.setOnClickListener(view -> onBackPressed());

        ImageButton filterButton = findViewById(R.id.button_filters);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FilterDialog filterDialog = new FilterDialog(DrinkSearchActivity.this, activeFilters, checkedItems);
                filterDialog.show(getSupportFragmentManager(), "filterDialog");
            }
        });
    }


    public void showDrinks() {
        if (activeFilters.size() > 0) {
            drinkShowed = filtraDrinks(drinks);
        } else {
            drinkShowed = new ArrayList<>(drinks);
        }
        if (!searchText.isEmpty())
        {
            drinkShowed = (ArrayList<Drink>) drinkShowed.stream().filter(drink -> drink.getName().toLowerCase(Locale.ROOT).contains(searchText.toLowerCase(Locale.ROOT))).collect(Collectors.toList());
        }
        adapter.setDrinks(drinkShowed);
        drinksRecView.setAdapter(adapter);
    }


    private ArrayList<Drink> filtraDrinks(ArrayList<Drink> drinks) {
        ArrayList<Drink> filteredDrinks = (ArrayList<Drink>) this.drinks.stream().filter(drink -> {
            ArrayList<String> commonItems = new ArrayList<String>(activeFilters);
            commonItems.retainAll(drink.getIngredients());
            return commonItems.size() > 0;
        }).collect(Collectors.toList());
        return filteredDrinks;
    }

    public void setDrinkType(String drinkType) {
        this.drinkType = drinkType;
        switch (this.drinkType) {
            case "BevandeAlcoliche":
                drinks.clear();
                drinks.addAll(drinkDAO.getBevandeAlcoliche());
                textViewTitle.setText(R.string.liquor);
                break;
            case "AltreBevande":
                drinks.clear();
                drinks.addAll(drinkDAO.getAltreBevande());
                textViewTitle.setText(R.string.others);
                break;
            case "Cocktail":
                drinks.clear();
                drinks.addAll(drinkDAO.getCocktail());
                textViewTitle.setText(R.string.cocktail);
                break;
            case "Smoothies":
                drinks.clear();
                drinks.addAll(drinkDAO.getSmoothies());
                textViewTitle.setText(R.string.smoothies);
                break;
            case "All": {
                drinks.clear();
                drinks.addAll(drinkDAO.getAllDrinks());
                textViewTitle.setText(R.string.all_drinks);
            }
        }
    }
    public String untrim(String ref){
        String ret = "";
        ret = ret.concat("\"");
        ret = ret.concat(ref);
        ret = ret.concat("\"");
        return ret;
    }
}

