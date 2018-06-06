package com.bizsoft.restaurant;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.restaurant.adapters.AddedItemListAdapter;
import com.bizsoft.restaurant.adapters.BillAdapter;
import com.bizsoft.restaurant.adapters.BillListAdapter;
import com.bizsoft.restaurant.adapters.CategoryListAdapter;
import com.bizsoft.restaurant.adapters.CustomSpinnerAdapter;
import com.bizsoft.restaurant.adapters.ItemListAdapter;
import com.bizsoft.restaurant.dataobjects.Bill;
import com.bizsoft.restaurant.dataobjects.Category;
import com.bizsoft.restaurant.dataobjects.Item;
import com.bizsoft.restaurant.dataobjects.MealTiming;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.service.AssetPropertyReader;
import com.bizsoft.restaurant.service.BizUtils;

import com.bizsoft.restaurant.service.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import me.anwarshahriar.calligrapher.Calligrapher;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.bizsoft.restaurant.service.BizUtils.println;


public class DashboardActivity extends AppCompatActivity {

    AutoCompleteTextView categorySearchBox;
    EditText itemSearchBox;
    public  TextView itemCount;
    public static TextView choosedItem;
    ListView listView;
    ArrayAdapter<String> listAdapter;
    String [] listViewAdapterContent ;
    String [] listViewAdapterItemContent ;
    ArrayAdapter<String> adapter;
    ImageButton itemSearch;
    private AddedItemListAdapter addedItemListAdapter;
    public static  TextView subTotal;
    public static TextView gst;
    public static TextView grandTotal;
    public static  TextView balance,discountValue;
    public static  EditText discount;
    public static EditText fromCustomer;
    TextView tableList;
    String tableValue;
    Long tableId;
    TextView tableLabel;
    ImageButton clear;
    Button save,hold;
    Spinner paymentModeSpinner;
    public static String paymentModeValue;
    TextView frmLabel,balanceLabel;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private static final int REQUEST_PERMISSION_SETTING1 = 102;

    final ArrayList<String> genderList = new ArrayList<String>();

    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    ImageButton clearTable;
    String ACTION = "SAVE";
    private HashMap<String, String> params;
    TextView closeBill;
    FloatingActionButton bills;
    private AssetPropertyReader assetsPropertyReader;
    private Properties property;
    private CustomSpinnerAdapter customSpinnerAdapter;
    private BillListAdapter billListAdapter;
    public static ListView billListView;
    public static Dialog billListDialog;
    private TextView title;
    private ImageButton categorySearchButton;
    TextView categoryLabel;
    TextView choosedCatLabel;
    TextView itemLabel;
    TextView orderedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_dashboard);
        //Applying font
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "fonts/Quicksand-BoldItalic.otf", true);

        genderList.add("Cash");
        genderList.add("Card");


        params  = new HashMap<String, String>();


        listView = (ListView) findViewById(R.id.listView);
        addedItemListAdapter  = new AddedItemListAdapter(DashboardActivity.this,Store.getInstance().addedItemList);
        listView.setAdapter(addedItemListAdapter);

        Store.getInstance().addedItemListAdapter =  addedItemListAdapter;

       // Store.getInstance().addedItemList.clear();
        //Declaring identifiers

        categorySearchBox = (AutoCompleteTextView) findViewById(R.id.cat_keyword);
        itemSearchBox = (EditText) findViewById(R.id.items_keyword);
        itemCount = (TextView) findViewById(R.id.name);
        choosedItem = (TextView) findViewById(R.id.choosed_item);
        itemSearch = (ImageButton) findViewById(R.id.item_search);
        clear = (ImageButton) findViewById(R.id.clear);
        save = (Button) findViewById(R.id.save);
        hold = (Button) findViewById(R.id.hold);
        paymentModeSpinner = (Spinner) findViewById(R.id.payment_mode_spinner);
        frmLabel = (TextView) findViewById(R.id.rfc_label);
        balanceLabel= (TextView) findViewById(R.id.balance_label);
        clearTable = (ImageButton) findViewById(R.id.clear_table);
        closeBill = (TextView) findViewById(R.id.close_bill);
        categorySearchButton = (ImageButton) findViewById(R.id.search_category);
        categoryLabel = (TextView) findViewById(R.id.category_label);
        choosedCatLabel = (TextView) findViewById(R.id.choosed_cat_label);
        itemLabel = (TextView) findViewById(R.id.item_label);
        orderedItems = (TextView) findViewById(R.id.ordered_items);


        subTotal = (TextView) findViewById(R.id.sub_total);
        gst = (TextView) findViewById(R.id.gst);
        grandTotal = (TextView) findViewById(R.id.grand_total);

        balance = (TextView) findViewById(R.id.balance);

        categorySearchBox.setText(String.valueOf("All"));
        discount = (EditText) findViewById(R.id.discount);
        fromCustomer = (EditText) findViewById(R.id.from_customer);
        discountValue = (TextView) findViewById(R.id.textView20);
        tableLabel = (TextView) findViewById(R.id.choose_table_label);
        tableList = (TextView) findViewById(R.id.choosed_table);





        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);




        View view =getSupportActionBar().getCustomView();
        title = view.findViewById(R.id.title);
        bills =  view.findViewById(R.id.bills);
        assetsPropertyReader = new AssetPropertyReader(DashboardActivity.this);
        property = assetsPropertyReader.getProperties("BizProp.properties");
        System.out.println("====Tab iD===="+Store.getInstance().currentTableId);

        setPayMode();


        if(Store.getInstance().currentTableId!=null)
        {

            System.out.println("---<><><><><><>---"+Store.getInstance().currentTableName+"----------"+Store.getInstance().currentTableId);
            tableList.setText(String.valueOf(Store.getInstance().currentTableName));
            if(Store.getInstance().currentTableId==0)
            {
                tableList.setText(String.valueOf("None"));
            }

            hold.setVisibility(View.VISIBLE);
            closeBill.setVisibility(View.VISIBLE);

            System.out.println("----------------"+Store.getInstance().currentTableBill);
            if(Store.getInstance().currentTableBill!=null) {
                title.setText("Bill Id :"+String.valueOf(Store.getInstance().currentTableBill.getId()));


                if(Store.getInstance().currentTableBill.getPaymentMode()!=null) {

                    int pos = 0;
                    for (int i = 0; i < genderList.size(); i++) {

                        if (genderList.get(i).toLowerCase().contains(Store.getInstance().currentTableBill.getPaymentMode())) {
                            pos = i;
                        }
                    }
                    paymentModeSpinner.setSelection(pos);

                    System.out.println("_---------"+Store.getInstance().currentTableBill.getPaymentMode());
                        if(Store.getInstance().currentTableBill.getPaymentMode().toLowerCase().contains("card"))
                        {
                            System.out.println("---------setting card mode");
                            balance.setVisibility(View.GONE);
                            fromCustomer.setVisibility(View.GONE);
                            frmLabel.setVisibility(View.GONE);
                            balanceLabel.setVisibility(View.GONE);
                        }
                        else
                        {
                            System.out.println("_---------setting cash mode");
                            paymentModeSpinner.setId(0);
                            balance.setVisibility(View.VISIBLE);
                            fromCustomer.setVisibility(View.VISIBLE);
                            frmLabel.setVisibility(View.VISIBLE);
                            balanceLabel.setVisibility(View.VISIBLE);
                            discountValue.setText(String.valueOf(Store.getInstance().currentTableBill.getDiscountValue()));
                            discount.setText(String.valueOf(Store.getInstance().currentTableBill.getDiscountPercentage()));
                            fromCustomer.setText(String.valueOf(Store.getInstance().currentTableBill.getReceivedFromCustomer()));
                            balance.setText(String.valueOf(Store.getInstance().currentTableBill.getBalance()));
                        }

                }
            }

            System.out.println("--------"+Store.getInstance().addedItemListAdapter.itemList.size());
            Store.getInstance().addedItemListAdapter.notifyDataSetChanged();
            new ItemList(0).execute();
            save.setVisibility(View.GONE);
            closeBill.setVisibility(View.VISIBLE);
        }
        else if ( Store.getInstance().currentBill!=null)
        {
            hold.setVisibility(View.VISIBLE);
            closeBill.setVisibility(View.GONE);


            if(Store.getInstance().currentBill!=null) {

                System.out.println("BILL ID---------"+Store.getInstance().currentBill.getId());
                title.setText("Bill ID :"+String.valueOf(Store.getInstance().currentBill.getId()));
                if(Store.getInstance().currentBill.getPaymentMode()!=null) {



                    System.out.println("_---------"+Store.getInstance().currentBill.getPaymentMode());
                    if(Store.getInstance().currentBill.getPaymentMode().toLowerCase().contains("card"))
                    {
                        System.out.println("---------setting card mode");
                        balance.setVisibility(View.GONE);
                        fromCustomer.setVisibility(View.GONE);
                        frmLabel.setVisibility(View.GONE);
                        balanceLabel.setVisibility(View.GONE);
                    }
                    else
                    {
                        System.out.println("_---------setting cash mode");
                        paymentModeSpinner.setId(0);
                        balance.setVisibility(View.VISIBLE);
                        fromCustomer.setVisibility(View.VISIBLE);
                        frmLabel.setVisibility(View.VISIBLE);
                        balanceLabel.setVisibility(View.VISIBLE);
                        discountValue.setText(String.valueOf(Store.getInstance().currentBill.getDiscountValue()));
                        discount.setText(String.valueOf(Store.getInstance().currentBill.getDiscountPercentage()));
                        fromCustomer.setText(String.valueOf(Store.getInstance().currentBill.getReceivedFromCustomer()));
                        balance.setText(String.valueOf(Store.getInstance().currentBill.getBalance()));
                    }

                }
            }

            System.out.println("--------"+Store.getInstance().addedItemListAdapter.itemList.size());
            Store.getInstance().addedItemListAdapter.notifyDataSetChanged();
            new ItemList(0).execute();
            save.setVisibility(View.VISIBLE);
            closeBill.setVisibility(View.GONE);
        }
        else {
            new ItemList(0).execute();
           // hold.setVisibility(View.GONE);
            save.setVisibility(View.VISIBLE);
            closeBill.setVisibility(View.GONE);

            clear();
        }


        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (permissionStatus.getBoolean("ACCESS_FINE_LOCATION", false)) {
               getPermission();
            }
            getPermission();

        }



        bills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showBillListDialog(DashboardActivity.this);

            }
        });

        System.out.println("Table Bill --------------"+Store.getInstance().currentTableBill);
        if(Store.getInstance().currentTableBill!=null)
        {


            if(Store.getInstance().currentTableBill.getId()!=null)
            {
                title.setText("Bill ID : "+Store.getInstance().currentTableBill.getId());
            }
            else
            {
                title.setText("Bill ID: yet to genarate");
            }
            System.out.println("Table Bill Id --------------"+Store.getInstance().currentTableBill.getId());
        }
        else
        if(Store.getInstance().currentBill!=null)
        {
            if(Store.getInstance().currentBill.getId()!=null)
            {
                title.setText("Bill ID : "+Store.getInstance().currentBill.getId());
            }
            else
            {
                title.setText("Bill ID: yet to genarate");
            }
            System.out.println("Bill Id --------------"+Store.getInstance().currentBill.getId());
        }
        else {
            System.out.println("Bill Id --------------None");
            title.setText("Bill ID: yet to genarate");
        }


        ImageButton imageButton= (ImageButton)view.findViewById(R.id.menu);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BizUtils.showMenu(DashboardActivity.this);

            }
        });



        clearTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tableList.setText(String.valueOf("None"));


                Store.getInstance().currentTableName = "None";
                Store.getInstance().currentBill = null;
                Store.getInstance().currentBillId = null;
                Store.getInstance().currentTableBill = null;
                Store.getInstance().currentTableId = null;



                Store.getInstance().addedItemListAdapter.itemList.clear();
                Store.getInstance().addedItemListAdapter.notifyDataSetChanged();
                title.setText("Bill ID yet to generate");
                discountValue.setText(String.valueOf("0.00"));
                discount.setText(String.valueOf("0.00"));
                fromCustomer.setText(String.valueOf("0.00"));
                balance.setText(String.valueOf("0.00"));

                //title.setText("Yet to generate");

              //  hold.setVisibility(View.GONE);
                closeBill.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);


            }
        });


        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ItemList(0).execute();
                categorySearchBox.setText(String.valueOf("All"));


            }
        });
        categorySearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                openCategoryDialog();
            }
        });

        hold.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {


                System.out.println("Table bill ------------------------");
                System.out.println("Item Count  ------------------------"+ Store.getInstance().addedItemListAdapter.getCount());
                System.out.println("Table bill ------------------------"+ Store.getInstance().currentTableBill);
                // Existing table bill
                if(Store.getInstance().currentTableBill!=null && Store.getInstance().addedItemListAdapter.getCount()>0) {

                    System.out.println("Table bill ------------------------");
                        if (Store.getInstance().currentTableBill != null) {
                            params.put("ACTION", "update");
                            System.out.println("CTab Bill Id"+Store.getInstance().currentTableBill.getId());
                            if(Store.getInstance().currentTableBill.getId()!=null) {
                                params.put("billid", String.valueOf(Store.getInstance().currentTableBill.getId()));
                            }
                        } else {
                            params.put("ACTION", "save");

                        }


                        params.put("status", "Open");

                        putParams();
                        new SaveBill(params).execute();

                }
                //existing OD bill
                else if (Store.getInstance().currentBill != null && Store.getInstance().addedItemListAdapter.getCount()>0)
                {
                    System.out.println("OD bill from list  ------------------------");
                    System.out.println("OD BILL"+Store.getInstance().currentBill.getId());
                    if (Store.getInstance().currentBill.getId() != null) {
                        System.out.println("UNSAVED BILL"+Store.getInstance().currentBill.getId());
                        params.put("ACTION", "update");
                        if(Store.getInstance().currentBill.getId()!=null) {
                            params.put("billid", String.valueOf(Store.getInstance().currentBill.getId()));
                        }
                    }

                    else {
                        params.put("ACTION", "save");

                    }


                    params.put("type", "odbill");
                    params.put("status", "Open");

                    putParams();
                     new SaveBill(params).execute();

                }
                // new table bill
                else
                if(Store.getInstance().currentTableId!=null)
                {
                params.put("ACTION", "save");
                params.put("status", "Open");
                    params.put("table", String.valueOf(Store.getInstance().currentTableId));
                putParams();
                new SaveBill(params).execute();
                }
                // new OD bill
                else if(Store.getInstance().addedItemListAdapter.getCount() >0)
                {
                    System.out.println("fresh bill ------------------------");

                    params.put("ACTION", "save");
                    params.put("type", "odbill");
                    params.put("status", "Open");

                    putParams();
                    new SaveBill(params).execute();
                }
                else
                {
                    Toast.makeText(DashboardActivity.this, "No Item to save...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        closeBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Store.getInstance().addedItemListAdapter.getCount()>0) {

                    ArrayList<Item> items = Store.getInstance().addedItemListAdapter.itemList;
                    System.out.println("Size  ==== "+Store.getInstance().addedItemListAdapter.itemList.size());

                    int notOkay = 0;
                    for(int i=0;i<items.size();i++)
                    {
                        if(items.get(i).kot!=null)
                        {

                            System.out.println("KOT  ==== "+items.get(i).kot+"========"+items.get(i).kot.status);
                            if(items.get(i).kot.status!=null) {
                                System.out.println("Status  ==== "+items.get(i).getKot().getStatus().toLowerCase());
                                if (!items.get(i).getKot().getStatus().toLowerCase().contains("served")) {
                                    notOkay++;
                                }
                            }
                            else
                            {
                                notOkay++;
                            }
                        }
                    }

                    System.out.println("Not okay ==== "+notOkay);
                    if(notOkay==0)
                    {
                        if(paymentModeValue.toLowerCase().contains("cash")) {

                            float gt = Float.parseFloat(grandTotal.getText().toString());
                            float fc = Float.parseFloat(fromCustomer.getText().toString());
                            if (fc >= gt) {



                                if (Store.getInstance().currentTableBill != null) {
                                    if (Store.getInstance().currentTableBill.getId() != null) {

                                        System.out.println("Current bill id ----not null" + Store.getInstance().currentTableBill.getId());


                                        if(Store.getInstance().currentTableBill.getId()!=null) {
                                            params.put("billid", String.valueOf(Store.getInstance().currentTableBill.getId()));
                                        }
                                    }
                                }


                                params.put("ACTION", "close");
                                params.put("status", "Closed");

                                putParams();
                                new SaveBill(params).execute();

                            } else {
                                Toast.makeText(DashboardActivity.this, "Please collect cash from customer", Toast.LENGTH_SHORT).show();
                            }

                        }
                        else
                        {

                            if (Store.getInstance().currentTableBill != null) {
                                if (Store.getInstance().currentTableBill.getId() != null) {

                                    System.out.println("Current bill id ----not null" + Store.getInstance().currentTableBill.getId());

                                    if(Store.getInstance().currentTableBill.getId()!=null) {
                                        params.put("billid", String.valueOf(Store.getInstance().currentTableBill.getId()));
                                    }
                                }
                            }


                            params.put("ACTION", "close");
                            params.put("status", "Closed");

                            putParams();
                            new SaveBill(params).execute();
                        }
                    }
                    else
                    {
                        Toast.makeText(DashboardActivity.this, "All items not served", Toast.LENGTH_SHORT).show();
                    }




                }
                else
                {
                    Toast.makeText(DashboardActivity.this, "Please add items", Toast.LENGTH_SHORT).show();
                }

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                if(Store.getInstance().currentBill!=null)

                {
                    params.put("ACTION","close");

                    if(Store.getInstance().currentBill.getId()!=null) {
                        params.put("billid", String.valueOf(Store.getInstance().currentBill.getId()));
                    }
                }
                else
                {
                    params.put("ACTION","save");
                }



                System.out.println("List Size ----"+Store.getInstance().addedItemListAdapter.getCount());

                if(Store.getInstance().addedItemListAdapter.getCount()>0)
                {


                    ArrayList<Item> items = Store.getInstance().addedItemListAdapter.itemList;
                    System.out.println("Size  ==== "+Store.getInstance().addedItemListAdapter.itemList.size());

                    int notOkay = 0;
                    for(int i=0;i<items.size();i++)
                    {
                        if(items.get(i).kot!=null)
                        {

                            System.out.println("KOT  ==== "+items.get(i).kot+"========"+items.get(i).kot.status);
                            if(items.get(i).kot.status!=null) {
                                System.out.println("Status  ==== "+items.get(i).getKot().getStatus().toLowerCase());
                                if (!items.get(i).getKot().getStatus().toLowerCase().contains("served")) {
                                    notOkay++;
                                }
                            }
                            else
                            {
                                notOkay++;
                            }
                        }
                    }

                    System.out.println("Not okay ==== "+notOkay);

                    if(notOkay==0)
                    {




                        putParams();
                        float gt = Float.parseFloat(grandTotal.getText().toString());
                        float fc = Float.parseFloat(fromCustomer.getText().toString());


                        if(paymentModeValue.toLowerCase().contains("cash")) {
                            if (fc >= gt) {
                                params.put("status","Closed");
                                new SaveBill(params).execute();
                            } else {
                                Toast.makeText(DashboardActivity.this, "Please collect cash from customer", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            params.put("status","Closed");
                            new SaveBill(params).execute();
                        }
                    }
                    else
                    {
                        Toast.makeText(DashboardActivity.this, "All items not served", Toast.LENGTH_SHORT).show();
                    }


                  


                }
                else
                {
                    Toast.makeText(DashboardActivity.this, "Please add items", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //
        Store.getInstance().chooesedItem =  choosedItem;

        //
        new CatList(2).execute();

        categorySearchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public int item_size;
            public Long cat_id;

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selection = (String)adapterView.getItemAtPosition(i);

                for(int x=0;x<Store.getInstance().categoryList.size();x++)
                {
                    if(selection.equals(Store.getInstance().categoryList.get(x).getName()))
                    {
                        cat_id = Store.getInstance().categoryList.get(x).getId();
                        item_size = Store.getInstance().categoryList.get(x).getItemSize();
                    }

                }

                Store.getInstance().currentCatId = cat_id;
                //Toast.makeText(DashboardActivity.this, "pos = "+cat_id, Toast.LENGTH_SHORT).show();

                new ItemList(cat_id).execute();


                itemCount.setText(String.valueOf(item_size)+" "+Store.getInstance().configuration.getItemLabel());


            }
        });





        itemSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(itemSearchBox.getText()!=null )
                {
                    String key = itemSearchBox.getText().toString();
                    System.out.println("Search keyword ----"+itemSearchBox.getText().toString());

                    openDialogForItem(key);
                }
            }
        });

        fromCustomer.addTextChangedListener(new TextWatcher() {

            float fc =0;
            String frmCus;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                frmCus = charSequence.toString();
                if(!TextUtils.isEmpty(frmCus))
                {
                    fc = Float.parseFloat(frmCus);

                }
                if(fc >= Float.parseFloat(grandTotal.getText().toString()) )
                {
                    System.out.println("Grtr");
                    float tc = fc - Float.parseFloat(grandTotal.getText().toString());

                    balance.setText(String.valueOf(String.format("%.2f",tc)));
                    fromCustomer.setTextColor(Color.GREEN);
                }

                else
                {
                    fromCustomer.setTextColor(Color.RED);
                    balance.setText(String.valueOf(String.format("%.2f",0.00)));
                    Toast.makeText(DashboardActivity.this, "Amount not enough", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {



            }
        });



        discount.addTextChangedListener(new TextWatcher() {

            String discountStrng;
            float dis = 0;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            addedItemListAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();


        tableLabel.setText("Choose "+Store.getInstance().configuration.getTableLabel());
        itemCount.setText(Store.getInstance().itemList.size()+" "+Store.getInstance().configuration.getItemLabel());
        categoryLabel.setText(String.valueOf(Store.getInstance().configuration.getCategoryLabel()));
        categoryLabel.setHint("Search "+Store.getInstance().configuration.getCategoryLabel());
        itemSearchBox.setHint("Search "+Store.getInstance().configuration.getItemLabel());
        choosedCatLabel.setText(String.valueOf("Choosed "+Store.getInstance().configuration.getCategoryLabel()+" Has "));
        itemLabel.setText(String.valueOf(Store.getInstance().configuration.getItemLabel()));
        choosedItem.setText(String.valueOf("0 "+Store.getInstance().configuration.getItemLabel()));
        orderedItems.setText("Ordered "+Store.getInstance().configuration.getItemLabel());
    }

    private void openCategoryDialog() {

        final ListView categoryList;
        Button close;
        final Dialog dialog = new Dialog(DashboardActivity.this);
        dialog.setTitle("Category List");
        dialog.setContentView(R.layout.category_dialog);
        categoryList = dialog.findViewById(R.id.listview);
        close = dialog.findViewById(R.id.close);
        categoryList.setAdapter(new CategoryListAdapter(DashboardActivity.this, Store.getInstance().categoryList));
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int item_size = 0;



                Long  cat_id =  categoryList.getAdapter().getItemId(i);
                item_size = Store.getInstance().categoryList.get(i).getItemSize();


                          Store.getInstance().currentCatId = cat_id;
                //Toast.makeText(DashboardActivity.this, "pos = "+cat_id, Toast.LENGTH_SHORT).show();

                new ItemList(cat_id).execute();
                itemCount.setText(String.valueOf(item_size) +" "+Store.getInstance().configuration.getItemLabel());
                dialog.dismiss();



            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showBillListDialog(Context context) {


         billListDialog =  new Dialog(context);
        billListDialog.setContentView(R.layout.bill_list);
        billListView = billListDialog.findViewById(R.id.listview);


        final HashMap<String, String> hm = new HashMap<String, String>();

        hm.put("waiterId", String.valueOf(Store.getInstance().user.getId()));
        new BillList(hm).execute();
        billListDialog.show();


    }

    public void putParams()
    {

        Gson gson = new Gson();
        Type type = new TypeToken<List<Item>>() {}.getType();
        String json = gson.toJson(Store.getInstance().addedItemListAdapter.itemList, type);
        System.out.println(json);

        params.put("itemList",json);
        params.put("subTotal",subTotal.getText().toString());
        params.put("gst",gst.getText().toString());
        params.put("grandTotal",grandTotal.getText().toString());
        params.put("paymentMode",paymentModeValue);

       if(TextUtils.isEmpty(discountValue.getText()))
        {
            params.put("discountValue","0");
        }
        else
        {
            params.put("discountValue",discountValue.getText().toString());
        }
        if(TextUtils.isEmpty(discount.getText()))
        {
            params.put("discountPercentage","0");
        }
        else
        {
            params.put("discountPercentage",discount.getText().toString());
        }


        if(TextUtils.isEmpty(fromCustomer.getText()))
        {
            params.put("fromCustomer","0");
        }
        else
        {
            params.put("fromCustomer",fromCustomer.getText().toString());
        }
        if(TextUtils.isEmpty(balance.getText()))
        {
            params.put("balance","0");
        }
        else
        {
            params.put("balance",balance.getText().toString());
        }
    }
    public void getPermission() {

        if (ActivityCompat.checkSelfPermission(DashboardActivity.this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardActivity.this, WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(WRITE_EXTERNAL_STORAGE, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }


            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(WRITE_EXTERNAL_STORAGE, true);
            editor.commit();


        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission(EXTERNAL_STORAGE_PERMISSION_CONSTANT);
        }


    }

    private void proceedAfterPermission(int v) {
        //We've got the permission, now we can proceed further
        if (v == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {

        }
    }


    public void setPayMode() {


        // Drop down layout style - list view with radio button
         customSpinnerAdapter = new CustomSpinnerAdapter(DashboardActivity.this, genderList,R.layout.dropdown_style);
        paymentModeSpinner.setAdapter(customSpinnerAdapter);

        paymentModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  Toast.makeText(getApplicationContext(), genderList.get(position), Toast.LENGTH_SHORT).show();
                paymentModeValue = genderList.get(position);
                if(paymentModeValue.toLowerCase().contains("card"))
                {
                    balance.setVisibility(View.GONE);
                    fromCustomer.setVisibility(View.GONE);
                    frmLabel.setVisibility(View.GONE);
                    balanceLabel.setVisibility(View.GONE);
                }
                else
                {
                    balance.setVisibility(View.VISIBLE);
                    fromCustomer.setVisibility(View.VISIBLE);
                    frmLabel.setVisibility(View.VISIBLE);
                    balanceLabel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                paymentModeValue = genderList.get(0);




            }


        });


    }

    public void openDialogForItem(String key)
    {
        final Dialog dialog = new Dialog(DashboardActivity.this);
        ListView listView ;
        Button add;
        ItemListAdapter itemListAdapter;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_list);
        listView = dialog.findViewById(R.id.listview);
        add =  dialog.findViewById(R.id.add);


        // -----Filtering based on time and keyword--------------------

        ArrayList<Item> filteredList =  new ArrayList<Item>();
        Iterator iterator = Store.getInstance().itemList.iterator();
        while (iterator.hasNext())
        {
            Item item = (Item) iterator.next();
            MealTiming mealTiming = item.getMealTiming();
            String startTime = mealTiming.getStartTime();
            String endTime = mealTiming.getEndTime();

            try {
                Date sd =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime);
                Date ed =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);

                Date st = new Date();
                st.setHours(sd.getHours());
                st.setMinutes(sd.getMinutes());

                Date et = new Date();
                et.setHours(ed.getHours());
                et.setMinutes(ed.getMinutes());

                Date currentDate = new Date();
                System.out.println("st :"+st); System.out.println("ct :"+currentDate);   System.out.println("et :"+et);

                if(currentDate.after(st) && currentDate.before(et))
                {


                    if(item.getName().toLowerCase().contains(key) || item.getId().toString().contains(key) )
                    {

                        Item item1= new Item();
                        item1 = item;
                        filteredList.add(item1);


                        System.out.println("----Available");

                    }

                }
                else
                {
                    System.out.println("----Unavailable");
                }





            } catch (Exception e) {
                e.printStackTrace();
            }


        }



        ArrayList<Item> tempItems = new ArrayList<Item>();

    /*    for(int i=0;i<Store.getInstance().itemList.size();i++)
        {

            System.out.println("Name LC :"+Store.getInstance().itemList.get(i).getName().toLowerCase());
            System.out.println("status  :"+Store.getInstance().itemList.get(i).getName().toLowerCase().contains(key));
            if(Store.getInstance().itemList.get(i).getName().toLowerCase().contains(key) || Store.getInstance().itemList.get(i).getId().toString().contains(key) )
            {

                Item item = new Item();
                item = Store.getInstance().itemList.get(i);
                tempItems.add(item);


            }
        }

*/
    tempItems.addAll(filteredList);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Store.getInstance().addedItemList.clear();
                Log.d("ITEM SHOW DIALOG- ADD :", "INVOKED");
                for(int i = 0;i< Store.getInstance().itemList.size();i++)
                {
                    if(Store.getInstance().itemList.get(i).getCalculatedQuantiy()>0)
                    {
                        boolean status = true;
                        for(int y=0;y<Store.getInstance().addedItemList.size();y++)
                        {
                            Log.d("Item Info:","added = "+Store.getInstance().addedItemList.get(y).getId());
                            Log.d("Item Info:","input = "+Store.getInstance().itemList.get(i).getId());

                            if(Store.getInstance().addedItemList.get(y).getId() !=Store.getInstance().itemList.get(i).getId())
                            {
                                Log.d("Item Info:","New Entry");
                            }
                            else
                            {    status = false;
                                Log.d("Item Info:","already exist");
                                Toast.makeText(DashboardActivity.this, Store.getInstance().itemList.get(i).getName()+" Already Exist..", Toast.LENGTH_SHORT).show();
                            }
                        }
                        if(status)
                        {
                            Item item =  new Item();
                            item = Store.getInstance().itemList.get(i);
                            Store.getInstance().addedItemList.add(item);
                            Log.d("Item added:",Store.getInstance().itemList.get(i).getName());
                            addedItemListAdapter.notifyDataSetChanged();
                        }





                    }

                }






                dialog.dismiss();

            }
        });


        Log.d("SIZE Items:", String.valueOf(tempItems.size()));


        //
        itemListAdapter = new ItemListAdapter(DashboardActivity.this,cloneItems(tempItems));
        listView.setAdapter(itemListAdapter);
        Log.d("SIZE Items:", String.valueOf(itemListAdapter.getCount()));
        dialog.show();

    }


    public ArrayList<Item> cloneItems(ArrayList<Item> items)
    {
        ArrayList<Item> clonedItems =  new ArrayList<>();
        for (int i=0;i<items.size();i++)
        {
            Item item = new Item();
            item = items.get(i);
            clonedItems.add(item);
        }

        return clonedItems;

    }

    class CatList extends AsyncTask<Void, Void, Boolean>
    {
        HashMap<String,String> params;
        String url;
        String jsonResult;
        Long id;


        public CatList(long currentCategoryId) {
            super();

            url = "category/toList";
            jsonResult = null;
            this.id = currentCategoryId;
            params = new HashMap<String, String>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            params.put("id", String.valueOf(id));

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


                Type collectionType = new TypeToken<Collection<Category>>() {
                }.getType();

                Collection<Category> restaurants = gson.fromJson(jsonResult, collectionType);


                Store.getInstance().categoryList = (ArrayList<Category>) restaurants;

                System.out.println("cat res :"+jsonResult);


                System.out.println("catList Size  :"+Store.getInstance().categoryList.size());

                listViewAdapterContent = new String[Store.getInstance().categoryList.size()];
                for(int i=0;i<Store.getInstance().categoryList.size();i++)
                {
                    listViewAdapterContent[i]=Store.getInstance().categoryList.get(i).getName();
                }

                int layoutItemId = android.R.layout.simple_dropdown_item_1line;
                adapter = new ArrayAdapter<>(DashboardActivity.this, layoutItemId, listViewAdapterContent);
                categorySearchBox.setAdapter(adapter);



            }
        }


    }
    class ItemList extends AsyncTask<Void, Void, Boolean>
    {
        HashMap<String,String> params;
        String url;
        String jsonResult;
        Long id;


        public ItemList(long itemId) {
            super();

            url = "item/toList";
            jsonResult = null;
            this.id = itemId;
            params = new HashMap<String, String>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            params.put("id", String.valueOf(id));

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
                Type collectionType = new TypeToken<Collection<Item>>() {
                }.getType();
                Collection<Item> items = gson.fromJson(jsonResult, collectionType);
              Store.getInstance().itemList = (ArrayList<Item>) items;


                System.out.println("item res :"+jsonResult);
                System.out.println("item Size  :"+Store.getInstance().itemList.size());
                System.out.println("added item Size  :"+Store.getInstance().addedItemList.size());



                itemCount.setText(String.valueOf(Store.getInstance().itemList.size()+" "+Store.getInstance().configuration.getItemLabel()));


            }
        }


    }
    class SaveBill extends AsyncTask<Void, Void, Boolean>
    {
        HashMap<String,String> params;
        String url;
        String jsonResult;
        Long id;


        public SaveBill(HashMap<String,String> params) {
            super();

            url = "bill/savedata";
            jsonResult = null;

            this.params = params;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            params.put("id", String.valueOf(id));
            System.out.println("user id "+Store.getInstance().user.getId());
            params.put("user_id", String.valueOf(Store.getInstance().user.getId()));
            params.put("dated", String.valueOf(BizUtils.getCurrentTime()));


            if(Store.getInstance().currentTableId!=null  )
            {
                if(Store.getInstance().currentTableId!=0)
                {
                    params.put("table", String.valueOf(Store.getInstance().currentTableId));

                }
            }




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

                System.out.println("bill save status  :"+jsonResult);

                if(jsonResult.toLowerCase().contains("saved"))
                {
                    Toast.makeText(DashboardActivity.this, "Bill saved..", Toast.LENGTH_SHORT).show();


                    System.out.println("----ACTIONS-------"+params.get("ACTION"));
                        if( params.get("ACTION").toLowerCase().contains("update")) {



                            clear();
                        }
                        else
                        {
                            System.out.println("----Getting last Bill Details-------");
                            new GetLastBill().execute();
                        }





                }



            }
        }


    }
    public void clear()
    {

        Store.getInstance().addedItemList.clear();

        if(Store.getInstance().addedItemListAdapter!=null)
        {
            Store.getInstance().addedItemListAdapter.notifyDataSetChanged();
        }
        title.setText("Bill Id: Yet to generate");
        tableList.setText("None");
        subTotal.setText(String.valueOf("0.00"));
        gst.setText(String.valueOf("0.00"));
        discount.setText(String.valueOf("0.00"));
        grandTotal.setText(String.valueOf("0.00"));
        fromCustomer.setText(String.valueOf("0.00"));
        balance.setText(String.valueOf("0.00"));

        tableList.setText("None");

        Store.getInstance().currentTableName = "None";
        Store.getInstance().currentBill = null;
        Store.getInstance().currentBillId = null;
        Store.getInstance().currentTableBill = null;
        Store.getInstance().currentTableId = null;



        clear.performClick();



    }
    @Override
    public void onBackPressed() {



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DashboardActivity.this.finish();



                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    class GetLastBill extends AsyncTask<Void, Void, Boolean> {

        String url;
        String jsonResult;
        Long id;
        HashMap<String, String> params;


        public GetLastBill() {
            super();




            url = "bill/getLastBill";
            jsonResult = null;

            this.params = new HashMap<String, String>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            params.put("id", String.valueOf(id));
            System.out.println("waiter id " + Store.getInstance().user.getId());
            params.put("waiter_id", String.valueOf(Store.getInstance().user.getId()));


        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            jsonResult = HttpHandler.makeServiceCall(this.url, params);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (jsonResult != null) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();


                Type collectionType = new TypeToken<Bill>() {
                }.getType();

                Bill bill = gson.fromJson(jsonResult, collectionType);


                Store.getInstance().lastbill  = (Bill) bill;

                System.out.println("Bill json:"+jsonResult);

                if(BizUtils.write(DashboardActivity.this,Store.getInstance().user.getId()+"-"+BizUtils.getCurrentTime(),Store.getInstance().addedItemList))
                {
                    Toast.makeText(DashboardActivity.this, "Bill saved as pdf..", Toast.LENGTH_SHORT).show();
                    clear();

                }
                else
                {
                    Toast.makeText(DashboardActivity.this, "Bill not saved as pdf", Toast.LENGTH_SHORT).show();
                }


            }


        }
    }
    public class BillList extends AsyncTask<Void, Void, Boolean>
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

            params.put("type","openlist");
            params.put("waiterId", String.valueOf(Store.getInstance().user.getId()));


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



                Collections.reverse(Store.getInstance().billList);
                billListAdapter =new BillListAdapter(DashboardActivity.this,Store.getInstance().billList,DashboardActivity.billListDialog);
                billListView.setAdapter(billListAdapter);





            }
        }


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        println("Post resume");

        Item.loadItems(onTaskCompleted);
        Category.loadData(onTaskCompleted_c);

    }

    @Override
    protected void onResume() {
        super.onResume();
        println("resume");
    }
    Item.OnTaskCompleted onTaskCompleted = new Item.OnTaskCompleted() {
        @Override
        public void onTaskCompleted() {

            System.out.println("------Updated items @ dashboard");

        }
    };
    Category.OnTaskCompleted onTaskCompleted_c = new Category.OnTaskCompleted() {
        @Override
        public void onTaskCompleted() {

            println("---Update Category @ dashboard");
        }
    };
}
