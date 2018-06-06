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
 * Created by GopiKing on 10-01-2018.
 */

public class MealTiming {

    Long id;
    String name;
    String startTime;
    String endTime;

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public static void loadData(final OnTaskCompleted onTaskCompleted)
    {

        new AsyncTask<Void, Void, List<MealTiming>>() {

            public String jsonResult;
            public String url;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                url = "mealTiming/toList";
            }

            @Override
            protected List<MealTiming> doInBackground(Void... voids) {

                jsonResult = HttpHandler.makeServiceCall(this.url,null);
                return (List<MealTiming>) Store.getInstance().mealTimingList;
            }

            @Override
            protected void onPostExecute(List<MealTiming> items) {
                super.onPostExecute(items);
                if(jsonResult!=null)
                {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                    Gson gson = gsonBuilder.create();
                    Type collectionType = new TypeToken<Collection<MealTiming>>() {
                    }.getType();
                    Collection<MealTiming> tables = gson.fromJson(jsonResult, collectionType);
                    Store.getInstance().mealTimingList = (ArrayList<MealTiming>) tables;
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

            String url = "mealTiming/saveUpdate";
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
                    Type collectionType = new TypeToken<MealTiming>() {
                    }.getType();
                    MealTiming mealTiming = gson.fromJson(result, collectionType);

                    System.out.println("uom res :"+mealTiming.getId());
                    if(mealTiming.getId()!=null) {
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
