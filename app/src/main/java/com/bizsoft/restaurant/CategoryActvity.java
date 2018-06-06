package com.bizsoft.restaurant;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bizsoft.restaurant.Forms.UserActivity;
import com.bizsoft.restaurant.adapters.CategoryListAdapter;
import com.bizsoft.restaurant.dataobjects.Category;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.service.BizUtils;
import com.bizsoft.restaurant.service.HttpHandler;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import me.anwarshahriar.calligrapher.Calligrapher;

public class CategoryActvity extends AppCompatActivity {

    ImageView image;
    ListView listview;
    TextView resName;
    private FloatingActionButton info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_actvity);

        getSupportActionBar().setTitle("Categories");
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "fonts/Quicksand-BoldItalic.otf", true);
        BizUtils.addCustomActionBar(CategoryActvity.this,"Categories");

        listview = findViewById(R.id.listview);
        image = findViewById(R.id.image);
        resName = findViewById(R.id.res_name);
        info =  findViewById(R.id.info);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Store.getInstance().currentResInfo = Store.getInstance().currentRestaurant;
                Intent intent =  new Intent(CategoryActvity.this,InfoActivity.class);
                startActivity(intent);
            }
        });

        resName.setText(String.valueOf(Store.getInstance().currentRestaurant.getName()));

        Glide.with(CategoryActvity.this).load(Store.getInstance().baseUrl + Store.getInstance().currentResImage).into(image);
        System.out.println("ID----"+Store.getInstance().currentResId);
        new CatList(Store.getInstance().currentResId).execute();




    }

    class CatList extends AsyncTask<Void, Void, Boolean>
    {
        HashMap<String,String> params;
        String url;
        String jsonResult;
        Long id;


        public CatList(long currentCategoryId) {
            super();

            url = "category/toList";
            jsonResult = null;
            this.id = currentCategoryId;
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


                Type collectionType = new TypeToken<Collection<Category>>() {
                }.getType();

                Collection<Category> restaurants = gson.fromJson(jsonResult, collectionType);


                Store.getInstance().categoryList = (ArrayList<Category>) restaurants;

                System.out.println("cat res :"+jsonResult);


                System.out.println("catList Size  :"+Store.getInstance().categoryList.size());
                listview.setAdapter(new CategoryListAdapter(CategoryActvity.this,Store.getInstance().categoryList));

            }
        }


    }
}
