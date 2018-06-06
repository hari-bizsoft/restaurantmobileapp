package com.bizsoft.restaurant;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Spinner;

import com.bizsoft.restaurant.adapters.ItemListAdapter;
import com.bizsoft.restaurant.dataobjects.Item;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.service.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ItemsActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);
        listView = findViewById(R.id.listview);

        new ItemList(0).execute();


    }

    class ItemList extends AsyncTask<Void, Void, Boolean>
    {
        HashMap<String,String> params;
        String url;
        String jsonResult;
        Long id;


        public ItemList(long itemId) {
            super();

            url = "item/toList";
            jsonResult = null;
            this.id = itemId;
            params = new HashMap<String, String>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            params.put("id", String.valueOf(id));

        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            jsonResult = HttpHandler.makeServiceCall(this.url,params);
            return true;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(jsonResult!=null)
            {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();


                Type collectionType = new TypeToken<Collection<Item>>() {
                }.getType();

                Collection<Item> items = gson.fromJson(jsonResult, collectionType);


                Store.getInstance().itemList = (ArrayList<Item>) items;

                System.out.println("item res :"+jsonResult);


                System.out.println("item Size  :"+Store.getInstance().itemList.size());
                System.out.println("added item Size  :"+Store.getInstance().addedItemList.size());

                listView.setAdapter(new ItemListAdapter(ItemsActivity.this,Store.getInstance().itemList));


                //itemCount.setText(String.valueOf(Store.getInstance().itemList.size()));


            }
        }


    }
}
