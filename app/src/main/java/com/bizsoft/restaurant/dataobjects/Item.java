package com.bizsoft.restaurant.dataobjects;


import android.os.AsyncTask;

import com.bizsoft.restaurant.service.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by GopiKing on 08-11-2017.
 */

public class Item {

    Long id;
    String name;

    String uom;

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public Long getKotChannelId() {
        return kotChannelId;
    }

    public void setKotChannelId(Long kotChannelId) {
        this.kotChannelId = kotChannelId;
    }

    Long kotChannelId;

    public Long getCat_id() {
        return cat_id;
    }

    public void setCat_id(Long cat_id) {
        this.cat_id = cat_id;
    }

    public  KOT kot;

    int quantity;
    String  priceRef;

    public KOT getKot() {
        return kot;
    }

    public void setKot(KOT kot) {
        this.kot = kot;
    }

    int calculatedQuantiy;
    float calculatedPrice;
    Long cat_id;

    public float getCalculatedPrice() {
        return calculatedPrice;
    }

    public void setCalculatedPrice(float calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    public Long getId() {
        return id;
    }

    public int getCalculatedQuantiy() {
        return calculatedQuantiy;
    }

    public void setCalculatedQuantiy(int calculatedQuantiy) {
        this.calculatedQuantiy = calculatedQuantiy;
    }

    public String getPriceRef() {

        return priceRef;
    }

    public void setPriceRef(String priceRef) {
        this.priceRef = priceRef;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    String price;

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    boolean isSaved;

    public MealTiming getMealTiming() {
        return mealTiming;
    }

    public void setMealTiming(MealTiming mealTiming) {
        this.mealTiming = mealTiming;
    }

    MealTiming mealTiming;

    public static void loadItems(final OnTaskCompleted onTaskCompleted)
    {


        new AsyncTask<Void,Void,List<Item>>() {

            public String jsonResult;
            public String url;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                url = "item/toList";
            }

            @Override
            protected List<Item> doInBackground(Void... voids) {

                jsonResult = HttpHandler.makeServiceCall(this.url,null);
                return (List<Item>) Store.getInstance().itemList;
            }

            @Override
            protected void onPostExecute(List<Item> items) {
                super.onPostExecute(items);
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();
                Type collectionType = new TypeToken<Collection<Item>>() {
                }.getType();
                Collection<Item> items1 = gson.fromJson(jsonResult, collectionType);
                Store.getInstance().itemList = (ArrayList<Item>) items1;

                onTaskCompleted.onTaskCompleted();

            }
        }.execute();


        return;
    }
    public interface OnTaskCompleted{
        void onTaskCompleted();
    }


}
