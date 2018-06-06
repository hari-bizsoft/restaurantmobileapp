package com.bizsoft.restaurant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bizsoft.restaurant.R;
import com.bizsoft.restaurant.dataobjects.Item;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;

/**
 * Created by GopiKing on 08-11-2017.
 */

public class BillItemListAdapter extends BaseAdapter {
    Context context;
    private LayoutInflater inflater;
    ArrayList<Item> itemList;

    public BillItemListAdapter(Context context, ArrayList<Item> itemList) {
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
        TextView name;

        TextView quantity;
        TextView price;


    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final Holder holder = new Holder();
        final Item item = (Item) getItem(position);
        view = inflater.inflate(R.layout.bill_item_single_item, viewGroup, false);
        Calligrapher calligrapher = new Calligrapher(context);
        calligrapher.setFont(view, "fonts/Quicksand-BoldItalic.otf");
        holder.name = view.findViewById(R.id.name);
        holder.price = view.findViewById(R.id.status);
        holder.quantity = view.findViewById(R.id.note);



        holder.price.setText(String.valueOf(item.getPriceRef()));



        holder.quantity.setText(String.valueOf(item.getQuantity()));




        holder.name.setText(String.valueOf(item.getName()));


        return view;
    }
}
