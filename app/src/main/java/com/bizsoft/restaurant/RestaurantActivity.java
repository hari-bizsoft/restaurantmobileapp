package com.bizsoft.restaurant;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bizsoft.restaurant.adapters.RestaurantListAdapter;
import com.bizsoft.restaurant.dataobjects.Restaurant;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.service.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import me.anwarshahriar.calligrapher.Calligrapher;

public class RestaurantActivity extends AppCompatActivity {

    ListView listView;
    private RestaurantListAdapter restaurantAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "fonts/Quicksand-BoldItalic.otf", true);
        listView = findViewById(R.id.listview);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Store.getInstance().currentResId = restaurantAdapter.getItemId(position);
                Store.getInstance().currentResImage = restaurantAdapter.getImage(position);

                System.out.println("ID----"+Store.getInstance().currentResId);
                Intent intent =  new Intent(RestaurantActivity.this,CategoryActvity.class);
                startActivity(intent);



            }
        });

        new ResList().execute();
    }
    class ResList extends AsyncTask<Void, Void, Boolean>
    {
        HashMap<String,String> params;
        String url;
        String jsonResult;


        public ResList() {
            super();

            url = "/restaurant/reslist";
            jsonResult = null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            jsonResult = HttpHandler.makeServiceCall(this.url,null);
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


                Type collectionType = new TypeToken<Collection<Restaurant>>() {
                }.getType();

                Collection<Restaurant> restaurants = gson.fromJson(jsonResult, collectionType);


                Store.getInstance().restaurantList = (ArrayList<Restaurant>) restaurants;

                System.out.println("ResList :"+jsonResult);


                System.out.println("ResList Size  :"+Store.getInstance().restaurantList.size());
                restaurantAdapter = new RestaurantListAdapter(RestaurantActivity.this,Store.getInstance().restaurantList);
                listView.setAdapter(restaurantAdapter);

            }
        }


    }
}
