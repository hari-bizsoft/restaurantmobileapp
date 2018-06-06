package com.bizsoft.restaurant.adapters;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.restaurant.R;
import com.bizsoft.restaurant.dataobjects.Item;
import com.bizsoft.restaurant.dataobjects.KOT;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.service.AssetPropertyReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import me.anwarshahriar.calligrapher.Calligrapher;

/**
 * Created by GopiKing on 08-11-2017.
 */

public class ItemListAdapter extends BaseAdapter {
    Context context;
    private LayoutInflater inflater;
    ArrayList<Item> itemList;
    private AssetPropertyReader assetsPropertyReader;
    private Properties property;

    public ItemListAdapter(Context context, ArrayList<Item> itemList) {
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
        ImageButton plus ,minus;
        TextView quantity;

        TextView calculatedPrice;
        TextView calculatedQuantity;
        Button kot;
        TextView uom;

    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final Holder holder = new Holder();
        final Item item = (Item) getItem(position);
        view = inflater.inflate(R.layout.item_single_item, viewGroup, false);
        Calligrapher calligrapher = new Calligrapher(context);
        calligrapher.setFont(view, "fonts/Quicksand-BoldItalic.otf");
        holder.name = view.findViewById(R.id.name);

        holder.quantity = view.findViewById(R.id.note);
        holder.calculatedPrice =  view.findViewById(R.id.c_price);
        holder.plus = view.findViewById(R.id.plus);
        holder.minus = view.findViewById(R.id.minus);
        holder.uom = view.findViewById(R.id.uom);
        holder.calculatedQuantity = view.findViewById(R.id.c_quantity);


        holder.kot = view.findViewById(R.id.kot);
        holder.kot.setText(String.valueOf("Sent to "+Store.getInstance().configuration.getKotLabel()));
        holder.kot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    showKOT(holder,item);
            }
        });



        holder.uom.setText(String.valueOf(item.getUom()));

        holder.calculatedPrice.setText(String.valueOf("0.00"));


        holder.quantity.setText(String.valueOf(item.getQuantity()));


        assetsPropertyReader = new AssetPropertyReader(context);
        property = assetsPropertyReader.getProperties("BizProp.properties");


        if(Boolean.valueOf(property.getProperty("isKot")))
        {
            holder.kot.setVisibility(View.VISIBLE);

        }
        else
        {
            holder.kot.setVisibility(View.GONE );
        }


        int qty = item.getQuantity();
        final int[] x = {0};
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                x[0] = 0;
                try {
                    x[0] = Integer.valueOf(holder.calculatedQuantity.getText().toString());
                    Log.d("Plus : ", String.valueOf(x[0]));
                    Log.d("Qty : ", String.valueOf(item.getQuantity()));
                    Log.d("Res : ", String.valueOf((x[0]< item.getQuantity())));
                    if(x[0]< item.getQuantity())
                    {

                        x[0]++;
                        float va = x[0] * Float.parseFloat(item.getPrice());
                        holder.calculatedPrice.setText(String.valueOf(va));
                        item.setCalculatedQuantiy(x[0]);
                        item.setCalculatedPrice(va);

                        holder.calculatedQuantity.setText(String.valueOf(item.getCalculatedQuantiy()));

                        Log.d("QTY  : ", String.valueOf(  item.getCalculatedQuantiy()));


                    }
                    else
                    {
                        Toast.makeText(context, "Invalid Quantity", Toast.LENGTH_SHORT).show();
                    }





                }
                catch (Exception e)
                {
                    Toast.makeText(context, "Invalid Quantity", Toast.LENGTH_SHORT).show();
                }



            }
        });


        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                x[0] = 0;
                try {
                    x[0] = Integer.valueOf(holder.calculatedQuantity.getText().toString());
                    Log.d("Minus : ", String.valueOf(x[0]));
                    if(x[0]>0)
                    {
                        x[0]--;


                        float va = x[0] * Float.parseFloat(item.getPrice());
                        holder.calculatedPrice.setText(String.valueOf(va));
                        item.setCalculatedQuantiy(x[0]);
                        item.setCalculatedPrice(va);
                        holder.calculatedQuantity.setText(String.valueOf(item.getCalculatedQuantiy()));
                    }
                    else
                    {
                        Toast.makeText(context, "Invalid Quantity", Toast.LENGTH_SHORT).show();
                    }

                }
                catch (Exception e)
                {
                    Toast.makeText(context, "Invalid Quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.name.setText(String.valueOf(item.getName()));


        return view;
    }
    public void showKOT(final Holder holder, final Item item)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.show_kot);
        final Button button = dialog.findViewById(R.id.sent_to_kot);
        final EditText note = dialog.findViewById(R.id.note);








        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                KOT kot = new KOT();
                if(Store.getInstance().currentTableId!=null)
                {
                    if(Store.getInstance().currentTableId!=0)
                    {
                        kot.setTableId(Store.getInstance().currentTableId);
                    }

                }
               // System.out.println("KOT---NOTE--"+(note.getText()));
                if(TextUtils.isEmpty(note.getText()))
                {
                    kot.setNote(String.valueOf("None"));
                }
                else
                {
                    kot.setNote(String.valueOf(note.getText()));
                }

                kot.setItem_id(String.valueOf((item.getId())));
                kot.setWaiter_id(String.valueOf(Store.getInstance().user.getId()));

                item.setKot(kot);
                holder.kot.setText("Added");


                Gson gson = new Gson();
                Type type = new TypeToken<KOT>() {}.getType();
                String json = gson.toJson(kot, type);
                System.out.println("kot json"+json);
                dialog.dismiss();


            }
        });
        dialog.show();

    }
}
