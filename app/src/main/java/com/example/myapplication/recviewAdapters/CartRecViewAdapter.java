package com.example.myapplication.recviewAdapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.CartFragment;
import com.example.myapplication.entity.Drink;
import com.example.myapplication.R;
import com.example.myapplication.dialogs.RemoveFromCartDialog;

import java.util.ArrayList;

public class CartRecViewAdapter extends RecyclerView.Adapter<CartRecViewAdapter.ViewHolder>{

    private CartFragment cart;
    private Context context;
    private ArrayList<Drink> drinks = new ArrayList<>();
    public CartRecViewAdapter(Context context, CartFragment cart){
        this.context = context;
        this.cart = cart;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView txtName;
        private final TextView txtPrice;
        private final ImageView image;
        private final EditText quantity;
        private final ImageButton buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            image = itemView.findViewById(R.id.image);
            buttonDelete = itemView.findViewById(R.id.button_delete);
            quantity = itemView.findViewById(R.id.spinner_cart);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(drinks.get(position).getName());
        holder.txtPrice.setText(drinks.get(position).getPrice());
        holder.quantity.setText(String.valueOf(drinks.get(position).getQuantity()));
        holder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = holder.quantity.getText().toString();
                if (!text.isEmpty())
                {
                    int quantity = Integer.parseInt(text);
                    drinks.get(position).setQuantity(quantity);
                    System.out.println(drinks.get(position).getQuantity());
                }
            }
        });
        holder.buttonDelete.setOnClickListener(view -> {
            DialogFragment removeFromCartDialog = new RemoveFromCartDialog(drinks.get(holder.getAdapterPosition()), cart, CartRecViewAdapter.this);
            removeFromCartDialog.show(((AppCompatActivity)context).getSupportFragmentManager(), "addToCart");
            updateDrinks(drinks);
        });

        Glide.with(context).load(drinks.get(position).getImageUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return drinks.size();
    }

    public void updateDrinks(ArrayList<Drink> drinks) {
        this.drinks = drinks;
        notifyDataSetChanged(); //serve per refresh dei dati
    }

    public void clearDrinks(){
        this.drinks.clear();
        notifyDataSetChanged();
    }


}
