package com.bizsoft.restaurant.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.restaurant.R;
import com.bizsoft.restaurant.dataobjects.BizFile;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by GopiKing on 31-08-2017.
 */

public class FileMangerAdpater extends BaseAdapter {

    Context context;

    LayoutInflater layoutInflater= null;
    public ArrayList<BizFile> fileList = new ArrayList<BizFile>();


    public FileMangerAdpater(Context context, ArrayList<BizFile> fileList) {
        this.context = context;
        this.layoutInflater = (LayoutInflater.from(context));
        this.fileList = fileList;
    }
    @Override
    public int getCount() {
        return this.fileList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.fileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    class  Holder
    {
        TextView fileName;
        Button sendMail,view;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        convertView = layoutInflater.inflate(R.layout.file_list_single_item, null);

        holder.fileName = (TextView) convertView.findViewById(R.id.file_name);
        holder.sendMail= (Button) convertView.findViewById(R.id.send_mail);
        holder.view= (Button) convertView.findViewById(R.id.view);
        holder.fileName.setText(String.valueOf(fileList.get(position).getFileName()));

        holder.sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    sendMail(fileList.get(position).getDetails().getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    openPDF(fileList.get(position).getDetails());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return convertView;
    }
    public void sendMail(String absolutePath) throws Exception {

        BackgroundMail.newBuilder(context)
                .withUsername("hari.bizsoft@gmail.com")
                .withPassword("Bizsoft@123")
                .withMailto("hari.bizsoft@gmail.com")
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject("Invoice copy")
                .withBody("This is a digitally generated invoice copy from Denariu Soft SDN BHD.")
                .withAttachments(absolutePath)


                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        //do some magic
                        Toast.makeText(context, "Success..", Toast.LENGTH_SHORT).show();
                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        //do some magic
                    }
                })
                .send();
    }
    public  void openPDF(File file)
    {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
// Just example, you should parse file name for extension
        String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(".pdf");

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), mime);
        ((Activity) context).startActivityForResult(intent, 10);

    }
}
