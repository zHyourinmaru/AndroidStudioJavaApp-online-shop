package com.example.myapplication.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.example.myapplication.CartFragment;
import com.example.myapplication.R;
import com.example.myapplication.entity.Drink;
import com.example.myapplication.Informations;
import com.example.myapplication.recviewAdapters.CartRecViewAdapter;

public class RemoveFromCartDialog extends DialogFragment {

    private final Drink currDrink;
    private final CartFragment cartFragment;
    private final CartRecViewAdapter adapter;

    public RemoveFromCartDialog(Drink currDrink, CartFragment cartFragment, CartRecViewAdapter adapter) {
        super();
        this.currDrink = currDrink;
        this.cartFragment = cartFragment;
        this.adapter = adapter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getResources().getString(R.string.cart_remove_dialog_text, currDrink.getName()))
                .setPositiveButton(R.string.remove_from_cart_button, (dialogInterface, i) -> {Informations.removeDrinkFromCart(currDrink);
                                                                            cartFragment.getRecyclerView().setAdapter(adapter);
                                                                            cartFragment.getTotalText().setText(getString(R.string.cart_total_price, Informations.foldPrice(), "€"));})
                .setNegativeButton(R.string.abort_remove_cart_button, (dialogInterface, i) -> System.out.println("Cancel"));

        return builder.create();
    }
}
