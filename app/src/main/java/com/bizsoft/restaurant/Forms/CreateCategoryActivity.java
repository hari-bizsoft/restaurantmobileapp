package com.bizsoft.restaurant.Forms;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.bizsoft.restaurant.R;
import com.bizsoft.restaurant.adapters.CustomSpinnerAdapter;
import com.bizsoft.restaurant.dataobjects.Category;
import com.bizsoft.restaurant.dataobjects.KOTChannel;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.service.BizUtils;

import java.util.ArrayList;
import java.util.HashMap;

import me.anwarshahriar.calligrapher.Calligrapher;

import static com.bizsoft.restaurant.service.BizUtils.println;

public class CreateCategoryActivity extends AppCompatActivity {

    AutoCompleteTextView keyword,category;
    Spinner kotChannel;
    int layoutItemId = android.R.layout.simple_dropdown_item_1line;
    private Category cat;
    Long cat_id = Long.valueOf(0);
    private ArrayAdapter<String> catAdaptor;
    private String kotChannelValue;
    private Long kotChannelValueId = Long.valueOf(0);
    private int kotChannelPosition =0;
    Button saveUpdate;
    private Category.OnTaskCompleted onTaskCompleted_catlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "fonts/Quicksand-BoldItalic.otf", true);
        category = findViewById(R.id.category);
        keyword = findViewById(R.id.keyword);
        kotChannel = findViewById(R.id.kot_channel);
        saveUpdate = findViewById(R.id.saveupdate);

        getSupportActionBar().setTitle("Category Create/Update");
        BizUtils.addCustomActionBar(CreateCategoryActivity.this,"Category Create/Update");

        onTaskCompleted_catlist = new Category.OnTaskCompleted() {
            @Override
            public void onTaskCompleted() {

                setCategory(Store.getInstance().categoryList,keyword,layoutItemId);
            }
        };
        Category.loadData(onTaskCompleted_catlist);

        KOTChannel.OnTaskCompleted onTaskCompleted1 = new KOTChannel.OnTaskCompleted() {
            @Override
            public void onTaskCompleted() {

                println("Kot update");
                setKotChannel(CreateCategoryActivity.this, KOTChannel.getNames(),kotChannel,R.layout.dropdown_style);

            }
        };

        KOTChannel.loadData(onTaskCompleted1);



    }
    public void setCategory(final ArrayList<Category> category, AutoCompleteTextView keyword, int layout)
    {
        String[] listViewAdapterContent = new String[category.size()];
        for(int i=0;i<category.size();i++)
        {
            listViewAdapterContent[i]=category.get(i).getName();
        }
        catAdaptor = new ArrayAdapter<>(CreateCategoryActivity.this, layout, listViewAdapterContent);
        keyword.setAdapter(catAdaptor);
        keyword.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = (String)adapterView.getItemAtPosition(i);
                for(int x=0;x<category.size();x++)
                {
                    if(selection.equals(category.get(x).getName()))
                    {
                        cat = category.get(x);
                        cat_id = category.get(x).getId();
                       // Toast.makeText(CreateCategoryActivity.this, "cat Id ---"+cat.getId(), Toast.LENGTH_SHORT).show();
                        setFields(cat);
                    }
                }
            }
        });
    }

    public void setKotChannel(final Context context, final ArrayList<String> list, Spinner keyword, int layout)
    {


        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(context, list,layout);
        keyword.setAdapter(customSpinnerAdapter);


        keyword.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  Toast.makeText(getApplicationContext(), genderList.get(position), Toast.LENGTH_SHORT).show();
                kotChannelValue = Store.getInstance().kotChannels.get(position).getName();
                kotChannelValueId = Store.getInstance().kotChannels.get(position).getId();
                kotChannelPosition = position;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                kotChannelValue = Store.getInstance().kotChannels.get(0).getName();
                kotChannelValueId = Store.getInstance().kotChannels.get(0).getId();
                kotChannelPosition = 0;


            }


        });
    }
    private void setFields(Category cat) {

        category.setText(String.valueOf(cat.getName()));

        for(int i=0;i<Store.getInstance().kotChannels.size();i++)
        {
            if(Store.getInstance().kotChannels.get(i).getId().equals(cat.getKotChannel_id()))
            {
                kotChannel.setSelection(i);

                kotChannelValueId = cat.getKotChannel_id();
                cat_id = cat.getId();



            }

        }

    }

   public void setSaveUpdate(View v)
   {

       if(validate()) {


           Category.OnTaskCompleted onTaskCompleted = new Category.OnTaskCompleted() {
               @Override
               public void onTaskCompleted() {


                   BizUtils.println("Saving / Updating category..");

                   Toast.makeText(CreateCategoryActivity.this, "Saved..", Toast.LENGTH_SHORT).show();

                   Category.loadData(onTaskCompleted_catlist);

                   clear();

               }
           };

           HashMap<String, String> params = new HashMap<String, String>();

           if (cat != null) {
               params.put("to", "update");
               params.put("id", String.valueOf(cat.getId()));

           } else {
               params.put("to", "save");


           }
           params.put("name", category.getText().toString());
           params.put("kot_id", String.valueOf(kotChannelValueId));


           Category.saveUpdate(onTaskCompleted, params);
       }
       else
       {
           BizUtils.println("Validate failed..");

       }
   }

    private boolean validate() {
        boolean status = true;

        if(TextUtils.isEmpty(keyword.getText()))
        {
            cat = null;
            cat_id = Long.valueOf(0);

        }

        if(TextUtils.isEmpty(category.getText()))
        {
            category.setError("Please Fill");
            status = false;
        }





        return  status;
    }

    private void clear() {

        cat =null;
        category.setText("");
        cat_id = Long.valueOf(0);


        kotChannelValue = null;
        kotChannelPosition =0;
        keyword.setText("");

        kotChannelValueId = Long.valueOf(0);
        kotChannel.clearFocus();




    }

}
