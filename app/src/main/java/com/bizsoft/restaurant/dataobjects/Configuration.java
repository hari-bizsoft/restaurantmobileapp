package com.bizsoft.restaurant.dataobjects;

import java.util.ArrayList;

/**
 * Created by GopiKing on 19-02-2018.
 */


public class Configuration {

    //App level
    String appName;
    String companyName;
    String appLogoMobileSource;
    byte[] appLogo;

    public byte[] getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(byte[] appLogo) {
        this.appLogo = appLogo;
    }

    //Entities name
    String categoryLabel;
    String itemLabel;
    String tableLabel;
    String userLabel;
    String kotLabel;
    String sessionLabel;


    //
    Boolean gst;
    Double gstValue;
    //
    Boolean discount;
    Double discountValue;
    public ArrayList<String> confNameList = new ArrayList<String>();

    public  ArrayList<EntityNameSet> confList = new ArrayList<EntityNameSet>();
    String currentTheme;
    public String getConfName() {
        return confName;
    }

    public void setConfName(String confName) {
        this.confName = confName;
    }

    String confName;
    public Configuration() {
        this.appName = "Restaurant";
        this.companyName = "DenariuSoft";
        this.appLogoMobileSource = "";
        this.categoryLabel = "Category";
        this.itemLabel = "Item";
        this.tableLabel = "Table";
        this.userLabel = "User";
        this.kotLabel = "KOT Channel";
        this.sessionLabel = "Meal Session";
        this.gst = true;
        this.gstValue = 6.0;
        this.discount = true;
        this.discountValue = 2.0;
        this.currentTheme = "Default";
        confNameList.add("Restaurant");
        confNameList.add("Car Wash service centre");
        confNameList.add("Parlour");
        confNameList.add("Custom");
        //intitAOB();

    }

    public void initAOB() {



        EntityNameSet restaurant = new EntityNameSet();
        restaurant.setName(Store.getInstance().configuration.confNameList.get(0));
        restaurant.setCategoryLabel("Category");
        restaurant.setItemLabel("Items");
        restaurant.setTableLabel("Table");
        restaurant.setSessionLabel("Meal Session");
        restaurant.setKotLabel("KOT");
        restaurant.setUserLabel("User");

        confList.add(restaurant);


        EntityNameSet carWash = new EntityNameSet();
        carWash.setName(Store.getInstance().configuration.confNameList.get(1));
        carWash.setCategoryLabel("Department");
        carWash.setItemLabel("Services");
        carWash.setTableLabel("Garage");
        carWash.setSessionLabel("Shift Timing");
        carWash.setKotLabel("Garage");
        carWash.setUserLabel("User");

        confList.add(carWash);

        EntityNameSet parlour = new EntityNameSet();
        parlour.setName(Store.getInstance().configuration.confNameList.get(2));
        parlour.setCategoryLabel("Treatment");
        parlour.setItemLabel("Services");
        parlour.setTableLabel("Stylist");
        parlour.setSessionLabel("Shift Timing");
        parlour.setKotLabel("Job");
        parlour.setUserLabel("User");

        confList.add(parlour);

        EntityNameSet custom = new EntityNameSet();
        custom.setName(Store.getInstance().configuration.confNameList.get(3));
        custom.setCategoryLabel("");
        custom.setItemLabel("");
        custom.setTableLabel("");
        custom.setSessionLabel("");
        custom.setKotLabel("");
        custom.setUserLabel("");
        confList.add(custom);

    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getAppLogoMobileSource() {
        return appLogoMobileSource;
    }

    public void setAppLogoMobileSource(String appLogoMobileSource) {
        this.appLogoMobileSource = appLogoMobileSource;
    }

    public String getCategoryLabel() {
        return categoryLabel;
    }

    public void setCategoryLabel(String categoryLabel) {
        this.categoryLabel = categoryLabel;
    }

    public String getItemLabel() {
        return itemLabel;
    }

    public void setItemLabel(String itemLabel) {
        this.itemLabel = itemLabel;
    }

    public String getTableLabel() {
        return tableLabel;
    }

    public void setTableLabel(String tableLabel) {
        this.tableLabel = tableLabel;
    }

    public String getUserLabel() {
        return userLabel;
    }

    public void setUserLabel(String userLabel) {
        this.userLabel = userLabel;
    }

    public String getKotLabel() {
        return kotLabel;
    }

    public void setKotLabel(String kotLabel) {
        this.kotLabel = kotLabel;
    }

    public String getSessionLabel() {
        return sessionLabel;
    }

    public void setSessionLabel(String sessionLabel) {
        this.sessionLabel = sessionLabel;
    }

    public Boolean getGst() {
        return gst;
    }

    public void setGst(Boolean gst) {
        this.gst = gst;
    }

    public Double getGstValue() {
        return gstValue;
    }

    public void setGstValue(Double gstValue) {
        this.gstValue = gstValue;
    }

    public Boolean getDiscount() {
        return discount;
    }

    public void setDiscount(Boolean discount) {
        this.discount = discount;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public String getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(String currentTheme) {
        this.currentTheme = currentTheme;
    }
}
