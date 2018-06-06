package com.bizsoft.restaurant;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bizsoft.restaurant.dataobjects.Configuration;
import com.bizsoft.restaurant.dataobjects.Store;
import com.turkialkhateeb.materialcolorpicker.ColorChooserDialog;
import com.turkialkhateeb.materialcolorpicker.ColorListener;


public class ConfigurationActivity3 extends AppCompatActivity {

    ImageButton colorP;
    ImageView colorPV;
    ImageView colorS;
    ImageButton colorSV;

    ImageView colorT;
    ImageButton  colorTV;
    ImageView colorA;
    ImageButton colorAV;

    Button next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration3);

        //setTheme(R.style.AppTheme);
        colorP = (ImageButton) findViewById(R.id.color_picker_p);
        colorPV = (ImageView) findViewById(R.id.primary_color);
        colorS= (ImageView) findViewById(R.id.secondary_app_color);
        colorSV = (ImageButton) findViewById(R.id.secondaryPicker);
        colorT = (ImageView) findViewById(R.id.app_text_color);
        colorTV = (ImageButton) findViewById(R.id.text_color_picker);
        colorA = (ImageView) findViewById(R.id.app_actionbar_color);
        colorAV = (ImageButton) findViewById(R.id.actionbar_color_picker);
        next = (Button) findViewById(R.id.next);


        colorP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getColor(colorPV);
            }
        });
        colorSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getColor(colorS);
            }
        });
        colorTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getColor(colorT);
            }
        });
        colorAV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getColor(colorA);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Store.getInstance().theme.setPrimaryColor(colorPV.getSolidColor());
                Store.getInstance().theme.setPrimaryDarkActionBar(colorAV.getSolidColor());
                Store.getInstance().theme.setSecondaryColor(colorSV.getSolidColor());
                Store.getInstance().theme.setTextColor(colorTV.getSolidColor());

                Intent intent = new Intent(ConfigurationActivity3.this,MainActivity.class);
                startActivity(intent);


            }
        });

    }

    public void getColor(final ImageView view)
    {

        int colour = 0 ;
        final ColorChooserDialog dialog = new ColorChooserDialog(ConfigurationActivity3.this);
        dialog.setTitle(R.string.app_name);
        dialog.setColorListener(new ColorListener() {
            @Override
            public void OnColorClick(View v, int color) {
                //do whatever you want to with the values
                color = color;
                view.setColorFilter(color);
                dialog.dismiss();
            }
        });
        //customize the dialog however you want
        dialog.show();
    }
}
