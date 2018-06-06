package com.bizsoft.restaurant.dataobjects;

import android.os.AsyncTask;

import com.bizsoft.restaurant.service.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static com.bizsoft.restaurant.service.BizUtils.println;

/**
 * Created by GopiKing on 16-01-2018.
 */

public class UOM  {
    Long id;
    String name;

    public UOM() {

    }

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

        new AsyncTask<Void, Void, List<UOM>>() {

            public String jsonResult;
            public String url;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                url = "UOM/toList";
            }

            @Override
            protected List<UOM> doInBackground(Void... voids) {

                jsonResult = HttpHandler.makeServiceCall(this.url,null);
                return (List<UOM>) Store.getInstance().uomList;
            }

            @Override
            protected void onPostExecute(List<UOM> items) {
                super.onPostExecute(items);
                if(jsonResult!=null)
                {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                    Gson gson = gsonBuilder.create();
                    Type collectionType = new TypeToken<Collection<UOM>>() {
                    }.getType();
                    Collection<UOM> tables = gson.fromJson(jsonResult, collectionType);
                    Store.getInstance().uomList = (ArrayList<UOM>) tables;
                    System.out.println("uom  res :"+jsonResult);

                    onTaskCompleted.onTaskCompleted();
                }
                else
                {
                    println("Result :"+jsonResult);
                }

            }
        }.execute();


        return;
    }
    public static void saveUpdate(final OnTaskCompleted onTaskCompleted, final HashMap<String, String> params) {


        new AsyncTask<Void,Void,Void>()
        {

            String url = "UOM/saveUpdate";
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
                    println(result);
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                    Gson gson = gsonBuilder.create();
                    Type collectionType = new TypeToken<UOM>() {
                    }.getType();
                    UOM user = gson.fromJson(result, collectionType);

                    System.out.println("uom res :"+user.getId());
                    if(user.getId()!=null) {
                        onTaskCompleted.onTaskCompleted();
                    }
                    else
                    {
                        println("Not saved");
                    }

                }
                else
                {
                    println("Result null..");
                }
            }
        }.execute();
    }
    public interface OnTaskCompleted{
        void onTaskCompleted();
    }
}
