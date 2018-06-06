package com.bizsoft.restaurant.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.restaurant.KOTViewActivity;
import com.bizsoft.restaurant.R;
import com.bizsoft.restaurant.dataobjects.KOT;
import com.bizsoft.restaurant.dataobjects.KOTChannel;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.service.AssetPropertyReader;
import com.bizsoft.restaurant.service.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created by GopiKing on 08-11-2017.
 */

public class KOTItemListAdapter extends BaseAdapter {
    Context context;
    private LayoutInflater inflater;
    ArrayList<KOT> kotList;
    private AssetPropertyReader assetsPropertyReader;
    private Properties property;
    Holder holder = null;
    private ArrayList<String> statusList;

    public KOTItemListAdapter(Context context, ArrayList<KOT> kot) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.kotList = kot;
    }

    @Override
    public int getCount() {
        return this.kotList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.kotList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return this.kotList.get(i).getId();
    }

    class Holder {
        TextView id, name, note;
        Spinner status;
        TextView tableDetails;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        holder = new Holder();
        KOT kot = (KOT) getItem(position);
        view = inflater.inflate(R.layout.kot_item_single_item, viewGroup, false);

        holder.id = view.findViewById(R.id.id);
        holder.name = view.findViewById(R.id.name);
        holder.status = view.findViewById(R.id.status);
        holder.note = view.findViewById(R.id.note);
        holder.tableDetails = view.findViewById(R.id.table_details);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(400); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        holder.status.startAnimation(anim);


        holder.tableDetails.setText(kot.getTableDetailsFDB());



        try {
            holder.id.setText(String.valueOf(kot.getId()));
            holder.name.setText(String.valueOf(kot.getItem().getName()));

            holder.note.setText("( " + String.valueOf(kot.getNote()) + " )");
            setStatus(kot);

            int pos = 0;
            for (int i = 0; i < statusList.size(); i++) {

                if (statusList.get(i).toLowerCase().contains(kot.getStatus())) {
                    pos = i;
                }
            }
            holder.status.setSelection(pos);


        } catch (Exception e) {
            System.out.println("Ex :" + e);
        }

        return view;
    }

    public void setStatus(final KOT kot) {

        statusList = new ArrayList<String>();

        statusList.add("ordered");
        statusList.add("preparing");
        statusList.add("cooked");
        statusList.add("served");








        // Drop down layout style - list view with radio button
        final CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(context, statusList,R.layout.dropdown_style);
        holder.status.setAdapter(customSpinnerAdapter);

        holder.status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  Toast.makeText(getApplicationContext(), genderList.get(position), Toast.LENGTH_SHORT).show();

                String status = statusList.get(position);


                if (status.toLowerCase().contains("preparing")) {

                    customSpinnerAdapter.holder.name.setTextColor(Color.parseColor("#FFE69752"));
                }
                if (status.toLowerCase().contains("cooked")) {
                    customSpinnerAdapter.holder.name.setTextColor(Color.parseColor("#FF00838F"));
                }
                if (status.toLowerCase().contains("served")) {
                    customSpinnerAdapter.holder.name.setTextColor(Color.parseColor("#FF388E3C"));
                }

                String kot_id = String.valueOf(kot.getId());

                HashMap<String,String> params = new HashMap<String, String>();
                params.put("id",kot_id);
                params.put("status",status);
                new UpdateStatus(params).execute();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                int pos = 0;
                for (int i = 0; i < statusList.size(); i++) {

                    if (statusList.get(i).toLowerCase().contains(kot.getStatus())) {
                        pos = i;
                    }
                }
                holder.status.setSelection(pos);
            }

        });


    }

    class UpdateStatus extends AsyncTask<Void, Void, Boolean> {
        HashMap<String, String> params;
        String url;
        String jsonResult;
        Long id;


        public UpdateStatus(HashMap<String, String> params) {
            super();

            url = "KOTChannel/updateStatus";
            jsonResult = null;
            this.params = params;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            jsonResult = HttpHandler.makeServiceCall(this.url, params);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(jsonResult!=null)
            {
                System.out.println("KOT Channel =="+jsonResult);


                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();


                Type collectionType = new TypeToken<Collection<KOTChannel>>() {
                }.getType();
                Collection<KOTChannel> kotChannels= gson.fromJson(jsonResult, collectionType);
                Store.getInstance().kotChannels = (ArrayList<KOTChannel>) kotChannels;

                System.out.println("KOT Channel =="+Store.getInstance().kotChannels);

                //Toast.makeText(context, "Updated...", Toast.LENGTH_SHORT).show();
            }


        }

    }
}
