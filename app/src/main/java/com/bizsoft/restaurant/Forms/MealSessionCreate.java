package com.bizsoft.restaurant.Forms;

import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bizsoft.restaurant.R;
import com.bizsoft.restaurant.dataobjects.KOTChannel;
import com.bizsoft.restaurant.dataobjects.MealTiming;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.dataobjects.UOM;
import com.bizsoft.restaurant.service.BizUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import me.anwarshahriar.calligrapher.Calligrapher;

import static android.text.method.TextKeyListener.clear;
import static com.bizsoft.restaurant.service.BizUtils.println;


public class MealSessionCreate extends AppCompatActivity {
    Button saveUpdate;
    EditText name;
    AutoCompleteTextView keyword;
    private ArrayAdapter<String> uomAdapter;
    private MealTiming mealTiming;

    private MealTiming.OnTaskCompleted onTaskCompleted_u;
    private HashMap<String,String> params;
    private Long mealTiming_id = Long.valueOf(0);
    private String FLAG_TIME;
    private int hour,minute;
    private int seconds;
    EditText fromTime,toTime;
    ImageButton fromTimeButton,toTimeButton;
    private String mealTimingName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_session_create);


        keyword = findViewById(R.id.keyword);
        name = findViewById(R.id.name);


        fromTime= findViewById(R.id.from_time);
        toTime = findViewById(R.id.to_time);


        BizUtils.addCustomActionBar(MealSessionCreate.this,"Meal Session Create/Update");
        fromTimeButton= findViewById(R.id.from_time_button);
        toTimeButton = findViewById(R.id.to_time_button);
        fromTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FLAG_TIME = "fromtime";
                showDialog(916);
            }
        });

        toTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FLAG_TIME = "totime";
                showDialog(916);

            }
        });




        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "fonts/Quicksand-BoldItalic.otf", true);
        getSupportActionBar().setTitle("Meal Session Create/Update");
        onTaskCompleted_u = new MealTiming.OnTaskCompleted() {
            @Override
            public void onTaskCompleted() {
                println("getting Meal Session list");

                setList(MealSessionCreate.this, Store.getInstance().mealTimingList,keyword,Store.getInstance().dropDownLayout);


            }
        };
        MealTiming.loadData(onTaskCompleted_u);
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub

        if(id==916)
        {

            Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            seconds = c.get(Calendar.SECOND);
            return new TimePickerDialog(this, myTimeListener, hour, minute, true);


        }
        return null;
    }




    private  TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener(){



        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            String time = hour+":"+minute;
            if(FLAG_TIME.compareToIgnoreCase("fromtime")==0)
            {
                fromTime.setText(String.valueOf(time));
                fromTime.setError(null);
                String fromTimeValue = String.valueOf(time);


            }
            else
            {
                toTime.setText(String.valueOf(time));
                toTime.setError(null);
                String toTimeValue = String.valueOf(time);

            }

        }
    };
    private void setList(Context context, final ArrayList<MealTiming> mealTimingList, AutoCompleteTextView keyword, int layout) {
        String[] listViewAdapterContent = new String[mealTimingList.size()];
        for(int i=0;i<mealTimingList.size();i++)
        {
            listViewAdapterContent[i]= mealTimingList.get(i).getName();
        }
        uomAdapter = new ArrayAdapter<>(context, layout, listViewAdapterContent);
        keyword.setAdapter(uomAdapter );

        keyword.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = (String)adapterView.getItemAtPosition(i);
                for(int x=0;x<mealTimingList.size();x++)
                {
                    if(selection.equals(mealTimingList.get(x).getName()))
                    {
                        mealTiming = mealTimingList.get(x);
                        mealTiming_id = mealTimingList.get(x).getId();
                        // Toast.makeText(CreateCategoryActivity.this, "cat Id ---"+cat.getId(), Toast.LENGTH_SHORT).show();
                        setFields(mealTiming);
                    }
                }
            }
        });
    }

    private void setFields(MealTiming mealTiming) {


        name.setText(mealTiming.getName().toString());


                java.util.Date ft = null,tt = null;
                try {
                    ft=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mealTiming.getStartTime());
                    tt  =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mealTiming.getEndTime());
                    mealTiming_id  =mealTiming.getId();
                    mealTimingName = mealTiming.getName();


                } catch (ParseException e) {
                    e.printStackTrace();
                }

                fromTime.setText(String.valueOf(ft.getHours()+":"+ft.getMinutes()));
                toTime.setText(String.valueOf(tt.getHours()+":"+tt.getMinutes()));



    }

    public void setSaveUpdate(View v)
    {

        if(validate())
        {


            println("passed");



            MealTiming.OnTaskCompleted onTaskCompleted = new MealTiming.OnTaskCompleted() {
                @Override
                public void onTaskCompleted() {

                    println("MealTiming saved");

                    Toast.makeText(MealSessionCreate.this, "Saved...", Toast.LENGTH_SHORT).show();
                    MealTiming.loadData(onTaskCompleted_u);


                    clear(null);
                }
            };

            MealTiming.saveUpdate(onTaskCompleted,params);


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

        String endDate =null;
        String startDate = null;



        if(!TextUtils.isEmpty(fromTime.getText()))
        {
            startDate = fromTime.getText().toString();
        }
        else
        {
            fromTime.setError("Invalid Format");
            status = false;
        }

        if(!TextUtils.isEmpty(name.getText()))
        {

        }
        else
        {
            fromTime.setError("Please Fill.. ");
            status = false;
        }
        if(!TextUtils.isEmpty(toTime.getText()))
        {
            endDate = toTime.getText().toString();
        }
        else
        {
            toTime.setError("Invalid Format");
            status = false;
        }

        if(startDate!=null && endDate!=null  && name.getText()!=null)
        {

             params = new HashMap<String, String>();




            if (mealTiming != null) {
                params.put("to", "update");
                params.put("id", String.valueOf(mealTiming.getId()));

            } else {
                params.put("to", "save");

            }

            params.put("startTime", String.valueOf(startDate));
            params.put("endTime", String.valueOf(endDate));
            params.put("name", String.valueOf(name.getText()));
        }
        else
        {
            Toast.makeText(MealSessionCreate.this, "Unable to set time", Toast.LENGTH_SHORT).show();
            status = false;
        }




        return status;
    }

    public void clear(View v)
    {



        mealTiming = null;
        mealTiming_id = Long.valueOf(0);
        mealTimingName = null;

        name.setError(null);
        name.setText("");
        fromTime.setError(null);
        fromTime.setText("");
        toTime.setError(null);
        toTime.setText("");

        keyword.setText("");


    }

}
