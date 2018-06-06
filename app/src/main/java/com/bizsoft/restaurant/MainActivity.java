package com.bizsoft.restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bizsoft.restaurant.dataobjects.Configuration;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.service.HttpHandler;
import com.bizsoft.restaurant.service.StartActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

import me.anwarshahriar.calligrapher.Calligrapher;

public class MainActivity extends AppCompatActivity {

    protected boolean _active = true;
    protected int _splashTime = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "fonts/GreatVibes-Regular.otf", true);
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (Exception e) {

                } finally {


                    SharedPreferences sp = Store.getInstance().getConfPref(MainActivity.this);
                    boolean status = sp.getBoolean(Store.getInstance().confIsStored,false);
                    if(status)
                    {

                        Intent intent  =new Intent(MainActivity.this,
                                LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                        new GetConf().execute();


                    }
                    else
                    {
                        Intent intent  =new Intent(MainActivity.this,
                                StartActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }


                }
            }

            ;
        };
        splashTread.start();

    }
    class GetConf extends AsyncTask
    {
        HashMap<String,String> params;
        String url = "configuration/getConfig";
        String response= "";
        Long id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params = new HashMap<String, String>();

            SharedPreferences sp = Store.getInstance().getConfPref(MainActivity.this);
            id = sp.getLong(Store.getInstance().confId,0);
            params.put("id", String.valueOf(id));


        }

        @Override
        protected Object doInBackground(Object[] objects) {
            response = HttpHandler.makeServiceCall(url,params);

            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            System.out.println("Res = "+response);

            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setDateFormat("M/d/yy hh:mm a");
            Gson gson = gsonBuilder.create();
            Type collectionType = new TypeToken<Configuration>() {
            }.getType();
            Configuration configuration = gson.fromJson(response, collectionType);
            System.out.println("Image byte == "+configuration.getItemLabel());

            Store.getInstance().configuration.setAppLogo(configuration.getAppLogo());
            Store.getInstance().configuration.setAppName(configuration.getAppName());
            Store.getInstance().configuration.setCompanyName(configuration.getCompanyName());
            Store.getInstance().configuration.setConfName(configuration.getConfName());
            Store.getInstance().configuration.setItemLabel(configuration.getItemLabel());
            Store.getInstance().configuration.setCategoryLabel(configuration.getCategoryLabel());
            Store.getInstance().configuration.setTableLabel(configuration.getTableLabel());
            Store.getInstance().configuration.setKotLabel(configuration.getKotLabel());
            Store.getInstance().configuration.setUserLabel(configuration.getUserLabel());
            Store.getInstance().configuration.setSessionLabel(configuration.getSessionLabel());


            System.out.println("Image byte == "+Store.getInstance().configuration.getAppLogo().length);





        }

    }
}