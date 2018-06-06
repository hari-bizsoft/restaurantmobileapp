package com.bizsoft.restaurant;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.bizsoft.restaurant.adapters.CustomSpinnerAdapter;
import com.bizsoft.restaurant.dataobjects.EntityNameSet;
import com.bizsoft.restaurant.dataobjects.ResponseType;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.service.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;


public class ConfigurationActivity2 extends AppCompatActivity {
    EditText category,items,table,user,kot,sessionTimimg;
    Spinner conf;
    CustomSpinnerAdapter confList;
    ArrayList<EntityNameSet> AOBlist;
    Button next;
    private EntityNameSet now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration2);

        conf = (Spinner) findViewById(R.id.spinner);
        category = (EditText) findViewById(R.id.categories);
        items = (EditText) findViewById(R.id.items);
        table = (EditText) findViewById(R.id.table);
        user = (EditText) findViewById(R.id.user);
        kot = (EditText) findViewById(R.id.kot);
        sessionTimimg = (EditText) findViewById(R.id.session_timing);
        next = (Button) findViewById(R.id.next);


        AOBlist = new ArrayList<>();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validate();
            }
        });



        getSupportActionBar().setTitle("Step 2");

        Store.getInstance().configuration.initAOB();

        confList = new CustomSpinnerAdapter(ConfigurationActivity2.this, Store.getInstance().configuration.confNameList,R.layout.dropdown_style_left);
        conf.setAdapter(confList);
        conf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                for(int x=0;x < Store.getInstance().configuration.confList.size();x++) {
                    System.out.println(Store.getInstance().configuration.confNameList.get(i)+"======================"+Store.getInstance().configuration.confList.get(x).getName());
                    if (Store.getInstance().configuration.confNameList.get(i).equals(Store.getInstance().configuration.confList.get(x).getName())) {

                         now = Store.getInstance().configuration.confList.get(x);

                        category.setText(String.valueOf(now.getCategoryLabel()));
                        items.setText(String.valueOf(now.getItemLabel()));
                        table.setText(String.valueOf(now.getTableLabel()));
                        user.setText(String.valueOf(now.getUserLabel()));
                        kot.setText(String.valueOf(now.getKotLabel()));
                        sessionTimimg.setText(String.valueOf(now.getSessionLabel()));


                    }
                    else
                    {
                        System.out.println("Not matching...");
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void validate() {

        boolean status = true;

        if(TextUtils.isEmpty(category.getText()))
        {
            category.setError("Please Fill !");
            status = false;
        }
        if(TextUtils.isEmpty(items.getText()))
        {
            items.setError("Please Fill !");
            status = false;
        }
        if(TextUtils.isEmpty(table.getText()))
        {
            table.setError("Please Fill !");
            status = false;
        }
        if(TextUtils.isEmpty(user.getText()))
        {
            user.setError("Please Fill !");
            status = false;
        }
        if(TextUtils.isEmpty(kot.getText()))
        {
            kot.setError("Please Fill !");
            status = false;
        }
        if(TextUtils.isEmpty(sessionTimimg.getText()))
        {
            sessionTimimg.setError("Please Fill !");
            status = false;
        }

        if(status)
        {
            if(now!=null)
            {
                if(now.getName()!=null)
                {
                Store.getInstance().configuration.setCategoryLabel(category.getText().toString());
                Store.getInstance().configuration.setItemLabel(items.getText().toString());
                Store.getInstance().configuration.setTableLabel(table.getText().toString());
                Store.getInstance().configuration.setKotLabel(kot.getText().toString());
                Store.getInstance().configuration.setSessionLabel(sessionTimimg.getText().toString());
                Store.getInstance().configuration.setUserLabel(user.getText().toString());
                Store.getInstance().configuration.setConfName(now.getName());



                   // Intent intent = new Intent(ConfigurationActivity2.this,MainActivity.class);
                    //startActivity(intent);
                    new StoreConf().execute();
                         }
                }


        }

    }
    class StoreConf extends AsyncTask
    {
        HashMap<String,String> params;
        String url = "configuration/saveDetails";
        String response= "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            params = new HashMap<String, String>();
            params.put("confName",Store.getInstance().configuration.getConfName());
            params.put("appLogo",Store.getInstance().configuration.getAppLogoMobileSource());
            params.put("appName",Store.getInstance().configuration.getAppName());
            params.put("companyName",Store.getInstance().configuration.getCompanyName());
            params.put("categoryLabel",Store.getInstance().configuration.getCategoryLabel());
            params.put("itemLabel",Store.getInstance().configuration.getItemLabel());
            params.put("tableLabel",Store.getInstance().configuration.getTableLabel());
            params.put("userLabel",Store.getInstance().configuration.getUserLabel());
            params.put("kotLabel",Store.getInstance().configuration.getKotLabel());
            params.put("sessionLabel",Store.getInstance().configuration.getSessionLabel());

        }


        @Override
        protected Object doInBackground(Object[] objects) {

            response =  HttpHandler.makeServiceCall(url,params);

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            System.out.println("Response = "+response);
            if(response.toLowerCase().contains("success"))
            {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();
                Type collectionType = new TypeToken<ResponseType>() {
                }.getType();
                ResponseType responseType  = gson.fromJson(response, collectionType);

                SharedPreferences sp = Store.getInstance().getConfPref(ConfigurationActivity2.this);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean(Store.getInstance().confIsStored, true);
                editor.putLong(Store.getInstance().confId, responseType.id);
                editor.commit();


                System.out.println("=========="+sp.getLong(Store.getInstance().confId,0));
                System.out.println("=========="+sp.getBoolean(Store.getInstance().confIsStored,false));
                startActivity(new Intent(ConfigurationActivity2.this,
                        LoginActivity.class));


                finish();


            }
        }

    }


}
