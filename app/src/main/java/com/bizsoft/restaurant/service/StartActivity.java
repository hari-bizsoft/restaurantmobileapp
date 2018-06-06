package com.bizsoft.restaurant.service;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bizsoft.restaurant.ConfigurationActivity;
import com.bizsoft.restaurant.R;

public class StartActivity extends AppCompatActivity {

    Button getStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getStarted = (Button) findViewById(R.id.get_started);
        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StartActivity.this, ConfigurationActivity.class);
                startActivity(intent);
            }
        });
    }
}
