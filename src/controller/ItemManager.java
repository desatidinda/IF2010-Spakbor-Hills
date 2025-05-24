package controller;

import entity.Item.*;
import main.GameClock;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class ItemManager {
    private static final Map<String, Food> FOOD_ITEMS = new HashMap<>();
    private static final Map<String, Seeds> SEED_ITEMS = new HashMap<>();
    private static final Map<String, Crops> CROP_ITEMS = new HashMap<>();

    static{
        Food fishNChips = new Food("Fish n' Chips", 50, 150.0, 135.0);
        FOOD_ITEMS.put(fishNChips.getItemName(), fishNChips);
        Food baguette = new Food("Baguette", 25, 100.0, 80.0);
        FOOD_ITEMS.put(baguette.getItemName(), baguette);
        Food sashimi = new Food("Sashimi", 70, 300.0, 275.0);
        FOOD_ITEMS.put(sashimi.getItemName(), sashimi);
        Food fugu = new Food("Fugu", 50, null, 135.0);
        FOOD_ITEMS.put(fugu.getItemName(), fugu);
        Food wine = new Food("Wine", 20, 100.0, 90.0);
        FOOD_ITEMS.put(wine.getItemName(), wine);
        Food pumpkinPie = new Food("Pumpkin Pie", 35, 120.0, 100.0);
        FOOD_ITEMS.put(pumpkinPie.getItemName(), pumpkinPie);
        Food veggieSoup = new Food("Veggie Soup", 40, 140.0, 120.0);
        FOOD_ITEMS.put(veggieSoup.getItemName(), veggieSoup);
        Food fishStew = new Food("Fish Stew", 70, 280.0, 260.0);
        FOOD_ITEMS.put(fishStew.getItemName(), fishStew);
        Food spakborSalad = new Food("Spakbor Salad", 70, null, 250.0);
        FOOD_ITEMS.put(spakborSalad.getItemName(), spakborSalad);
        Food fishSandwich = new Food("Fish Sandwich", 50, 200.0, 180.0);
        FOOD_ITEMS.put(fishSandwich.getItemName(), fishSandwich);
        Food theLegendsOfSpakbor = new Food("The Legends of Spakbor", 100, null, 2000.0);
        FOOD_ITEMS.put(theLegendsOfSpakbor.getItemName(), theLegendsOfSpakbor);
        Food cookedPigsHead = new Food("Cooked Pig's Head", 100, 1000.0, 0.0);
        FOOD_ITEMS.put(cookedPigsHead.getItemName(), cookedPigsHead);
        
        Seeds parsnipSeeds = new Seeds("Parsnip Seeds", EnumSet.of(GameClock.Season.SPRING), 1, 20.0);
        SEED_ITEMS.put(parsnipSeeds.getItemName(), parsnipSeeds);
        Seeds cauliflowerSeeds = new Seeds("Cauliflower Seeds", EnumSet.of(GameClock.Season.SPRING), 5, 80.0);
        SEED_ITEMS.put(cauliflowerSeeds.getItemName(), cauliflowerSeeds);
        Seeds potatoSeeds = new Seeds("Potato Seeds", EnumSet.of(GameClock.Season.SPRING), 3, 50.0);
        SEED_ITEMS.put(potatoSeeds.getItemName(), potatoSeeds);
        Seeds wheatSeeds = new Seeds("Wheat Seeds", EnumSet.of(GameClock.Season.SUMMER, GameClock.Season.FALL), 1, 60.0);
        SEED_ITEMS.put(wheatSeeds.getItemName(), wheatSeeds);
        Seeds blueberrySeeds = new Seeds("Blueberry Seeds", EnumSet.of(GameClock.Season.SUMMER), 7, 80.0);
        SEED_ITEMS.put(blueberrySeeds.getItemName(), blueberrySeeds);
        Seeds tomatoSeeds = new Seeds("Tomato Seeds", EnumSet.of(GameClock.Season.SUMMER), 3, 50.0);
        SEED_ITEMS.put(tomatoSeeds.getItemName(), tomatoSeeds);
        Seeds hotPepperSeeds = new Seeds("Hot Pepper Seeds", EnumSet.of(GameClock.Season.SUMMER), 1, 40.0);
        SEED_ITEMS.put(hotPepperSeeds.getItemName(), hotPepperSeeds);
        Seeds cranberrySeeds = new Seeds("Cranberry Seeds", EnumSet.of(GameClock.Season.FALL), 2, 100.0);
        SEED_ITEMS.put(cranberrySeeds.getItemName(), cranberrySeeds);
        Seeds pumpkinSeeds = new Seeds("Pumpkin Seeds", EnumSet.of(GameClock.Season.FALL), 7, 150.0);
        SEED_ITEMS.put(pumpkinSeeds.getItemName(), pumpkinSeeds);
        Seeds grapeSeeds = new Seeds("Grape Seeds", EnumSet.of(GameClock.Season.FALL), 3, 60.0);
        SEED_ITEMS.put(grapeSeeds.getItemName(), grapeSeeds);
        Seeds melonSeeds = new Seeds("Melon Seeds", EnumSet.of(GameClock.Season.SUMMER), 4, 80.0);
        SEED_ITEMS.put(melonSeeds.getItemName(), melonSeeds);

        Crops parsnip = new Crops("Parsnip", 50.0, 35.0, 1, 3);
        CROP_ITEMS.put(parsnip.getItemName(), parsnip);
        Crops cauliflower = new Crops("Cauliflower", 200.0, 150.0, 1, 3);
        CROP_ITEMS.put(cauliflower.getItemName(), cauliflower);
        Crops potato = new Crops("Potato", null, 80.0, 1, 3);
        CROP_ITEMS.put(potato.getItemName(), potato);
        Crops wheat = new Crops("Wheat", 50.0, 30.0, 3, 3);
        CROP_ITEMS.put(wheat.getItemName(), wheat);
        Crops blueberry = new Crops("Blueberry", 150.0, 40.0, 3, 3);
        CROP_ITEMS.put(blueberry.getItemName(), blueberry);
        Crops tomato = new Crops("Tomato", 90.0, 60.0, 1, 3);
        CROP_ITEMS.put(tomato.getItemName(), tomato);
        Crops hotPepper = new Crops("Hot Pepper", null, 40.0, 1, 3);
        CROP_ITEMS.put(hotPepper.getItemName(), hotPepper);
        Crops melon = new Crops("Melon", null, 250.0, 1, 3);
        CROP_ITEMS.put(melon.getItemName(), melon);
        Crops cranberry = new Crops("Cranberry", null, 25.0, 10, 3);
        CROP_ITEMS.put(cranberry.getItemName(), cranberry);
        Crops pumpkin = new Crops("Pumpkin", 300.0, 250.0, 1, 3);
        CROP_ITEMS.put(pumpkin.getItemName(), pumpkin);
        Crops grape = new Crops("Grape", 100.0, 10.0, 20, 3);
        CROP_ITEMS.put(grape.getItemName(), grape);
    }

    public static Food getFoodItem(String itemName){
        return FOOD_ITEMS.get(itemName);
    }

    public static Seeds getSeedItem(String itemName){
        return SEED_ITEMS.get(itemName);
    }

    public static Crops getCropItem(String itemName){
        return CROP_ITEMS.get(itemName);
    }
}