package com.bizsoft.restaurant;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bizsoft.restaurant.adapters.BillAdapter;
import com.bizsoft.restaurant.dataobjects.Bill;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.service.BizUtils;
import com.bizsoft.restaurant.service.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import me.anwarshahriar.calligrapher.Calligrapher;

public class ReportsActivity extends AppCompatActivity {

    EditText fromDate,toDate,fromTime,toTime,keyword;
    ImageButton fromDateButton,toDateButton,fromTimeButton,toTimeButton;
    ListView listView;
    private String FLAG_DATE;
    private int year,month,day;
    private HashMap<String, String> map = new HashMap<String, String>();
    private int hour,minute;
    private String FLAG_TIME;
    Button search;
    ArrayList<EditText> editTexts = new ArrayList<EditText>();
    private BillAdapter billAdapter;
    private ArrayList<Bill> billList;
    TextView grandTotal;
    Button generate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "fonts/Quicksand-BoldItalic.otf", true);

        fromDate = findViewById(R.id.from_date);
        toDate= findViewById(R.id.to_date);
        fromTime= findViewById(R.id.from_time);
        toTime = findViewById(R.id.to_time);


        getSupportActionBar().setTitle("Report");
        fromDateButton = findViewById(R.id.from_date_button);
        toDateButton = findViewById(R.id.to_date_BUTTON);
        fromTimeButton= findViewById(R.id.from_time_button);
        toTimeButton = findViewById(R.id.to_time_button);
        keyword = findViewById(R.id.keyword);
        search = findViewById(R.id.search);
        listView = findViewById(R.id.listview);
        grandTotal = findViewById(R.id.grand_total);
        generate = findViewById(R.id.generate);


        BizUtils.addCustomActionBar(ReportsActivity.this,"Reports");

        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(map!=null)
                {
                    if(map.get("fromdate")!=null && map.get("todate")!=null)
                    {
                        if(Store.getInstance().billList.size()>0) {
                            Boolean status = BizUtils.write_to_report(ReportsActivity.this, "Report " + map.get("fromdate") + " " + map.get("todate"), Store.getInstance().billList1, map);
                            if(status)
                            {
                                Toast.makeText(ReportsActivity.this, "Report generated ..", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ReportsActivity.this, "Report generation failed ..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

            }
        });







        fromDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FLAG_DATE = "fromdate";
                showDialog(999);
            }
        });

        toDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FLAG_DATE = "todate";
                showDialog(999);

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
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editTexts.add(fromDate);
                editTexts.add(toDate);
                editTexts.add(toTime);
                editTexts.add(fromTime);


                int status = checkFields(editTexts);


                if(status == 0)
                {

                    map.put("fromdate",fromDate.getText().toString()+"-"+fromTime.getText().toString()+"-00");
                    map.put("todate",toDate.getText().toString()+"-"+toTime.getText().toString()+"-00");
                    map.put("type","report");
                    new BillList(map).execute();
                }
                else
                {
                    Toast.makeText(ReportsActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }





            }
        });

        keyword.addTextChangedListener(new TextWatcher() {

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


                    search.performClick();

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

                if(billList.size()!=0) {

                    System.out.println("Adding bill list size" + billAdapter.billList.size());
                    billAdapter.billList.clear();
                    billAdapter.billList.addAll(billList);
                    System.out.println("Adding customer list size" + billAdapter.billList.size());

                    billAdapter.notifyDataSetChanged();
                }
            }
        });

    }
    public int checkFields(ArrayList<EditText> editTexts)
    {
        int status = 0;

        for(int i=0;i<editTexts.size();i++) {
            if (TextUtils.isEmpty(editTexts.get(i).getText()))
            {
                editTexts.get(i).setError("Please Fill");

                status ++;
            }
            else
            {
                editTexts.get(i).setError(null);
            }
        }


        return  status;
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        if(id==916)
        {

            Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            day = c.get(Calendar.SECOND);
            return new TimePickerDialog(this, myTimeListener, hour, minute, true);



        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        public String fromDateValue;

        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            String date = (arg2+1)+"-"+arg3+"-"+arg1;
            if(FLAG_DATE.compareToIgnoreCase("fromdate")==0)
            {
                fromDate.setText(String.valueOf(date));
                fromDate.setError(null);
                fromDateValue = String.valueOf(date);


            }
            else
            {
                toDate.setText(String.valueOf(date));
                toDate.setError(null);
                String toDateValue = String.valueOf(date);

            }

        }
    };

    private  TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener(){



        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            String time = hour+"-"+minute;
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



                billAdapter =new BillAdapter(ReportsActivity.this,Store.getInstance().billList);
                listView.setAdapter(billAdapter);

                Store.getInstance().billList1 = (ArrayList<Bill>) gson.fromJson(jsonResult, collectionType);
                grandTotal.setText(String.valueOf(String.format("%.2f",billAdapter.getTotal())));







            }
        }


    }


}
