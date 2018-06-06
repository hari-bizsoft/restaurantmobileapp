package com.bizsoft.restaurant;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bizsoft.restaurant.adapters.CustomSpinnerAdapter;
import com.bizsoft.restaurant.adapters.KOTItemListAdapter;
import com.bizsoft.restaurant.dataobjects.Item;
import com.bizsoft.restaurant.dataobjects.KOTChannel;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.service.BizUtils;
import com.bizsoft.restaurant.service.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class KOTViewActivity extends AppCompatActivity {

    static Spinner kotChannel;
    static ListView listView;
    private static String kotChannelValue;
    private static Long kotChannelValueId;
    public static int kotChannelPosition;
    public TextView kot_label;
    public TextView item_name;
    TextView table_label;

    FloatingActionButton refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kotview);

        getSupportActionBar().setTitle(Store.getInstance().configuration.getKotLabel()+" System View");

        BizUtils.addCustomActionBar(KOTViewActivity.this,Store.getInstance().configuration.getKotLabel()+" View");
        kotChannel = (Spinner) findViewById(R.id.kot_channel);
        listView = (ListView) findViewById(R.id.listview);
        refresh = (FloatingActionButton) findViewById(R.id.refresh);

        kot_label = (TextView) findViewById(R.id.kot_label);
        item_name = (TextView) findViewById(R.id.item_name);
        table_label = (TextView) findViewById(R.id.table_label);

        kot_label.setText(String.valueOf(Store.getInstance().configuration.getKotLabel()+" Channel"));
        item_name.setText(String.valueOf(Store.getInstance().configuration.getItemLabel()+" Name"));
        table_label.setText(String.valueOf(Store.getInstance().configuration.getTableLabel()));


        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new KOTChannelList(KOTViewActivity.this).execute();
            }
        });
        new KOTChannelList(KOTViewActivity.this).execute();
        }
    public static void setKOTChannels(final Context context) {

        final ArrayList<String> genderList = new ArrayList<String>();

        for(int i=0;i<Store.getInstance().kotChannels.size();i++)
        {
            System.out.println("SIZE =="+Store.getInstance().kotChannels.get(i).getKot().size());
            genderList.add(Store.getInstance().kotChannels.get(i).getName());

            for(int x=0;x<Store.getInstance().kotChannels.get(i).getKot().size();x++)
            {

                System.out.println("KTC =="+Store.getInstance().kotChannels.get(i).getKot().get(x).getId());

            }
        }

        // Drop down layout style - list view with radio button
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(context, genderList,R.layout.dropdown_style);
        kotChannel.setAdapter(customSpinnerAdapter);

        kotChannel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  Toast.makeText(getApplicationContext(), genderList.get(position), Toast.LENGTH_SHORT).show();
                kotChannelValue = Store.getInstance().kotChannels.get(position).getName();
                kotChannelValueId = Store.getInstance().kotChannels.get(position).getId();
                kotChannelPosition = position;

                listView.setAdapter(new KOTItemListAdapter(context,Store.getInstance().kotChannels.get(position).getKot()));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                kotChannelValue = Store.getInstance().kotChannels.get(0).getName();
                kotChannelValueId = Store.getInstance().kotChannels.get(0).getId();
                kotChannelPosition = 0;

                listView.setAdapter(new KOTItemListAdapter(context,Store.getInstance().kotChannels.get(0).getKot()));

            }


        });


    }
    public static class KOTChannelList extends AsyncTask<Void, Void, Boolean>
    {
        HashMap<String,String> params;
        String url;
        String jsonResult;
        Context context;



        public KOTChannelList(Context context) {
            super();

            url = "KOTChannel/toList";
            jsonResult = null;
            this.context = context;

            params = new HashMap<String, String>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();




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
                System.out.println("KOT Channel =="+jsonResult);


                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();


                Type collectionType = new TypeToken<Collection<KOTChannel>>() {
                }.getType();
                Collection<KOTChannel> kotChannels= gson.fromJson(jsonResult, collectionType);
                Store.getInstance().kotChannels = (ArrayList<KOTChannel>) kotChannels;

                System.out.println("KOT Channel =="+Store.getInstance().kotChannels);
                setKOTChannels(context);

            }
        }


    }
}
