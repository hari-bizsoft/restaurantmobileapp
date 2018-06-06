package com.bizsoft.restaurant.Forms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.bizsoft.restaurant.BillActivity;
import com.bizsoft.restaurant.R;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.dataobjects.Table;
import com.bizsoft.restaurant.service.BizUtils;

import java.util.ArrayList;
import java.util.HashMap;

import me.anwarshahriar.calligrapher.Calligrapher;

import static com.bizsoft.restaurant.service.BizUtils.println;

public class BizTableActivity extends AppCompatActivity {

     AutoCompleteTextView keyword;
    AutoCompleteTextView name;
    Table.OnTaskCompleted onTaskCompleted_t;
    private ArrayAdapter<String> tabAdaptor;
    private Table table;
    Long table_id = Long.valueOf(0);
    private ArrayAdapter<String> catAdaptor;
    int layoutItemId = android.R.layout.simple_dropdown_item_1line;
    private Button saveUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biz_table);
        Calligrapher calligrapher = new Calligrapher(this);


        getSupportActionBar().setTitle("Table Create/Update");


        BizUtils.addCustomActionBar(BizTableActivity.this,"Table Create/Update");

        calligrapher.setFont(this, "fonts/Quicksand-BoldItalic.otf", true);

        keyword = findViewById(R.id.keyword);
        name = findViewById(R.id.name);
        saveUpdate = findViewById(R.id.saveupdate);



         onTaskCompleted_t = new Table.OnTaskCompleted() {
            @Override
            public void onTaskCompleted() {




                println("getting table list");
                setTableLsit(Store.getInstance().tableList,keyword,Store.getInstance().dropDownLayout);


            }
        };
         Table.loadData(onTaskCompleted_t);






    }

    private void setTableLsit(final ArrayList<Table> tableList, AutoCompleteTextView keyword, int layout) {

        String[] listViewAdapterContent = new String[tableList.size()];
        for(int i=0;i<tableList.size();i++)
        {
            listViewAdapterContent[i]= tableList.get(i).getName();
        }
        tabAdaptor = new ArrayAdapter<>(BizTableActivity.this, layout, listViewAdapterContent);
        keyword.setAdapter(tabAdaptor);

        keyword.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = (String)adapterView.getItemAtPosition(i);
                for(int x=0;x<tableList.size();x++)
                {
                    if(selection.equals(tableList.get(x).getName()))
                    {
                        table = tableList.get(x);
                        table_id = tableList.get(x).getId();
                        // Toast.makeText(CreateCategoryActivity.this, "cat Id ---"+cat.getId(), Toast.LENGTH_SHORT).show();
                        setFields(table);
                    }
                }
            }
        });
    }

    private void setFields(Table table) {

        name.setText(table.getName());


    }
    public void saveUpdate(View v)
    {

        if(validate())
        {

            HashMap<String, String> params = new HashMap<String, String>();

            if (table != null) {
                params.put("to", "update");
                params.put("id", String.valueOf(table.getId()));

            } else {
                params.put("to", "save");


            }
            params.put("name", name.getText().toString());


            Table.OnTaskCompleted onTaskCompleted = new Table.OnTaskCompleted() {
                @Override
                public void onTaskCompleted() {

                    println("saved...");
                    Toast.makeText(BizTableActivity.this, "Saved..", Toast.LENGTH_SHORT).show();
                    Table.loadData(onTaskCompleted_t);
                    clear();

                }
            };
            Table.saveUpdate(onTaskCompleted, params);


        }
        else
        {
            println("----validate failed---");
        }

    }

    private boolean validate() {
        boolean status = true;

        if(TextUtils.isEmpty(name.getText()))
        {
            status = false;
            name.setError("Please Fill");
        }

        return status;
    }
    public void clear()
    {


        table = null;
        table_id = Long.valueOf(0);
        keyword.setText("");
        name.setText("");



    }
}
