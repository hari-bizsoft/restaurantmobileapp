package com.bizsoft.restaurant.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizsoft.restaurant.CategoryActvity;
import com.bizsoft.restaurant.InfoActivity;
import com.bizsoft.restaurant.R;
import com.bizsoft.restaurant.dataobjects.Restaurant;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;

/**
 * Created by GopiKing on 30-10-2017.
 */

public class RestaurantListAdapter extends BaseAdapter{

    Context context;
    private LayoutInflater inflater;
    ArrayList<Restaurant> resList;


    public RestaurantListAdapter(Context context, ArrayList<Restaurant> resList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.resList = resList;
    }

    @Override
    public int getCount() {
        return resList.size();
    }

    @Override
    public Restaurant getItem(int i) {
        return resList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return resList.get(i).getId();
    }
    public String getImage(int i)
    {
        return resList.get(i).getImage();
    }

    class Holder
    {
     TextView name,location;
        ImageView image;
        FloatingActionButton info;
        ImageView delivery,preOrder,dineIn;

    }
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        Holder holder = new Holder();

        final Restaurant restaurant = (Restaurant) getItem(position);
        view = inflater.inflate(R.layout.res_single_item, viewGroup, false);
        Calligrapher calligrapher = new Calligrapher(context);
        calligrapher.setFont(view, "fonts/Quicksand-BoldItalic.otf");

        holder.name = view.findViewById(R.id.res_name);
        holder.location =  view.findViewById(R.id.location);
        holder.image =  view.findViewById(R.id.image);


        holder.delivery = view.findViewById(R.id.delivery);
        holder.dineIn = view.findViewById(R.id.dinein);
        holder.preOrder = view.findViewById(R.id.preOrder);


        if(!restaurant.isDelivery()) {
            holder.delivery.setVisibility(View.INVISIBLE);
        }
        if(!restaurant.isDineIn()) {
            holder.dineIn.setVisibility(View.INVISIBLE);
        }
        if(!restaurant.isPreOrder()) {
            holder.preOrder.setVisibility(View.INVISIBLE);
        }



        holder.name.setText(String.valueOf(restaurant.getName()));
        holder.location.setText(String.valueOf(restaurant.getLocation()));

        holder.info =  view.findViewById(R.id.info);
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Store.getInstance().currentResInfo = getItem(position);
                Intent intent =  new Intent(context,InfoActivity.class);
                context.startActivity(intent);
            }
        });


        Glide.with(context).load(Store.getInstance().baseUrl + restaurant.getImage()).into(holder.image);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Store.getInstance().currentResId = getItemId(position);
                Store.getInstance().currentResImage = getImage(position);
                Store.getInstance().currentRestaurant = getItem(position);

                System.out.println("ID----"+Store.getInstance().currentResId);
                Intent intent =  new Intent(context,CategoryActvity.class);
                context.startActivity(intent);
            }
        });


        return view;
    }
}
