package com.example.myapplication.entity;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Drink {

    private String ID, name, price;
    private String imageUrl;
    private ArrayList<String> ingredients;

    private int quantity = 1;

    public Drink(String ID, String name, String price, ArrayList<String> ingredients, String imageUrl) {
        this.ID = ID;
        this.name = name.substring(1, name.length() - 1);
        this.price = price;
        this.ingredients = ingredients;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setRandomPrice(String price) {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 16);
        this.price = String.valueOf(randomNum);
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setBitmap(String bitmap) {
        this.imageUrl = imageUrl;
    }


    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
