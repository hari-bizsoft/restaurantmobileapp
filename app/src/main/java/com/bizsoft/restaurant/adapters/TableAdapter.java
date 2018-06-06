package com.bizsoft.restaurant.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.restaurant.DashboardActivity;
import com.bizsoft.restaurant.R;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.dataobjects.Table;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;

/**
 * Created by GopiKing on 28-11-2017.
 */

public class TableAdapter extends BaseAdapter {

    Context context;
    private LayoutInflater inflater;

    public TableAdapter(Context context, ArrayList<Table> tableList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.tableList = tableList;
    }

    ArrayList<Table> tableList;


    @Override
    public int getCount() {
        return tableList.size();
    }

    @Override
    public Table getItem(int i) {
        return tableList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return tableList.get(i).getId();
    }

    class Holder
    {

        ImageView image;
        TextView name;
        TextView billCount;

    }
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final Holder holder = new Holder();
      //  final Table table = (Table) getItem(i);
        view = inflater.inflate(R.layout.table_single_item, viewGroup, false);
        Calligrapher calligrapher = new Calligrapher(context);
        calligrapher.setFont(view, "fonts/Quicksand-BoldItalic.otf");
        
        holder.name = view.findViewById(R.id.name);
        holder.image = view.findViewById(R.id.image);

        holder.name.setText(String.valueOf(getItem(i).getName()));
        holder.billCount  = view.findViewById(R.id.bill_count);

        holder.billCount.setText(String.valueOf(getItem(i).getBillList().size()));
        if(getItem(i).getBillList().size()>0)
        {

            int id = context.getResources().getIdentifier("table_orange", "drawable",context.getPackageName());
            holder.image.setImageResource(id);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Long id = getItemId(i);

                Store.getInstance().currentTableId = id;
                Store.getInstance().currentTableName = getItem(i).getName();

                Toast.makeText(context, "Tab Id :"+id, Toast.LENGTH_SHORT).show();

               /* Intent intent = new Intent(context, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                */

               showMenu(i,getItem(i));

            }
        });
        return view;
    }
    public void showMenu(int i, final Table item)
    {

        ListView listview;
        Button newBill;
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.table_menu);

        listview = dialog.findViewById(R.id.listview);
        newBill = dialog.findViewById(R.id.new_bill);

        System.out.println("Size of bill items ---"+item.getBillList().size());
        listview.setAdapter(new TableBillItemListAdapter(context,item.getBillList()));
        newBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Store.getInstance().addedItemList.clear();
                Store.getInstance().currentTableBill = null;
                Store.getInstance().currentTableName = item.getName();

                Store.getInstance().currentTableId = item.getId();


                Store.getInstance().addedItemListAdapter.notifyDataSetChanged();
                Intent intent = new Intent(context, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);


            }
        });
        dialog.show();


    }

}
