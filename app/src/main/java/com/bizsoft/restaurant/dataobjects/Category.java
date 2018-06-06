package com.bizsoft.restaurant.dataobjects;

import android.os.AsyncTask;

import com.bizsoft.restaurant.service.BizUtils;
import com.bizsoft.restaurant.service.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by GopiKing on 01-11-2017.
 */

public class Category {

    Long id;
    String name;

    Long kotChannel_id;

    public int getItemSize() {
        return itemSize;
    }

    public Long getKotChannel_id() {
        return kotChannel_id;
    }

    public void setKotChannel_id(Long kotChannel_id) {
        this.kotChannel_id = kotChannel_id;
    }

    public void setItemSize(int itemSize) {
        this.itemSize = itemSize;
    }

    int itemSize;


    public Long getId() {
        return id;
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



    public static void loadData(final OnTaskCompleted onTaskCompleted)
    {


        new AsyncTask<Void, Void, List<Category>>() {

            public String jsonResult;
            public String url;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                url = "category/toList";
            }

            @Override
            protected List<Category> doInBackground(Void... voids) {

                jsonResult = HttpHandler.makeServiceCall(this.url,null);
                return (List<Category>) Store.getInstance().categoryList;
            }

            @Override
            protected void onPostExecute(List<Category> items) {
                super.onPostExecute(items);
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();
                Type collectionType = new TypeToken<Collection<Category>>() {
                }.getType();
                Collection<Category> collection = gson.fromJson(jsonResult, collectionType);
                Store.getInstance().categoryList = (ArrayList<Category>) collection;

                onTaskCompleted.onTaskCompleted();

            }
        }.execute();


        return;
    }
    public static void saveUpdate(final OnTaskCompleted onTaskCompleted, final HashMap<String, String> params)
    {
        new AsyncTask<Void,Void,Void>()
        {

            String url = "category/saveUpdate";
            String result = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


            }
            @Override
            protected Void doInBackground(Void... voids) {
                result = HttpHandler.makeServiceCall(this.url,params);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(result!=null)
                {
                    BizUtils.println(result);
                    onTaskCompleted.onTaskCompleted();

                }
                else
                {
                    BizUtils.println("Result null..");
                }
            }
        }.execute();



    }
    public interface OnTaskCompleted{
        void onTaskCompleted();
    }
}
