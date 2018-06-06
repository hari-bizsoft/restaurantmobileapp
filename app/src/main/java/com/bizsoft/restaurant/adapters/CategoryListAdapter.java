package com.bizsoft.restaurant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bizsoft.restaurant.R;
import com.bizsoft.restaurant.dataobjects.Category;
import com.bizsoft.restaurant.dataobjects.Store;

import java.util.ArrayList;

import me.anwarshahriar.calligrapher.Calligrapher;

/**
 * Created by GopiKing on 01-11-2017.
 */

public class CategoryListAdapter extends BaseAdapter{
    Context context;
    private LayoutInflater inflater;
    ArrayList<Category> categoryList;

    public CategoryListAdapter(Context context, ArrayList<Category> categoryList) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.categoryList = categoryList;
    }
    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int i) {
        return categoryList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return categoryList.get(i).getId();
    }
    class Holder
    {
        TextView name;
        TextView itemSize;


    }
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();

        final Category category = (Category) getItem(position);
        view = inflater.inflate(R.layout.category_single_item, viewGroup, false);
        holder.name = view.findViewById(R.id.res_name);
        holder.itemSize = view.findViewById(R.id.item_size);
        Calligrapher calligrapher = new Calligrapher(context);
        calligrapher.setFont(view, "fonts/Quicksand-BoldItalic.otf");


        holder.name.setText(String.valueOf(category.getName()));
        holder.itemSize.setText(Store.getInstance().categoryList.get(position).getItemSize()+" "+Store.getInstance().configuration.getItemLabel());


        return view;
    }
}
