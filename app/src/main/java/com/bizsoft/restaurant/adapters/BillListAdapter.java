package com.bizsoft.restaurant.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bizsoft.restaurant.DashboardActivity;
import com.bizsoft.restaurant.R;
import com.bizsoft.restaurant.ShowBillActivity;
import com.bizsoft.restaurant.dataobjects.Bill;
import com.bizsoft.restaurant.dataobjects.Store;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;

/**
 * Created by GopiKing on 14-11-2017.
 */

public class BillListAdapter extends BaseAdapter {
    Context context;
    private LayoutInflater inflater;
    public ArrayList<Bill> billList;

    public  Dialog billListDialog;
    public BillListAdapter(Context context, ArrayList<Bill> billList, Dialog billListDialog) {
        this.context = context;
        this.billList = billList;
        this.inflater = LayoutInflater.from(context);
        this.billListDialog = billListDialog;

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
        TextView id,amount,itemCount,kot,table_details;
    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final Holder holder = new Holder();
        final Bill bill= (Bill) getItem(position);

        view = inflater.inflate(R.layout.show_bill_single_item, viewGroup, false);
        Calligrapher calligrapher = new Calligrapher(context);
        calligrapher.setFont(view, "fonts/Quicksand-BoldItalic.otf");

        holder.id = view.findViewById(R.id.id);

        holder.amount = view.findViewById(R.id.amount);
        holder.itemCount = view.findViewById(R.id.name);
        holder.kot= view.findViewById(R.id.kot);
        holder.table_details= view.findViewById(R.id.table_details);


        int kotItemsUnserved =0;

        System.out.println("item size "+bill.getItemList().size());
        for(int i=0;i<bill.getItemList().size();i++)
        {
            System.out.println("kot --- "+bill.getItemList().get(i).kot);
            if(bill.getItemList().get(i).kot!=null)
            {

                if(bill.getItemList().get(i).kot.status!=null)
                {

                    System.out.println("Status --- "+bill.getItemList().get(i).kot.status);
                    if(!bill.getItemList().get(i).kot.getStatus().toLowerCase().contains("served"))
                    {
                        kotItemsUnserved++;
                    }

                }
            }


        }
        if(kotItemsUnserved==0)
        {
            holder.kot.setVisibility(View.GONE);
        }

        if(bill.getTableId()!=null)
        {
            holder.table_details.setText(String.valueOf("Table Details: "+bill.getTableDetails()));
        }
        else
        {
            holder.table_details.setVisibility(View.GONE);
        }

        holder.id.setText("Bill ID :"+String.valueOf(bill.getId()));

        holder.amount.setText("RM "+String.valueOf(String.format("%.2f",bill.getGrandTotal())));
        holder.itemCount.setText(String.valueOf(bill.getItemList().size())+" Items");
        holder.kot.setText("KOT Details: "+kotItemsUnserved+" Need to be served");


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billListDialog.dismiss();

                Store.getInstance().currentTableName = "None";
                Store.getInstance().currentBill = null;
                Store.getInstance().currentBillId = null;
                Store.getInstance().currentTableBill = null;
                Store.getInstance().currentTableId = null;



                System.out.println("Table details "+bill.getTableDetails()+"---------"+bill.getTableId());
                if(bill.getTableDetails()!=null)
                {

                    Store.getInstance().currentTableBill = bill;
                    Store.getInstance().currentTableId = bill.getTableId();
                    Store.getInstance().currentTableName = bill.getTableName();
                    Store.getInstance().addedItemList.clear();
                    Store.getInstance().addedItemList.addAll(bill.getItemList());
                    Store.getInstance().addedItemListAdapter.notifyDataSetChanged();
                    Intent intent = new Intent(context, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
                else
                {


                    Store.getInstance().currentBill = bill;
                    Store.getInstance().addedItemList.clear();
                    Store.getInstance().addedItemList.addAll(bill.getItemList());
                    Store.getInstance().addedItemListAdapter.notifyDataSetChanged();
                    Intent intent = new Intent(context, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);

                }

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
