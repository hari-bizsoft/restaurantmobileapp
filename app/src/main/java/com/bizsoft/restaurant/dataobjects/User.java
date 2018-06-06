package com.bizsoft.restaurant.dataobjects;

import android.os.AsyncTask;

import com.bizsoft.restaurant.service.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static com.bizsoft.restaurant.service.BizUtils.println;

/**
 * Created by GopiKing on 23-11-2017.
 */

public class User {

    Long id;
    String name;
    String username;
    String emailAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    String phoneNumber;
    String password;
    String role;


    public static void loadData(final OnTaskCompleted onTaskCompleted)
    {

        new AsyncTask<Void, Void, List<Category>>() {

            public String jsonResult;
            public String url;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                url = "user/toList";
            }

            @Override
            protected List<Category> doInBackground(Void... voids) {

                jsonResult = HttpHandler.makeServiceCall(this.url,null);
                return (List<Category>) Store.getInstance().categoryList;
            }

            @Override
            protected void onPostExecute(List<Category> items) {
                super.onPostExecute(items);
                if(jsonResult!=null)
                {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                    Gson gson = gsonBuilder.create();
                    Type collectionType = new TypeToken<Collection<User>>() {
                    }.getType();
                    Collection<User> tables = gson.fromJson(jsonResult, collectionType);
                    Store.getInstance().userList = (ArrayList<User>) tables;
                    System.out.println("item res :"+jsonResult);

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
    public static void saveUpdate(final OnTaskCompleted onTaskCompleted, final HashMap<String, String> params) {


        new AsyncTask<Void,Void,Void>()
        {

            String url = "user/saveUpdate";
            String result = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();


            }
            @Override
            protected Void doInBackground(Void... voids) {
                result = HttpHandler.makeServiceCall(this.url,params);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                if(result!=null)
                {
                    println(result);
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                    Gson gson = gsonBuilder.create();
                    Type collectionType = new TypeToken<User>() {
                    }.getType();
                    User user = gson.fromJson(result, collectionType);
                    User user1 = (User) user;
                    System.out.println("table res :"+user1.getId());
                    if(user1.getId()!=null) {
                        onTaskCompleted.onTaskCompleted();
                    }
                    else
                    {
                        println("Not saved");
                    }

                }
                else
                {
                    println("Result null..");
                }
            }
        }.execute();
    }
    public interface OnTaskCompleted{
        void onTaskCompleted();
    }

}
