package com.bizsoft.restaurant;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bizsoft.restaurant.adapters.BillAdapter;
import com.bizsoft.restaurant.dataobjects.Bill;
import com.bizsoft.restaurant.dataobjects.Item;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.service.BizUtils;
import com.bizsoft.restaurant.service.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class BillActivity extends AppCompatActivity {

    static ListView listView;
    EditText searchBar;
    ArrayList<Bill> billList;
    ArrayList<Bill> AllBillList = new ArrayList<Bill>();
    private static BillAdapter billAdapter;
    private TextView title;
    ImageButton bills,menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        getSupportActionBar().setTitle("Bill List");
        listView = findViewById(R.id.listview);
        searchBar = (EditText) findViewById(R.id.search_bar);


        BizUtils.addCustomActionBar(BillActivity.this,"Bill List");



        final HashMap<String, String> hm = new HashMap<String, String>();
        new BillList(hm).execute();

        searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                billList = new ArrayList<Bill>();


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                System.out.println("Char Sequence = "+s);
                billList.clear();
                if(TextUtils.isEmpty(s) | s.equals("") | s==null)
                {
                    System.out.println("Adding all the customers");

                    new BillList(hm).execute();
                }
                else {

                    for (int i = 0; i < Store.getInstance().billList.size(); i++) {

                        if (String.valueOf(Store.getInstance().billList.get(i).getId()).contains(s)) {
                            billList.add(Store.getInstance().billList.get(i));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                System.out.println("Adding bill list size"+billList.size());

                System.out.println("Adding bill list size"+billAdapter.billList.size());
                billAdapter.billList.clear();
                billAdapter.billList.addAll(billList);
                System.out.println("Adding customer list size"+billAdapter.billList.size());

                billAdapter.notifyDataSetChanged();
            }
        });



    }
    class BillList extends AsyncTask<Void, Void, Boolean>
    {
        HashMap<String,String> params;
        String url;
        String jsonResult;



        public BillList(HashMap<String,String> params) {
            super();

            url = "bill/toList";
            jsonResult = null;

            this.params = params;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            params.put("type","list");
            params.put("waiterId", String.valueOf(Store.getInstance().user.getId()));


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


                Type collectionType = new TypeToken<Collection<Bill>>() {
                }.getType();

                Collection<Bill> bills = gson.fromJson(jsonResult, collectionType);
                Store.getInstance().billList = (ArrayList<Bill>) bills;
                System.out.println("bill res :"+jsonResult);
                System.out.println("bill size :"+Store.getInstance().billList.size());



                Collections.reverse(Store.getInstance().billList);
                billAdapter =new BillAdapter(BillActivity.this,Store.getInstance().billList);
                listView.setAdapter(billAdapter);





            }
        }


    }
}
