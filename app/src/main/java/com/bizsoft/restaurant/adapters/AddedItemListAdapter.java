package com.bizsoft.restaurant.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bizsoft.restaurant.DashboardActivity;
import com.bizsoft.restaurant.R;
import com.bizsoft.restaurant.dataobjects.Item;
import com.bizsoft.restaurant.dataobjects.Store;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;

/**
 * Created by GopiKing on 08-11-2017.
 */

public class AddedItemListAdapter extends BaseAdapter {
    Context context;
    private LayoutInflater inflater;
    public ArrayList<Item> itemList;

    public AddedItemListAdapter(Context context, ArrayList<Item> itemList) {
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
        TextView status;
        ImageButton remove;

    }
    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        final Holder holder = new Holder();
        final Item item = (Item) getItem(position);
        view = inflater.inflate(R.layout.added_item_single_item, viewGroup, false);
        Calligrapher calligrapher = new Calligrapher(context);
        calligrapher.setFont(view, "fonts/Quicksand-BoldItalic.otf");
        holder.name = view.findViewById(R.id.name);
        holder.price = view.findViewById(R.id.price);
        holder.quantity = view.findViewById(R.id.note);
        holder.remove = view.findViewById(R.id.remove);
        holder.status = view.findViewById(R.id.status);

        System.out.println("KOT -->>"+item.kot);
        if(item.kot!=null) {

            holder.status.setVisibility(View.VISIBLE);
            System.out.println("KOT --status"+item.kot.getStatus());
            holder.status.setText(item.getKot().getStatus());

            if(item.getKot().status==null)
            {

                holder.status.setText("ordered");
            }
            else
            {
                if(item.getKot().getStatus().toLowerCase().contains("preparing"))
                {

                    holder.status.setTextColor(Color.parseColor("#FFE69752"));
                }
                if(item.getKot().getStatus().toLowerCase().contains("cooked"))
                {
                    holder.status.setTextColor(Color.parseColor("#FF00838F"));
                }
                if(item.getKot().getStatus().toLowerCase().contains("served"))
                {
                    holder.status.setTextColor(Color.parseColor("#FF388E3C"));
                }
            }

        }
        else
        {
            System.out.println("KOT status null");

            holder.status.setVisibility(View.GONE);
        }



        holder.price.setText(String.valueOf(item.getCalculatedPrice()));



        holder.quantity.setText(String.valueOf(item.getCalculatedQuantiy()));


        int qty = item.getQuantity();
        final int[] x = {0};


        holder.name.setText(String.valueOf(item.getName()));

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<itemList.size();i++)
                {
                    if(position==i)
                    {
                        itemList.get(i).setCalculatedQuantiy(0);
                        itemList.get(i).setCalculatedPrice(0);
                        itemList.remove(i);
                        notifyDataSetChanged();
                    }

                }

            }
        });

        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();


        DashboardActivity.choosedItem.setText(String.valueOf(getCount()+" "+Store.getInstance().configuration.getItemLabel()));

        float st =0;
        float gst = 0;
        float gt =0;
        for(int i=0;i<itemList.size();i++)
        {
             st = st + itemList.get(i).getCalculatedPrice();
        }

        gst = (float) (st * (0.06));
        gt =  gst + st;

        DashboardActivity.subTotal.setText(String.valueOf(String.format("%.2f",st)));
        DashboardActivity.gst.setText(String.valueOf(String.format("%.2f",gst)));


        Store.getInstance().actualGT = gt;

        //
        float dis =0;
        float dp =0;
        float dv = 0;
        if(!TextUtils.isEmpty(DashboardActivity.discount.getText()))
        {



             dis = Float.parseFloat(DashboardActivity.discount.getText().toString());

             dp = (dis/100)*gt;



            dv =  gt - dp;

        }
        else
        {
            DashboardActivity.discountValue.setText(String.valueOf(String.format("%.2f",dp)));
        }


        DashboardActivity.discountValue.setText(String.valueOf(String.format("%.2f",dp)));
        DashboardActivity.grandTotal.setText(String.valueOf(String.format("%.2f",gt-dp)));


        float fc = 0;
        float bal = 0;


        float FRMCUS  =  0;
        if(!TextUtils.isEmpty(DashboardActivity.fromCustomer.getText()))
        {
            FRMCUS = Float.parseFloat(DashboardActivity.fromCustomer.getText().toString());


            if(FRMCUS>(gt-dp)) {
                bal = FRMCUS - (gt - dp);
            }


        }

            DashboardActivity.balance.setText(String.valueOf(String.format("%.2f", bal)));




    }
}
