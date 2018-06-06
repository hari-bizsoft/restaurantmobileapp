package com.bizsoft.restaurant.controllers;

import android.os.AsyncTask;

import com.bizsoft.restaurant.Forms.CreateItems;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.dataobjects.UOM;
import com.bizsoft.restaurant.service.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by GopiKing on 16-01-2018.
 */

public class UOMController {



    public static class ToList extends AsyncTask
    {
        private final String action;
        String url;
        private String jsonResult;


        CreateItems onTaskCompleted;


        public ToList(String action) {
            this.url = "UOM/toList";
            this.jsonResult = null;
            this.onTaskCompleted = new CreateItems() ;
            this.action = action;




        }

        @Override
        protected Object doInBackground(Object[] objects) {
            jsonResult = HttpHandler.makeServiceCall(this.url,null);

            return true;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(jsonResult!=null)
            {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                Type collectionType = new TypeToken<Collection<UOM>>() { }.getType();

                UOM uom = new UOM();
                Collection<UOM> uoms = gson.fromJson(jsonResult, collectionType);


                Store.getInstance().uomList = (ArrayList<UOM>) uoms;

                System.out.println("uom res :"+jsonResult);
                System.out.println("uom res :"+Store.getInstance().uomList.size());

                onTaskCompleted.onTaskCompleted(action);




            }

        }

    }
    public interface OnTaskCompleted {
        void onTaskCompleted(String response);


    }

}
