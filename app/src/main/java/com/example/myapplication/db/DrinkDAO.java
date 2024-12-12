package com.example.myapplication.db;

import androidx.annotation.NonNull;

import com.example.myapplication.Informations;
import com.example.myapplication.entity.Drink;

import org.neo4j.driver.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class DrinkDAO {

    protected HashMap<String, String> Queries = new HashMap<>();
    {
        Queries.put(
                "BevandeAlcoliche",
                "MATCH (n:BEVERAGE_ALCOHOLIC) RETURN DISTINCT n.flavorDBID ,n.aliases, n.naturalSource, n.imageUrl, n.price ORDER BY n.flavorDBID"
        );

        Queries.put(
                "AltreBevande",
                "MATCH (n) WHERE n:BEVERAGE_CAFFEINATED OR n:BEVERAGE RETURN DISTINCT " +
                        "n.flavorDBID, n.aliases, n.naturalSource, n.imageUrl, n.price ORDER BY n.flavorDBID"
        );

        Queries.put(
                "Cocktail",
                "MATCH (n:COCKTAIL) <-[:BELONGS_TO]-(y) RETURN y.bevID, y.aliases, y.imageUrl, y.price ORDER BY y.bevID"
        );

        Queries.put(
                "Smoothies",
                "MATCH (n:SMOOTHIE) <-[:BELONGS_TO]-(y) RETURN y.smoothieID, y.aliases, y.imageUrl, y.price ORDER BY y.smoothieID"
        );

        Queries.put(
                "AllSource",
                "MATCH (n) WHERE n:BEVERAGE OR n:BEVERAGE_ALCOHOLIC OR n:BEVERAGE_CAFFEINATED RETURN DISTINCT n.flavorDBID ,n.aliases, n.naturalSource, n.imageUrl, n.price ORDER BY n.flavorDBID"
        );

        Queries.put(
                "AllContaining",
                "MATCH (n) <-[:BELONGS_TO]-(y) WHERE (n:COCKTAIL) OR (n:SMOOTHIE) RETURN id(y), y.aliases, y.imageUrl, y.price ORDER BY id(y)"
        );

        Queries.put(
                "RandomSource",
                "MATCH (n) WHERE (n:BEVERAGE OR n:BEVERAGE_ALCOHOLIC OR n:BEVERAGE_CAFFEINATED) " +
                        "AND (n.flavorDBID>(toInteger(rand() * (26)) + 7)) AND (n.flavorDBID<toInteger(rand() * (912)) + 33) " +
                        "RETURN DISTINCT n.flavorDBID ,n.aliases, n.naturalSource, n.imageUrl, n.price, rand() AS r ORDER BY r"
        );

        Queries.put(
                "RandomContaining",
                "MATCH (n) <-[:BELONGS_TO]-(y) WHERE (n:COCKTAIL) OR (n:SMOOTHIE) " +
                        "AND (n.flavorDBID > (toInteger(rand() * (12669)) + 18502)) " +
                        "AND (n.flavorDBID < (toInteger(rand() * (19)) + 31173)) " +
                        "RETURN id(y), y.aliases, y.imageUrl, y.price, rand() AS r ORDER BY r"
        );
    }


    public ArrayList<Drink> getBevandeAlcoliche() {
        return getDrinksSource(Queries.get("BevandeAlcoliche"));
    }

    public ArrayList<Drink> getAltreBevande() {
        return getDrinksSource(Queries.get("AltreBevande"));
    }

    public ArrayList<Drink> getCocktail() {
        return getDrinksContaining(Queries.get("Cocktail"));
    }

    public ArrayList<Drink> getSmoothies() {
        return getDrinksContaining(Queries.get("Smoothies"));
    }

    public ArrayList<Drink> getAllSource() {
        return getDrinksSource(Queries.get("AllSource"));
    }

    public ArrayList<Drink> getAllContaining() {
        return getDrinksContaining(Queries.get("AllContaining"));
    }

    public ArrayList<Drink> getAllDrinks() {
        ArrayList<Drink> allDrinks = new ArrayList<>();
        allDrinks.addAll(this.getAllContaining());
        allDrinks.addAll(this.getAllSource());
        return allDrinks;
    }

    public ArrayList<Drink> getRandomDrinks() {
        ArrayList<Drink> allDrinks = new ArrayList<>();
        ArrayList<Drink> sourceRandomDrinks = getDrinksSource(Queries.get("RandomSource"));
        ArrayList<Drink> containingRandomDrinks = getDrinksContaining(Queries.get("RandomContaining"));
        allDrinks.addAll(sourceRandomDrinks);
        allDrinks.addAll(containingRandomDrinks);
        return allDrinks;
    }

    public ArrayList<Drink> getAllDrinks(ArrayList<String> filters){
        ArrayList<Drink> allDrinks = new ArrayList<>();

        ArrayList<Drink> filteredContaining = (ArrayList<Drink>) this.getAllContaining().stream().filter(drink -> {
            ArrayList<String> commonItems = new ArrayList<String>(filters);
            commonItems.retainAll(drink.getIngredients());
            return commonItems.size() > 0;
        }).collect(Collectors.toList());

        ArrayList<Drink> filteredSource = (ArrayList<Drink>) this.getAllSource().stream().filter(drink -> {
            ArrayList<String> commonItems = new ArrayList<String>(filters);
            commonItems.retainAll(drink.getIngredients());
            return commonItems.size() > 0;
        }).collect(Collectors.toList());

        allDrinks.addAll(filteredContaining);
        allDrinks.addAll(filteredSource);
        return allDrinks;
    }

    @NonNull
    private ArrayList<Drink> getDrinksContaining(String query) {
        ArrayList<Drink> list = new ArrayList<>();
        //Neo4j
        Result result = Neo4jConnection.getInstance().runQuery(query);
        while (result.hasNext()) {

            String ID = result.peek().get(0).toString();

            String ingredientsQuery = "MATCH (n:DRINK) -[:CONTAINS]->(y) WHERE n.bevID=" + ID + " RETURN DISTINCT y.namesIT";
            Result ingredientsResult = Neo4jConnection.getInstance().runQuery(ingredientsQuery);
            ArrayList<String> ingredientsArrayList = new ArrayList<String>();

            while(ingredientsResult.hasNext()){
                ingredientsArrayList.add(ingredientsResult.next().get(0).toString());
            }

            list.add(new Drink(ID, result.peek().get(1).toString(),
                    trim(result.peek().get(3).toString()), ingredientsArrayList, trim(result.peek().get(2).toString())));
            result.next();
        }
        return list;
    }

    @NonNull
    private ArrayList<Drink> getDrinksSource(String query) {
        ArrayList<Drink> list = new ArrayList<>();
        //Neo4j
        Result result = Neo4jConnection.getInstance().runQuery(query);
        while(result.hasNext()){
            ArrayList<String> tempIngredients = new ArrayList<>();
            tempIngredients.add(result.peek().get(2).toString());

            list.add(new Drink(result.peek().get(0).toString(), result.peek().get(1).toString(),
                    trim(result.peek().get(4).toString()), tempIngredients, trim(result.peek().get(3).toString())));
            result.next();
        }
        return list;
    }

    @NonNull
    private String trim(String input){
        input = input.substring(1, input.length()-1);
        return input;
    }
}
