package com.bizsoft.restaurant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bizsoft.restaurant.Forms.BizTableActivity;
import com.bizsoft.restaurant.Forms.CreateCategoryActivity;
import com.bizsoft.restaurant.Forms.CreateItems;
import com.bizsoft.restaurant.Forms.CreateKOTChannelActivity;
import com.bizsoft.restaurant.Forms.MealSessionCreate;
import com.bizsoft.restaurant.Forms.UOMCreateActivity;
import com.bizsoft.restaurant.Forms.UserActivity;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.service.BizUtils;

import me.anwarshahriar.calligrapher.Calligrapher;

public class AdminActivity extends AppCompatActivity {

    Button items,category,table,user,uom,kotChannel,mealSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "fonts/Quicksand-BoldItalic.otf", true);

        getSupportActionBar().setTitle("Admin Tools");
        items = findViewById(R.id.items);
        category = findViewById(R.id.category);
        table = findViewById(R.id.Table);
        user = findViewById(R.id.user);
        uom = findViewById(R.id.uom);
        kotChannel  = findViewById(R.id.kot_channel);
        mealSession = findViewById(R.id.meal_session);
        BizUtils.addCustomActionBar(AdminActivity.this,"Admin Tools");


        items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, CreateItems.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, CreateCategoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, BizTableActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, UserActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        uom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, UOMCreateActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        kotChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, CreateKOTChannelActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        mealSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, MealSessionCreate.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        if(Boolean.valueOf(Store.getInstance().getProperty(AdminActivity.this).getProperty("isTableModel")))
        {
            table.setVisibility(View.VISIBLE);
        }
        else
        {

            table.setVisibility(View.GONE);
        }

        if(Boolean.valueOf(Store.getInstance().getProperty(AdminActivity.this).getProperty("isKot")))
        {
            kotChannel.setVisibility(View.VISIBLE);
        }
        else
        {
            kotChannel.setVisibility(View.GONE);

        }


    }
}
