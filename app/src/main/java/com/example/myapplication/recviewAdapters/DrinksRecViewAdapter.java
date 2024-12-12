package com.example.myapplication.recviewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.entity.Drink;
import com.example.myapplication.R;
import com.example.myapplication.dialogs.AddToCartDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class DrinksRecViewAdapter extends RecyclerView.Adapter<DrinksRecViewAdapter.ViewHolder>{

    private final Context context;
    private ArrayList<Drink> drinks = new ArrayList<>();

    public DrinksRecViewAdapter(Context context){
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView txtName;
        private final ImageButton buttonCart;
        private final TextView txtPrice;
        private final ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            buttonCart = itemView.findViewById(R.id.button_cart);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            image = itemView.findViewById(R.id.image);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drink_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(drinks.get(position).getName());
        holder.txtPrice.setText(drinks.get(position).getPrice());
        holder.buttonCart.setOnClickListener(view -> {
            DialogFragment addToCartDialog = new AddToCartDialog(drinks.get(holder.getAdapterPosition()));
            addToCartDialog.show(((AppCompatActivity)context).getSupportFragmentManager(), "addToCart");
        });
        holder.image.setOnClickListener(view -> {

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                    (AppCompatActivity)context, R.style.BottomSheetDialogTheme
            );
            View bottomSheetView = LayoutInflater.from(context)
                    .inflate(R.layout.layout_bottom_sheet, ((AppCompatActivity) context).findViewById(R.id.bottom_sheet_container));

            bottomSheetView.findViewById(R.id.button_sheet_cart).setOnClickListener(view1 -> {
                DialogFragment addToCartDialog = new AddToCartDialog(drinks.get(holder.getAdapterPosition()));
                addToCartDialog.show(((AppCompatActivity)context).getSupportFragmentManager(), "addToCart");
            });

            ((TextView) bottomSheetView.findViewById(R.id.text_view_title)).setText(drinks.get(position).getName());
            String listString = String.join(", ", drinks.get(position).getIngredients());
            ((TextView) bottomSheetView.findViewById(R.id.text_view_ingredients)).setText(listString);
            ((TextView) bottomSheetView.findViewById(R.id.text_view_price)).setText(drinks.get(position).getPrice());
            ImageView image = ((ImageView) bottomSheetView.findViewById(R.id.image_view));
            Glide.with(context).load(drinks.get(position).getImageUrl()).into(image);

            bottomSheetDialog.setContentView(bottomSheetView);
            bottomSheetDialog.show();
        });

        Glide.with(context).load(drinks.get(position).getImageUrl()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return drinks.size();
    }

    public void setDrinks(ArrayList<Drink> drinks) {
        this.drinks = drinks;
        notifyDataSetChanged(); //serve per refresh dei dati
    }


}
