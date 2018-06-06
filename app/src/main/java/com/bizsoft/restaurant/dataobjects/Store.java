package com.bizsoft.restaurant.dataobjects;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.widget.TextView;

import com.bizsoft.restaurant.adapters.AddedItemListAdapter;
import com.bizsoft.restaurant.service.AssetPropertyReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by shri on 8/8/17.
 */

public class Store  extends LiveData{

    private static Store instance =null;
    public ArrayList<Category> categoryList = new ArrayList<Category>();
    public long currentResId;
    public String currentResImage;
    public Restaurant restaurant;
    public Restaurant currentRestaurant;
    public Restaurant currentResInfo;
    public Long currentCatId;
    public  ArrayList<Item> itemList = new ArrayList<Item>();
    public Long currentItemId;
    public  ArrayList<Item> addedItemList = new ArrayList<Item>();
    public AddedItemListAdapter addedItemListAdapter;
    public TextView chooesedItem;
    public float actualGT;
    public TextView reprintSpinnerText;
    public ArrayList<Bill> billList = new ArrayList<Bill>();
    public Bill currentShowBill;
    public ArrayList<Bill> billList1= new ArrayList<Bill>();
    public User user = new User();
    public Bill lastbill;
    public ArrayList<Table> tableList = new ArrayList<Table>();
    public Long currentTableId = Long.valueOf(0);
    public String currentTableName;
    public Bill currentTableBill = new Bill();
    private AssetPropertyReader assetsPropertyReader;
    private Properties property;
    public ArrayList<KOTChannel> kotChannels = new ArrayList<KOTChannel>();
    public Long currentBillId;
    public Bill currentBill;
    public ArrayList<MealTiming> mealTimingList = new ArrayList<>();
    public ArrayList<UOM> uomList = new ArrayList<UOM>();
    private SharedPreferences confPreferences ;
    public int dropDownLayout =  android.R.layout.simple_dropdown_item_1line;;


    private MutableLiveData<List<Item>> data =
            new MutableLiveData<List<Item>>();
    public ArrayList<User> userList = new ArrayList<User>();
    public Configuration configuration = new Configuration();
    public Theme theme = new Theme();
    public String confIsStored = "confStore";
    public String confId = "confId";

    public static Store getInstance() {

        if(instance==null)
        {
            instance = new Store();

        }


        return instance;
    }
    public String domain = "192.168.1.160:8080";
    public String baseUrl = "http://"+domain+"/bizres/";

    //Tables


    public ArrayList<Restaurant> restaurantList = new ArrayList<Restaurant>();


    public Properties getProperty(Context context)
    {
        assetsPropertyReader = new AssetPropertyReader(context);
        property = assetsPropertyReader.getProperties("BizProp.properties");

        return property;
    }
    public SharedPreferences getConfPref(Context context)
    {
        confPreferences = context.getSharedPreferences("confPref", MODE_PRIVATE);

        return confPreferences;

    }



}
