package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.searchActivities.DrinkSearchActivity;

public class SearchManagerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_search_manager, container, false);

        ConstraintLayout allDrinkButton = root.findViewById(R.id.layout_all_drinks);
        ConstraintLayout cocktailButton = root.findViewById(R.id.layout_cocktail);
        ConstraintLayout mocktailButton = root.findViewById(R.id.layout_mocktail);
        ConstraintLayout smoothiesButton = root.findViewById(R.id.layout_smoothies);
        ConstraintLayout altroButton = root.findViewById(R.id.layout_others);

        allDrinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(), DrinkSearchActivity.class);
                intent.putExtra("drinkType", "All");
                startActivity(intent);
            }
        });

        cocktailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(), DrinkSearchActivity.class);
                intent.putExtra("drinkType", "Cocktail");
                startActivity(intent);
            }
        });

        mocktailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(), DrinkSearchActivity.class);
                intent.putExtra("drinkType", "BevandeAlcoliche");
                startActivity(intent);
            }
        });

        smoothiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(), DrinkSearchActivity.class);
                intent.putExtra("drinkType", "Smoothies");
                startActivity(intent);
            }
        });

        altroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(root.getContext(), DrinkSearchActivity.class);
                intent.putExtra("drinkType", "AltreBevande");
                startActivity(intent);
            }
        });

        return root;
    }
}