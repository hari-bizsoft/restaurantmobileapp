package com.bizsoft.restaurant;

import android.app.Dialog;
import android.app.TimePickerDialog;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bizsoft.restaurant.adapters.CustomSpinnerAdapter;
import com.bizsoft.restaurant.dataobjects.MealTiming;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.service.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class MealTimingActivity extends AppCompatActivity {

    EditText fromTime,toTime,keyword;
    ImageButton fromTimeButton,toTimeButton;
    private String FLAG_TIME;
    private int hour,minute;
    private int seconds;
    Spinner spinner;
    private CustomSpinnerAdapter customSpinnerAdapter;
    private MealTiming mealTiming;
    Button set;
    private Long mealId;
    private String mealTimingName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_timing);
        fromTime= findViewById(R.id.from_time);
        toTime = findViewById(R.id.to_time);

        fromTimeButton= findViewById(R.id.from_time_button);
        toTimeButton = findViewById(R.id.to_time_button);
        spinner = findViewById(R.id.name);
        set = findViewById(R.id.set);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String endDate =null;
                String startDate = null;
                Long id = mealId;
                String name = null;
                
                if(!TextUtils.isEmpty(fromTime.getText()))
                {
                     startDate = fromTime.getText().toString();
                }
                else
                {
                    fromTime.setError("Invalid Format");
                }

                if(!TextUtils.isEmpty(mealTimingName))
                {
                    name = mealTimingName;
                }
                else
                {

                }
                if(!TextUtils.isEmpty(toTime.getText()))
                {
                     endDate = toTime.getText().toString();
                }
                else
                {
                    toTime.setError("Invalid Format");
                }
                
                if(startDate!=null && endDate!=null && mealId!=0 && name!=null)
                {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("id", String.valueOf(mealId));
                    params.put("startTime", String.valueOf(startDate));
                    params.put("endTime", String.valueOf(endDate));
                    params.put("name", String.valueOf(name));
                    new SetTiming(params).execute();
                }
                else
                {
                    Toast.makeText(MealTimingActivity.this, "Unable to set time", Toast.LENGTH_SHORT).show();
                }
                


            }
        });


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


        new MealTimingList(null).execute();
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
    public void setMealSession(final ArrayList<String> strings) {


        // Drop down layout style - list view with radio button

        customSpinnerAdapter = new CustomSpinnerAdapter(MealTimingActivity.this, strings,R.layout.dropdown_style_left);
        spinner.setAdapter(customSpinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 Toast.makeText(getApplicationContext(), strings.get(position), Toast.LENGTH_SHORT).show();

                Iterator iterator = Store.getInstance().mealTimingList.iterator();
                while (iterator.hasNext())
                {
                    mealTiming = (MealTiming) iterator.next();
                    if(mealTiming.getName().equals(strings.get(position)))
                    {
                        java.util.Date ft = null,tt = null;
                        try {
                          ft=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mealTiming.getStartTime());
                          tt  =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(mealTiming.getEndTime());
                          mealId =mealTiming.getId();
                          mealTimingName = mealTiming.getName();


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        fromTime.setText(String.valueOf(ft.getHours()+":"+ft.getMinutes()));
                        toTime.setText(String.valueOf(tt.getHours()+":"+tt.getMinutes()));

                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


                System.out.println("@nothing selected");



            }


        });


    }
    class MealTimingList extends AsyncTask<Void, Void, Boolean>
    {
        HashMap<String,String> params;
        String url;
        String jsonResult;




        public MealTimingList(HashMap<String,String> params) {
            super();
            url = "mealTiming/toList";
            jsonResult = null;
            this.params = params;
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
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();


                Type collectionType = new TypeToken<Collection<MealTiming>>() {
                }.getType();

                Collection<MealTiming> mealTimings = gson.fromJson(jsonResult, collectionType);
                Store.getInstance().mealTimingList = (ArrayList<MealTiming>) mealTimings;
                System.out.println("mealTimings res :"+jsonResult);
                System.out.println("mealTimings size :"+Store.getInstance().mealTimingList.size());



                ArrayList<String> strings = new ArrayList<String>();
                Iterator iterator = Store.getInstance().mealTimingList.iterator();
                while (iterator.hasNext())
                {
                    MealTiming mealTiming = (MealTiming) iterator.next();
                    strings.add(mealTiming.getName());
                }
                setMealSession(strings);







            }
        }


    }
    class SetTiming extends AsyncTask<Void, Void, Boolean>
    {
        HashMap<String,String> params;
        String url;
        String jsonResult;

        public SetTiming(HashMap<String,String> params) {
            super();
            url = "mealTiming/setMealTime";
            jsonResult = null;
            this.params = params;
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

                System.out.println("RES : "+jsonResult);

                if(jsonResult.contains("ok"))
                {
                    Toast.makeText(MealTimingActivity.this, "Updated...", Toast.LENGTH_SHORT).show();
                }

            }
        }


    }
}
