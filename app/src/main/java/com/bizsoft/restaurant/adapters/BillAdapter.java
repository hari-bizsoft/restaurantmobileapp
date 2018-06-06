package com.bizsoft.restaurant.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bizsoft.restaurant.R;
import com.bizsoft.restaurant.ShowBillActivity;
import com.bizsoft.restaurant.dataobjects.Bill;
import com.bizsoft.restaurant.dataobjects.Store;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;

/**
 * Created by GopiKing on 14-11-2017.
 */

public class BillAdapter extends BaseAdapter {
    Context context;
    private LayoutInflater inflater;
    public ArrayList<Bill> billList;

    public BillAdapter(Context context, ArrayList<Bill> billList) {
        this.context = context;
        this.billList = billList;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return billList.size();
    }

    @Override
    public Object getItem(int i) {
        return billList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return billList.get(i).getId();
    }

    class Holder
    {
        TextView id,amount,itemCount;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final Holder holder = new Holder();
        final Bill bill= (Bill) getItem(position);




        view = inflater.inflate(R.layout.bill_single_item, viewGroup, false);
        Calligrapher calligrapher = new Calligrapher(context);
        calligrapher.setFont(view, "fonts/Quicksand-BoldItalic.otf");

        holder.id = view.findViewById(R.id.id);

        holder.amount = view.findViewById(R.id.amount);
        holder.itemCount = view.findViewById(R.id.name);

        holder.id.setText("Bill ID :"+String.valueOf(bill.getId()));

        holder.amount.setText("RM "+String.valueOf(String.format("%.2f",bill.getGrandTotal())));
        holder.itemCount.setText(String.valueOf(bill.getItemList().size())+" Items");


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Store.getInstance().currentShowBill= bill;
                Intent intent = new Intent(context, ShowBillActivity.class);
                context.startActivity(intent);

            }
        });


        return view;
    }
    public float getTotal()
    {
        float gt =0;
        for(int i=0;i<billList.size();i++)
        {
            gt =gt + billList.get(i).getGrandTotal();
        }
        return gt;
    }
}
