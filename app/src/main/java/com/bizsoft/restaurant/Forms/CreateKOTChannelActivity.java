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
import android.widget.EditText;
import android.widget.Toast;

import com.bizsoft.restaurant.R;
import com.bizsoft.restaurant.dataobjects.KOTChannel;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.service.BizUtils;

import java.util.ArrayList;
import java.util.HashMap;

import me.anwarshahriar.calligrapher.Calligrapher;

import static com.bizsoft.restaurant.service.BizUtils.println;

public class CreateKOTChannelActivity extends AppCompatActivity {
    Button saveUpdate;
    EditText name;
    AutoCompleteTextView keyword;
    private ArrayAdapter<String> uomAdapter;
    private KOTChannel uom;
    private Long uom_id = Long.valueOf(0);
    private KOTChannel.OnTaskCompleted onTaskCompleted_u;
    private HashMap<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_kotchannel);

        keyword = findViewById(R.id.keyword);
        name = findViewById(R.id.name);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "fonts/Quicksand-BoldItalic.otf", true);
        getSupportActionBar().setTitle("KOT Channel Create/Update");
        BizUtils.addCustomActionBar(CreateKOTChannelActivity.this,"KOT Channel Create/Update");
        onTaskCompleted_u = new KOTChannel.OnTaskCompleted() {
            @Override
            public void onTaskCompleted() {
                println("getting uom list");

                setList(CreateKOTChannelActivity.this, Store.getInstance().kotChannels,keyword,Store.getInstance().dropDownLayout);


            }
        };
        KOTChannel.loadData(onTaskCompleted_u);

    }
    private void setList(Context context, final ArrayList<KOTChannel> uoms , AutoCompleteTextView keyword, int layout) {

        String[] listViewAdapterContent = new String[uoms.size()];
        for(int i=0;i<uoms.size();i++)
        {
            listViewAdapterContent[i]= uoms.get(i).getName();
        }
        uomAdapter = new ArrayAdapter<>(context, layout, listViewAdapterContent);
        keyword.setAdapter(uomAdapter );

        keyword.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = (String)adapterView.getItemAtPosition(i);
                for(int x=0;x<uoms.size();x++)
                {
                    if(selection.equals(uoms.get(x).getName()))
                    {
                        uom = uoms.get(x);
                        uom_id = uoms.get(x).getId();
                        // Toast.makeText(CreateCategoryActivity.this, "cat Id ---"+cat.getId(), Toast.LENGTH_SHORT).show();
                        setFields(uom);
                    }
                }
            }
        });
    }

    private void setFields(KOTChannel uom) {

        name.setText(uom.getName().toString());
    }
    public void setSaveUpdate(View v)
    {
        if(validate())
        {
            params = new HashMap<String, String>();

            println("passed");

            if (uom != null) {
                params.put("to", "update");
                params.put("id", String.valueOf(uom.getId()));

            } else {
                params.put("to", "save");

            }
            params.put("name",name.getText().toString());

            KOTChannel.OnTaskCompleted onTaskCompleted = new KOTChannel.OnTaskCompleted() {
                @Override
                public void onTaskCompleted() {

                    println("User saved");

                    Toast.makeText(CreateKOTChannelActivity.this, "Saved...", Toast.LENGTH_SHORT).show();
                    KOTChannel.loadData(onTaskCompleted_u);


                    clear(null);
                }
            };

            KOTChannel.saveUpdate(onTaskCompleted,params);


        }
        else
        {
            println("failed");
        }
    }

    private boolean validate() {


        boolean status = true;

        if(TextUtils.isEmpty(name.getText()))
        {
            name.setError("Please Fill..");
            status = false;
        }




        return status;
    }
    public void clear(View v)
    {

        name.setError(null);
        name.setText("");
        keyword.setText("");
        uom = null;
        uom_id = Long.valueOf(0);

    }
}
