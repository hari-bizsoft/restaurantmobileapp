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
 * Created by GopiKing on 08-12-2017.
 */

public class KOTChannel {

    Long id;
    String name;
    ArrayList<KOT> kot = new ArrayList<KOT>();

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<KOT> getKot() {
        return kot;
    }

    public void setKot(ArrayList<KOT> kot) {
        this.kot = kot;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static void loadData(final OnTaskCompleted onTaskCompleted)
    {


        new AsyncTask<Void, Void, List<KOTChannel>>() {

            public String jsonResult;
            public String url;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                url = "KOTChannel/toList";
            }

            @Override
            protected List<KOTChannel> doInBackground(Void... voids) {

                jsonResult = HttpHandler.makeServiceCall(this.url,null);
                return (List<KOTChannel>) Store.getInstance().kotChannels;
            }

            @Override
            protected void onPostExecute(List<KOTChannel> items) {
                super.onPostExecute(items);
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();
                Type collectionType = new TypeToken<Collection<KOTChannel>>() {
                }.getType();
                Collection<KOTChannel> collection = gson.fromJson(jsonResult, collectionType);
                Store.getInstance().kotChannels = (ArrayList<KOTChannel>) collection;

                onTaskCompleted.onTaskCompleted();

            }
        }.execute();


        return;
    }
    public static void saveUpdate(final OnTaskCompleted onTaskCompleted, final HashMap<String, String> params) {


        new AsyncTask<Void,Void,Void>()
        {

            String url = "KOTChannel/saveUpdate";
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
                    Type collectionType = new TypeToken<KOTChannel>() {
                    }.getType();
                    KOTChannel user = gson.fromJson(result, collectionType);

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

    public static ArrayList<String> getNames()
    {
        ArrayList<String> strings = new ArrayList<String>();

        for(int i=0;i<Store.getInstance().kotChannels.size();i++)
        {
            strings.add(Store.getInstance().kotChannels.get(i).getName());


        }

        return  strings;
    }

}
