package com.bizsoft.restaurant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bizsoft.restaurant.R;
import com.bizsoft.restaurant.dataobjects.Item;
import com.bizsoft.restaurant.service.AssetPropertyReader;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by GopiKing on 08-11-2017.
 */

public class ShowItemListAdapter extends BaseAdapter {
    Context context;
    private LayoutInflater inflater;
    ArrayList<Item> itemList;
    private AssetPropertyReader assetsPropertyReader;
    private Properties property;

    public ShowItemListAdapter(Context context, ArrayList<Item> itemList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return itemList.get(i).getId();
    }

    class Holder
    {

        TextView id,name,quantity,price;



    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final Holder holder = new Holder();
        final Item item = (Item) getItem(position);
        view = inflater.inflate(R.layout.show_item_single_item, viewGroup, false);

        holder.id = view.findViewById(R.id.id);
        holder.name = view.findViewById(R.id.name);
        holder.price = view.findViewById(R.id.status);
        holder.quantity = view.findViewById(R.id.note);

        holder.id.setText(String.valueOf(item.getId()));
        holder.name.setText(String.valueOf(item.getName()));
        holder.price.setText(String.valueOf(item.getPrice()));
        holder.quantity.setText(String.valueOf(item.getQuantity()));



        return view;
    }

}
