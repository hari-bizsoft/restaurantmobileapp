package com.bizsoft.restaurant.dataobjects;

import com.bizsoft.restaurant.R;

/**
 * Created by GopiKing on 20-02-2018.
 */

public class Theme {

    int primaryDarkActionBar;
    int primaryColor;
    int secondaryColor;
    int textColor;

    Theme()
    {
        primaryDarkActionBar = R.color.colorPrimaryDark;
        primaryColor = R.color.colorPrimary;
        secondaryColor = R.color.colorAccent;
        textColor = R.color.selector_text;
    }

    public int getPrimaryDarkActionBar() {
        return primaryDarkActionBar;
    }

    public void setPrimaryDarkActionBar(int primaryDarkActionBar) {
        this.primaryDarkActionBar = primaryDarkActionBar;
    }

    public int getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}
