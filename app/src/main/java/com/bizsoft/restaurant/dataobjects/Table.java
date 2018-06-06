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

import static com.bizsoft.restaurant.service.BizUtils.println;

/**
 * Created by GopiKing on 28-11-2017.
 */

public class Table {
    Long id;
    String name;
    String status;




    ArrayList<Bill> billList = new ArrayList<Bill>();






    public Long getId() {
        return id;
    }

    public ArrayList<Bill> getBillList() {
        return billList;
    }

    public void setBillList(ArrayList<Bill> billList) {
        this.billList = billList;
    }

    public String getStatus() {
        return status;

    }

    public void setStatus(String status) {
        this.status = status;
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
                url = "bizTable/toList";
            }

            @Override
            protected List<Category> doInBackground(Void... voids) {

                jsonResult = HttpHandler.makeServiceCall(this.url,null);
                return (List<Category>) Store.getInstance().categoryList;
            }

            @Override
            protected void onPostExecute(List<Category> items) {
                super.onPostExecute(items);
                if(jsonResult!=null)
                {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                    Gson gson = gsonBuilder.create();
                    Type collectionType = new TypeToken<Collection<Table>>() {
                    }.getType();
                    Collection<Table> tables = gson.fromJson(jsonResult, collectionType);
                    Store.getInstance().tableList = (ArrayList<Table>) tables;
                    System.out.println("item res :"+jsonResult);

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

            String url = "bizTable/saveUpdate";
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
                    Type collectionType = new TypeToken<Table>() {
                    }.getType();
                    Table tables = gson.fromJson(result, collectionType);
                    Table table = (Table) tables;
                    System.out.println("table res :"+table.getId());
                    if(table.getId()!=null) {
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
