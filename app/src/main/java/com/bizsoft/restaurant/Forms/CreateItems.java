package com.bizsoft.restaurant.Forms;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.restaurant.R;
import com.bizsoft.restaurant.adapters.CustomSpinnerAdapter;
import com.bizsoft.restaurant.controllers.UOMController;
import com.bizsoft.restaurant.dataobjects.Category;
import com.bizsoft.restaurant.dataobjects.Item;
import com.bizsoft.restaurant.dataobjects.MealTiming;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.dataobjects.UOM;

import com.bizsoft.restaurant.service.BizUtils;
import com.bizsoft.restaurant.service.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import me.anwarshahriar.calligrapher.Calligrapher;

import static com.bizsoft.restaurant.service.BizUtils.println;


public class CreateItems extends AppCompatActivity implements UOMController.OnTaskCompleted {

    AutoCompleteTextView keyword;
    AutoCompleteTextView category;
    AutoCompleteTextView uom;
    Spinner mealSession;

    private ArrayAdapter<String> adapter;
    private Item item;
    TextView name;
    private Category cat;
    private ArrayAdapter<String> catAdaptor;
    private ArrayAdapter<String> Fadapter;

    int layoutItemId = android.R.layout.simple_dropdown_item_1line;
    private Long cat_id= Long.valueOf(0);
    public  static String UOM_LIST= "UOM_LIST_FROM_CREATE_ITEMS";
    private UOM cUOM;
    private Long uom_id= Long.valueOf(0);
    private ArrayAdapter<String> mealAdapter;
    private MealTiming meal;
    private Long meal_id= Long.valueOf(0);
    private CustomSpinnerAdapter customSpinnerAdapter;
    TextView price,quantity;
    Button saveupdate;
    private Long item_id;
    private HashMap<String, String> params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_items);
        keyword = findViewById(R.id.keyword);
        category = findViewById(R.id.category);
        name = findViewById(R.id.name);
        uom = findViewById(R.id.uom);
        mealSession = findViewById(R.id.meal_session);
        price = findViewById(R.id.price);
        quantity = findViewById(R.id.quantity);
        saveupdate = findViewById(R.id.saveupdate);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "fonts/Quicksand-BoldItalic.otf", true);


        getSupportActionBar().setTitle("Items Create/Update");


        BizUtils.addCustomActionBar(CreateItems.this,"Items Create/Update");
        doList();

        setCategory(Store.getInstance().categoryList,category,layoutItemId);

        new ToList(UOM_LIST).execute();

        new MealTimingList(null).execute();











    }

    public void saveupdate(View v)
    {


        if(validate())
        {

            new SaveUpdate().execute();


        }

    }

    private void clear() {

        item =null;
        meal = null;
        cat = null;
        cUOM = null;

        cat_id = Long.valueOf(0);
        meal_id = Long.valueOf(0);
        uom_id  = Long.valueOf(0);
        name.setText("");
        category.setText("");
        uom.setText("");
        price.setText("");
        quantity.setText("");
        mealSession.clearFocus();

        for(int i=0;i<customSpinnerAdapter.getCount();i++)
        {

            String option = (String) customSpinnerAdapter.getItem(i);

            if(option.toLowerCase().equals("none"))
            {
                mealSession.setSelection(i);
            }


        }


        Item.OnTaskCompleted onTaskCompleted = new Item.OnTaskCompleted() {
            @Override
            public void onTaskCompleted() {

                System.out.println("------Item List Save/Update");
                doList();

            }
        };
        Item.loadItems(onTaskCompleted);



    }

    public boolean validate()
    {
        boolean status = true;

        params = new HashMap<String, String>();

        if(item!=null)
        {
            params.put("to","update");
            params.put("id", String.valueOf(item.getId()));

        }
        else
        {
            params.put("to","save");
        }
        if(cat_id!=0) {
            params.put("cat_id", String.valueOf(cat_id));
        }
        else
        {
            status = false;
            category.setError("Please fill..");
        }
        if(uom_id!=0) {
            params.put("uom_id", String.valueOf(uom_id));
        }
        else
        {
            status = false;
            uom.setError("Please fill..");
        }
        if(meal_id!=0 && meal!=null ) {
            params.put("meal_id", String.valueOf(meal_id));
        }
        else
        {
            status = false;
            Toast.makeText(this, "Choose meal session", Toast.LENGTH_SHORT).show();

        }
        if(!TextUtils.isEmpty( quantity.getText())) {
            params.put("quantity", String.valueOf(quantity.getText().toString()));
        }
        else
        {
            status = false;
            quantity.setError("Please fill..");
        }
        if(!TextUtils.isEmpty( price.getText())) {
            params.put("price", String.valueOf(price.getText().toString()));
        }
        else
        {
            status = false;
            price.setError("Please fill..");
        }
        if(!TextUtils.isEmpty( name.getText())) {
            params.put("name", String.valueOf(name.getText().toString()));
        }
        else
        {
            status = false;
            name.setError("Please fill..");
        }





        return  status;
    }

    public void doList()
    {
        String[] listViewAdapterContent = new String[Store.getInstance().itemList.size()];
        for(int i=0;i<Store.getInstance().itemList.size();i++)
        {
            listViewAdapterContent[i]=Store.getInstance().itemList.get(i).getName();
            println("----->"+Store.getInstance().itemList.get(i));
        }

        adapter = new ArrayAdapter<>(CreateItems.this, layoutItemId, listViewAdapterContent);
        keyword.setAdapter(adapter);
        keyword.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String selection = (String)adapterView.getItemAtPosition(i);

                for(int x=0;x<Store.getInstance().itemList.size();x++)
                {
                    if(selection.equals(Store.getInstance().itemList.get(x).getName()))
                    {
                         item = Store.getInstance().itemList.get(x);


                         setFiels(item);

                        //do
                        Toast.makeText(CreateItems.this, "Item Id ---", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }
    public void setFiels(Item item)
    {
        name.setText(String.valueOf(item.getName()));

        for(int i=0;i<Store.getInstance().categoryList.size();i++)
        {
            if(Store.getInstance().categoryList.get(i).getId()== item.getCat_id()) {
                category.setText(String.valueOf(Store.getInstance().categoryList.get(i).getName()));
                cat_id = Store.getInstance().categoryList.get(i).getId();
            }
        }
        for(int i=0;i<Store.getInstance().uomList.size();i++)
        {
            System.out.println(Store.getInstance().uomList.get(i).getName()+":==========:"+item.getUom()+"=="+Store.getInstance().uomList.get(i).getName().trim().equals(item.getUom().trim()));
            if(Store.getInstance().uomList.get(i).getName().trim().equals(item.getUom().trim())) {
                uom.setText(String.valueOf(Store.getInstance().uomList.get(i).getName()));
                uom_id = Store.getInstance().uomList.get(i).getId();
                uom_id = Store.getInstance().uomList.get(i).getId();
            }
        }
        for(int i=0;i<Store.getInstance().mealTimingList.size();i++)
        {
            System.out.println(Store.getInstance().mealTimingList.get(i).getName()+":==========:"+item.getMealTiming().getName()+"=="+Store.getInstance().uomList.get(i).getName().trim().equals(item.getMealTiming().getName().trim()));
            if(Store.getInstance().mealTimingList.get(i).getName().trim().equals(item.getMealTiming().getName().trim())) {
                mealSession.setSelection(i);
                meal_id = Store.getInstance().mealTimingList.get(i).getId();
            }
        }

        quantity.setText(String.valueOf(item.getQuantity()));

        price.setText(String.valueOf(item.getPriceRef()));




    }

    public void setCategory(final ArrayList<Category> category, AutoCompleteTextView keyword,int layout)
    {
        String[] listViewAdapterContent = new String[category.size()];
        for(int i=0;i<category.size();i++)
        {
            listViewAdapterContent[i]=category.get(i).getName();
        }
        catAdaptor = new ArrayAdapter<>(CreateItems.this, layout, listViewAdapterContent);
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
                        Toast.makeText(CreateItems.this, "cat Id ---"+cat.getId(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    //-------------UOM----
    public void setUOM(final ArrayList<UOM> uoms, AutoCompleteTextView keyword,int layout)
    {
        String[] listViewAdapterContent = new String[uoms.size()];
        System.out.println("-----"+uoms.size());
        for(int i=0;i<uoms.size();i++)
        {
            System.out.println("-----"+uoms.get(i).getName());
            listViewAdapterContent[i]=uoms.get(i).getName();
        }
        Fadapter = new ArrayAdapter<>(CreateItems.this, layout, listViewAdapterContent);
        keyword.setAdapter(Fadapter);
        keyword.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = (String)adapterView.getItemAtPosition(i);
                for(int x=0;x<uoms.size();x++)
                {
                    if(selection.equals(uoms.get(x).getName()))
                    {
                        cUOM = uoms.get(x);
                        uom_id = uoms.get(x).getId();
                        Toast.makeText(CreateItems.this, "uom_id  ---"+cUOM.getId(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onTaskCompleted(String action) {


        System.out.println("---->"+action);


    }
    public  class ToList extends AsyncTask
    {
        private final String action;
        String url;
        private String jsonResult;


        CreateItems onTaskCompleted;


        public ToList(String action) {
            this.url = "UOM/toList";
            this.jsonResult = null;
            this.onTaskCompleted = new CreateItems() ;
            this.action = action;




        }

        @Override
        protected Object doInBackground(Object[] objects) {
            jsonResult = HttpHandler.makeServiceCall(this.url,null);

            return true;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if(jsonResult!=null)
            {
                GsonBuilder gsonBuilder = new GsonBuilder();

                Gson gson = gsonBuilder.create();
                Type collectionType = new TypeToken<Collection<UOM>>() { }.getType();


                Collection<UOM> uoms = gson.fromJson(jsonResult, collectionType);




                System.out.println("uom res :"+jsonResult);
                System.out.println("uom res :"+Store.getInstance().uomList.size());
                Store.getInstance().uomList = (ArrayList<UOM>) uoms;
                for(int i=0;i<Store.getInstance().uomList.size();i++)
                {
                    System.out.println("====="+Store.getInstance().uomList.get(i));
                }


                setUOM(Store.getInstance().uomList,uom,layoutItemId);


            }
        }

    }
    //--------------------Meal Session
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
    public void setMealSession(final ArrayList<String> strings) {
        strings.add("None");
        // Drop down layout style - list view with radio button
        customSpinnerAdapter = new CustomSpinnerAdapter(CreateItems.this, strings,R.layout.dropdown_style_left);
        mealSession.setAdapter(customSpinnerAdapter);
        mealSession.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public MealTiming mealTiming;

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
                            meal_id =mealTiming.getId();
                            meal = mealTiming;


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }



                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


                System.out.println("@nothing selected");



            }






        });


    }


    public  class SaveUpdate extends  AsyncTask
    {

        String url;
        String result;

        public SaveUpdate() {

            this.url = "item/saveUpdate";
            this.result = null;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
             result = HttpHandler.makeServiceCall(this.url, params);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if(result!=null)
            {

                println(result);

                clear();
            }
        }
    }
}
