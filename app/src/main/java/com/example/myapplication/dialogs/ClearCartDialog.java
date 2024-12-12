package com.example.myapplication.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.CartFragment;
import com.example.myapplication.Informations;
import com.example.myapplication.R;
import com.example.myapplication.entity.Drink;
import com.example.myapplication.recviewAdapters.CartRecViewAdapter;

public class ClearCartDialog extends DialogFragment {
    private final CartFragment cartFragment;
    private final CartRecViewAdapter adapter;

    public ClearCartDialog(CartFragment cartFragment, CartRecViewAdapter adapter) {
        super();
        this.cartFragment = cartFragment;
        this.adapter = adapter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.clear_cart_text)
                .setPositiveButton(R.string.clear_cart_positive_button, (dialogInterface, i) -> {
                    Informations.clearCart();
                    cartFragment.getRecyclerView().setAdapter(adapter);
                    cartFragment.getTotalText().setText(getResources().getString(R.string.cart_total_price, Informations.foldPrice(), "€" ));})
                .setNegativeButton(R.string.clear_cart_abort, (dialogInterface, i) -> System.out.println("Cancel"));

        return builder.create();
    }
}
