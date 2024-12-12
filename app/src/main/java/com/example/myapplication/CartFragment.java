package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.dialogs.ClearCartDialog;
import com.example.myapplication.dialogs.PurchaseDialog;
import com.example.myapplication.entity.Drink;
import com.example.myapplication.recviewAdapters.CartRecViewAdapter;

import java.util.ArrayList;


public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView totalText, creditText;
    private Context context;
    private CartRecViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_cart, container, false);

        Button purchaseButton = root.findViewById(R.id.purchaseButton);
        Button clearButton = root.findViewById(R.id.button_clear);

        recyclerView = root.findViewById(R.id.cartRecyclerView);
        totalText = root.findViewById(R.id.totalText);
        creditText = root.findViewById(R.id.creditText);

        creditText.setText(getResources().getString(R.string.current_balance, Informations.getCurrentBalance(), "€"));
        ArrayList<Drink> cartDrinkList = Informations.getCartDrinkList();

        adapter = new CartRecViewAdapter(getContext(), this);
        adapter.updateDrinks(cartDrinkList);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1 ));

        purchaseButton.setOnClickListener(view -> {
            DialogFragment confirmPurchase = new PurchaseDialog(CartFragment.this, adapter);
            confirmPurchase.show(((AppCompatActivity)getContext()).getSupportFragmentManager(), "confirmPurchase");
        });

        clearButton.setOnClickListener((view) -> {
            DialogFragment confirmClear = new ClearCartDialog(CartFragment.this, adapter);
            confirmClear.show(((AppCompatActivity)getContext()).getSupportFragmentManager(), "confirmClear");
        });

        totalText.setText(getResources().getString(R.string.cart_total_price, Informations.foldPrice(), "€"));

        return root;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public TextView getTotalText() {
        return totalText;
    }

    public TextView getCreditText() {
        return creditText;
    }

    public void updateCredit(){

        //(Integer.valueOf(creditText.getText().toString().substring(17,creditText.getText().length()-1)));
        Informations.setCurrentBalance(Informations.getCurrentBalance() - Informations.foldPrice());
        creditText.setText(getResources().getString(R.string.current_balance, Informations.getCurrentBalance(), "€"));

    }

}