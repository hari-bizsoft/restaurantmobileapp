package com.bizsoft.restaurant.dataobjects;

import android.os.AsyncTask;

import com.bizsoft.restaurant.service.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.bizsoft.restaurant.service.BizUtils.println;

/**
 * Created by GopiKing on 14-11-2017.
 */

public class Bill {

    Long id;
    float grandTotal;
    float subTotal;
    float gst;
    String paymentMode;
    float receivedFromCustomer;
    float balance;

    String tableName;
    String tableStatus;
    String waiter;

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    Long tableId;

    String tableDetails;

    public String getTableDetails() {
        return tableDetails;
    }

    public void setTableDetails(String tableDetails) {
        this.tableDetails = tableDetails;
    }

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    String datetime;

    public float getReceivedFromCustomer() {
        return receivedFromCustomer;
    }

    public void setReceivedFromCustomer(float receivedFromCustomer) {
        this.receivedFromCustomer = receivedFromCustomer;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(float grandTotal) {
        this.grandTotal = grandTotal;
    }

    public float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(float subTotal) {
        this.subTotal = subTotal;
    }

    public float getGst() {
        return gst;
    }

    public void setGst(float gst) {
        this.gst = gst;
    }

    public float getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public float getDiscountValue() {
        return discountValue;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }

    public void setDiscountValue(float discountValue) {
        this.discountValue = discountValue;
    }

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }

    float discountPercentage;
    float discountValue;
    ArrayList<Item> itemList = new ArrayList<Item>();


    public static void loadData(final OnTaskCompleted onTaskCompleted, final HashMap<String, String> hm)
    {

        new AsyncTask<Void, Void, List<Bill>>() {

            public String jsonResult;
            public String url;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                url = "bill/toList";
            }

            @Override
            protected List<Bill> doInBackground(Void... voids) {

                jsonResult = HttpHandler.makeServiceCall(this.url,hm);
                return (List<Bill>) Store.getInstance().billList;
            }

            @Override
            protected void onPostExecute(List<Bill> items) {
                super.onPostExecute(items);
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

                    onTaskCompleted.onTaskCompleted();
                }
                else
                {
                    println("Result :"+jsonResult);
                }

            }
        }.execute();


        return;
    }


    public interface OnTaskCompleted{
        void onTaskCompleted();
    }


}
