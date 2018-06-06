package com.bizsoft.restaurant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizsoft.restaurant.dataobjects.Store;
import com.bumptech.glide.Glide;

import me.anwarshahriar.calligrapher.Calligrapher;

public class InfoActivity extends AppCompatActivity {

    TextView name,address,phone;
    ImageView image;
    ImageView delivery,preOrder,dineIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        getSupportActionBar().setTitle("Restaurant Info");
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "fonts/Quicksand-BoldItalic.otf", true);
        name =  findViewById(R.id.res_name);
        address =  findViewById(R.id.address);
        phone =  findViewById(R.id.phone_number);
        delivery = findViewById(R.id.delivery);
        dineIn = findViewById(R.id.dinein);
        preOrder = findViewById(R.id.preOrder);
        if(!Store.getInstance().currentResInfo.isDelivery()) {
            delivery.setVisibility(View.INVISIBLE);
        }
        if(!Store.getInstance().currentResInfo.isDineIn()) {
            dineIn.setVisibility(View.INVISIBLE);
        }
        if(!Store.getInstance().currentResInfo.isPreOrder()) {
            preOrder.setVisibility(View.INVISIBLE);
        }

        image =  findViewById(R.id.image);


        name.setText(String.valueOf(Store.getInstance().currentResInfo.getName()));
        address.setText(String.valueOf(Store.getInstance().currentResInfo.getAddress()));
        phone.setText(String.valueOf(Store.getInstance().currentResInfo.getPhone()));
        Glide.with(InfoActivity.this).load(Store.getInstance().baseUrl + Store.getInstance().currentResInfo.getImage()).into(image);




    }
}
