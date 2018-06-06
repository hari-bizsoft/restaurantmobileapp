
package com.bizsoft.restaurant;

import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bizsoft.restaurant.adapters.TableAdapter;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.dataobjects.Table;
import com.bizsoft.restaurant.service.BizUtils;
import com.bizsoft.restaurant.service.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TableActivity extends AppCompatActivity {

    GridView gridView;
    private TextView title;
    ImageButton bills,menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        gridView =  findViewById(R.id.gridview);




        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view =getSupportActionBar().getCustomView();
        title = view.findViewById(R.id.title);
        title.setText(String.valueOf(Store.getInstance().configuration.getTableLabel()+" View"));

        bills =  view.findViewById(R.id.bills);
        menu = (ImageButton)view.findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BizUtils.showMenu(TableActivity.this);

            }
        });
        bills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BizUtils.showBillListDialog(TableActivity.this);

            }
        });




        new TableList().execute();

    }

    class TableList extends AsyncTask<Void, Void, Boolean>
    {

        String url;
        String jsonResult;
        Long id;


        public TableList() {
            super();

            url = "bizTable/toList";
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


                Type collectionType = new TypeToken<Collection<Table>>() {
                }.getType();

                Collection<Table> tables = gson.fromJson(jsonResult, collectionType);


                Store.getInstance().tableList = (ArrayList<Table>) tables;

                System.out.println("item res :"+jsonResult);



                gridView.setAdapter(new TableAdapter(TableActivity.this,Store.getInstance().tableList));








            }
        }


    }

}
