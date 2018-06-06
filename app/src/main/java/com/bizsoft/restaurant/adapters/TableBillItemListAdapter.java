package com.bizsoft.restaurant.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bizsoft.restaurant.DashboardActivity;
import com.bizsoft.restaurant.R;
import com.bizsoft.restaurant.dataobjects.Bill;
import com.bizsoft.restaurant.dataobjects.Item;
import com.bizsoft.restaurant.dataobjects.Store;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;

/**
 * Created by GopiKing on 08-11-2017.
 */

public class TableBillItemListAdapter extends BaseAdapter {
    Context context;
    private LayoutInflater inflater;
    ArrayList<Bill> billList;

    public TableBillItemListAdapter(Context context, ArrayList<Bill> bills) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.billList = bills;
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
    {  TextView items,amount;
        Button open;


    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final Holder holder = new Holder();
        final Bill bill = (Bill) getItem(position);
        view = inflater.inflate(R.layout.table_menu_single_item, viewGroup, false);
        Calligrapher calligrapher = new Calligrapher(context);
        calligrapher.setFont(view, "fonts/Quicksand-BoldItalic.otf");

        holder.items =  view.findViewById(R.id.items);
        holder.amount = view.findViewById(R.id.amount);
        holder.open = view.findViewById(R.id.open);



        holder.items.setText(String.valueOf(bill.getItemList().size()+" Items"));
        holder.amount.setText(String.valueOf("RM "+bill.getGrandTotal()));

        holder.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Store.getInstance().currentBill = null;
                Store.getInstance().currentBillId = null;
                Store.getInstance().currentTableBill = null;





                Store.getInstance().currentTableBill = bill;




                Store.getInstance().addedItemList.clear();
                Store.getInstance().addedItemList.addAll(bill.getItemList());
                Store.getInstance().addedItemListAdapter.notifyDataSetChanged();
                Intent intent = new Intent(context, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);





            }
        });


        return view;
    }
}
