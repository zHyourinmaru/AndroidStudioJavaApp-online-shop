package com.example.myapplication;

import com.example.myapplication.entity.Drink;

import java.util.ArrayList;
import java.util.Optional;

public class Informations {

    private static boolean isLoggedIn = false;
    private static String currUser;
    private static ArrayList<Drink> cartDrinkList = new ArrayList<Drink>();
    private static int currentBalance = 9999;

    public static ArrayList<String> allIngredients = new ArrayList<String>();

    public static boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    public static void setIsLoggedIn(boolean isLoggedIn) {
        Informations.isLoggedIn = isLoggedIn;
    }

    public static String getCurrUser() {
        return currUser;
    }

    public static void setCurrUser(String currUser) {
        Informations.currUser = currUser;
    }

    public static void clearCart(){
        cartDrinkList.clear();
    }

    public static void addDrinkToCart(Drink drink){
        Optional<Drink> match = cartDrinkList.stream().filter(d -> d.getName().equals(drink.getName())).findFirst();
        if (match.isPresent()){
            match.get().setQuantity(match.get().getQuantity() + 1);
        } else {
            cartDrinkList.add(drink);
        }

    }

    public static void removeDrinkFromCart(Drink drink){
        cartDrinkList.remove(drink);
    }

    public static void setCartDrinkList(ArrayList<Drink> cartDrinkList) {
        Informations.cartDrinkList = cartDrinkList;
    }

    public static ArrayList<Drink> getCartDrinkList() {
        return cartDrinkList;
    }

    public static int foldPrice(){

        int totalPrice = 0;
        String currStringPrice;

        for(Drink var: cartDrinkList){
            currStringPrice = var.getPrice();
            totalPrice += Integer.parseInt(currStringPrice.substring(0,currStringPrice.length()-1));
        }

        return totalPrice;
    }

    public static int getCurrentBalance() {
        return currentBalance;
    }

    public static void setCurrentBalance(int currentBalance) {
        Informations.currentBalance = currentBalance;
    }
}
