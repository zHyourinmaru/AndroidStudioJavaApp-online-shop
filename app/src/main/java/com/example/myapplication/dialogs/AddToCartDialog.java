package com.example.myapplication.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;
import com.example.myapplication.entity.Drink;
import com.example.myapplication.Informations;

public class AddToCartDialog extends DialogFragment {

    private Drink currDrink;

    public AddToCartDialog(Drink currDrink) {
        super();
        this.currDrink = currDrink;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.add_to_cart_text, currDrink.getName()))
                .setPositiveButton(R.string.add_to_cart_text, (dialogInterface, i) -> Informations.addDrinkToCart(currDrink))
                .setNegativeButton(R.string.abort_remove_cart_button, (dialogInterface, i) -> System.out.println("Cancel"));

        return builder.create();
    }
}
