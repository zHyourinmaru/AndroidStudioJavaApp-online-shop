package com.example.myapplication.dialogs;

import static java.lang.Thread.sleep;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.CartFragment;
import com.example.myapplication.R;
import com.example.myapplication.db.Query;
import com.example.myapplication.db.ServerConnection;
import com.example.myapplication.entity.Drink;
import com.example.myapplication.Informations;
import com.example.myapplication.login.LoginActivity;
import com.example.myapplication.recviewAdapters.CartRecViewAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;

public class PurchaseDialog extends DialogFragment {

    private CartFragment cartFragment;
    private CartRecViewAdapter adapter;

    public PurchaseDialog(CartFragment cartFragment, CartRecViewAdapter adapter) {
        this.cartFragment = cartFragment;
        this.adapter = adapter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Acquistare le bevande nel carrello?")
                .setPositiveButton("Conferma acquisto", (dialogInterface, i) -> {
                                                              if(Informations.getCartDrinkList().isEmpty()){
                                                                  Toast.makeText(getContext(), "Il carrello è vuoto!", Toast.LENGTH_SHORT).show();
                                                              }
                                                              else if(!Informations.isIsLoggedIn()){
                                                                  Toast.makeText(getContext(), "Non sei loggato", Toast.LENGTH_SHORT).show();
                                                                  Intent intent = new Intent(getContext(), LoginActivity.class);
                                                                  startActivity(intent);
                                                              }
                                                              else{
                                                                  updatePreferences();
                                                                  cartFragment.updateCredit();
                                                                  Informations.clearCart();
                                                                  cartFragment.getRecyclerView().setAdapter(adapter);
                                                                  cartFragment.getTotalText().setText("Totale: 0€");
                                                              }
                                                            }
                )
                .setNegativeButton("Annulla", (dialogInterface, i) -> System.out.println("Cancel"));

        return builder.create();
    }

    public void updatePreferences(){


        Toast.makeText(getContext(), R.string.purchase_confirmed, Toast.LENGTH_SHORT).show();
        ArrayList<Drink> drinkAcquistati = Informations.getCartDrinkList();

        drinkAcquistati.forEach((drink) -> {

            for (int i=0; i < drink.getQuantity(); i++)
            {
                drink.getIngredients().forEach((ingredient) -> {

                    Thread t = new Thread(() -> {

                        Gson gson = new Gson();
                        String query = gson.toJson(new Query("updatePreferences", ingredient.toString(), Informations.getCurrUser()));

                        try {
                            ServerConnection.getInstance().sendMessage(query);
                            String result = ServerConnection.getInstance().readMessage();
                            System.out.println(result);

                        } catch (IOException e) {
                            e.printStackTrace();
                            System.out.println(e.toString());
                        }
                    });

                    t.start();
                    try {
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }


        });
    }

    @NonNull
    private String trim(String input){
        input = input.substring(1, input.length()-1);
        return input;
    }
}
