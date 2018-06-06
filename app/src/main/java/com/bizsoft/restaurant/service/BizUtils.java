package com.bizsoft.restaurant.service;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.restaurant.AdminActivity;
import com.bizsoft.restaurant.BillActivity;
import com.bizsoft.restaurant.DashboardActivity;
import com.bizsoft.restaurant.InvoiceListActivity;
import com.bizsoft.restaurant.KOTViewActivity;
import com.bizsoft.restaurant.LoginActivity;
import com.bizsoft.restaurant.MealTimingActivity;
import com.bizsoft.restaurant.R;
import com.bizsoft.restaurant.ReportsActivity;
import com.bizsoft.restaurant.TableActivity;
import com.bizsoft.restaurant.adapters.BillListAdapter;
import com.bizsoft.restaurant.dataobjects.Bill;
import com.bizsoft.restaurant.dataobjects.Company;
import com.bizsoft.restaurant.dataobjects.Item;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.dataobjects.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import me.anwarshahriar.calligrapher.Calligrapher;

import static com.bizsoft.restaurant.service.BizUtils.println;

/**
 * Created by GopiKing on 21-11-2017.
 */

public  class BizUtils {

    public static String getCurrentTime() {
        String fromDate;
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss");
        Date date = new Date();
        fromDate = dateFormat.format(date);

        return fromDate;
    }
    public static void println(String message)
    {
        System.out.println("BizUtils : "+message);

    }

    public static Boolean write(Context context, String fname, ArrayList<Item> productList) {
        try {

            Company company = new Company();


            BizUtils bizUtils = new BizUtils();

            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "fmcg");
            boolean success = true;


            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {
                // Do something on success
                Log.d("FOLDER CREATED", "TRUE");
            } else {
                // Do something else on failure
                Log.d("FOLDER CREATED", "FALSE");
            }


            folder.getAbsolutePath();
            Log.d("ABS PATH", folder.getAbsolutePath());
            //Create file path for Pdf
            String fpath = folder.getAbsolutePath() + "/" + fname + ".pdf";
            File file = new File(fpath);
            System.out.println("fpath = " + fpath);

            if (!file.exists()) {
                file.createNewFile();
            }
            // To customise the text of the pdf
            // we can use FontFamily
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN,
                    12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN,
                    12);
            // create an instance of itext document
            Document document = new Document();

            PdfWriter.getInstance(document,
                    new FileOutputStream(file.getAbsoluteFile()));
            document.open();

            try {
                // get receipt stream
                InputStream ims = context.getAssets().open("log_trans.png");
                Bitmap bmp = BitmapFactory.decodeStream(ims);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());


                PdfPTable logo = new PdfPTable(1);
                logo.setWidthPercentage(15);
                image.setAlignment(Image.ALIGN_CENTER);
                logo.addCell(image);
                document.add(logo);


            } catch (IOException ex) {
                ex.getStackTrace();
            }

            //using add method in document to insert a paragraph
            PdfPTable thank = new PdfPTable(1);
            thank.setWidthPercentage(100);
            thank.addCell(getCell(context.getString(R.string.app_name), PdfPCell.ALIGN_CENTER));
            document.add(thank);
            document.add(new Paragraph("_____________________________________________________________________________"));
            document.add(new Paragraph("                                                                             "));

            PdfPTable cn = new PdfPTable(1);
            cn.setWidthPercentage(100);

            cn.addCell(getCell( company.getCompanyName(), PdfPCell.ALIGN_CENTER));

            cn.addCell(getCell( company.getAddressLine1() ,PdfPCell.ALIGN_CENTER));
            cn.addCell(getCell(  company.getAddressLine2(),PdfPCell.ALIGN_CENTER));
            cn.addCell(getCell( company.getEMailId(),PdfPCell.ALIGN_CENTER));
            cn.addCell(getCell(company.getTelephoneNo(),PdfPCell.ALIGN_CENTER));
            cn.addCell(getCell( company.getGSTNo(),PdfPCell.ALIGN_CENTER));
            cn.addCell(getCell("_____________________________________________________________________________",PdfPCell.ALIGN_CENTER));
            document.add(cn);




            document.add(new Paragraph("Bill Id :"+Store.getInstance().lastbill.getId()));
            document.add(new Paragraph("Bill Date :"+Store.getInstance().lastbill.getDatetime()));
            document.add(new Paragraph("Payment Mode :"+DashboardActivity.paymentModeValue));
            document.add(new Paragraph("Table Details :"+Store.getInstance().lastbill.getTableDetails()));





            PdfPTable cn1 = new PdfPTable(1);
            cn1.setWidthPercentage(100);
            cn1.addCell(getCell("_____________________________________________________________________________", PdfPCell.ALIGN_LEFT));

            PdfPTable cn2 = new PdfPTable(1);
            cn2.setWidthPercentage(100);
            cn2.addCell(getCell("                   ", PdfPCell.ALIGN_LEFT));
            //  cn2.addCell(getCell(DashboardActivity.currentSaleType, PdfPCell.ALIGN_CENTER));

            document.add(cn2);

            PdfPTable pm = new PdfPTable(2);
            pm.setWidthPercentage(100);

            //  pm.addCell(getCell("Sale/Sale Order/Sale Return: "+ DashboardActivity.currentSaleType,PdfPTable.ALIGN_LEFT));
            pm.addCell(getCell("Payment Mode: " + DashboardActivity.paymentModeValue, PdfPTable.ALIGN_RIGHT));
            document.add(pm);

            PdfPTable cn3 = new PdfPTable(1);
            cn3.setWidthPercentage(100);
            cn3.addCell(getCell("Item Details", PdfPCell.ALIGN_CENTER));
            cn2.addCell(getCell("_____________________________________________________________________________", PdfPCell.ALIGN_LEFT));
            document.add(cn3);


            PdfPTable line = new PdfPTable(1);
            line.setWidthPercentage(100);
            line.addCell(getCell("_____________________________________________________________________________", PdfPCell.ALIGN_LEFT));
            document.add(line);


            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);

            table.addCell(getCell("S.No", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("ID", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("Name", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("Qty", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("UOM", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("Price", PdfPCell.ALIGN_LEFT));

            table.addCell(getCell("Amount", PdfPCell.ALIGN_RIGHT));

            document.add(table);
            PdfPTable line1 = new PdfPTable(1);
            line1.setWidthPercentage(100);
            line1.addCell(getCell("_____________________________________________________________________________", PdfPCell.ALIGN_LEFT));
            document.add(line1);

            for (int i = 0; i < productList.size(); i++) {
                PdfPTable table1 = new PdfPTable(7);
                table1.setWidthPercentage(100);

                table1.addCell(getCell(String.valueOf(i + 1), PdfPCell.ALIGN_LEFT));
                table1.addCell(getCell(String.valueOf(productList.get(i).getId()), PdfPCell.ALIGN_LEFT));
                table1.addCell(getCell(productList.get(i).getName(), PdfPCell.ALIGN_LEFT));
                table1.addCell(getCell(String.valueOf(productList.get(i).getQuantity()), PdfPCell.ALIGN_LEFT));
                table1.addCell(getCell(String.valueOf(productList.get(i).getUom()), PdfPCell.ALIGN_LEFT));
                table1.addCell(getCell(String.valueOf(String.format("%.2f", Float.parseFloat(productList.get(i).getPrice()))), PdfPCell.ALIGN_LEFT));

                table1.addCell(getCell(String.valueOf(String.format("%.2f", productList.get(i).getCalculatedPrice())), PdfPCell.ALIGN_RIGHT));

                document.add(table1);

            }


            String gstSpace = "";

            String st = String.valueOf(DashboardActivity.subTotal.getText().toString());
            int subTotalLength = st.length();


            String gstx = String.valueOf(DashboardActivity.gst.toString());
            int gstLength = gstx.length();


            int c = subTotalLength - gstLength;

            for (int f = 0; f < c; f++) {
                gstSpace = gstSpace + " ";
            }

            double s = Double.parseDouble(DashboardActivity.subTotal.getText().toString());

            double rrm = 0;
            if (TextUtils.isEmpty(DashboardActivity.fromCustomer.getText().toString())) {
                rrm = 0;
            } else {
                rrm = Double.parseDouble(DashboardActivity.fromCustomer.getText().toString());
            }

            double brm = Double.parseDouble(DashboardActivity.balance.getText().toString());

            String mgt = String.valueOf(String.format("%.2f", s));
            String ra = String.valueOf(String.format("%.2f", rrm));
            String ba = String.valueOf(String.format("%.2f", brm));

            int mgtL = mgt.length();
            int raL = ra.length();
            int baL = ba.length();

            String mgSpace = "";
            String raSpace = "";
            String baSpace = "";


            int x = 0;
            int y = 0;
            if (mgtL > raL) {
                x = mgtL - raL;
                y = mgtL - baL;
                for (int i = 0; i < x; i++) {
                    raSpace = raSpace + " ";

                }
                for (int i = 0; i < y; i++) {
                    baSpace = baSpace + " ";

                }
            } else if (raL > mgtL) {
                x = raL - mgtL;
                y = raL - baL;
                for (int i = 0; i < x; i++) {
                    mgSpace = mgSpace + " ";
                }
                for (int i = 0; i < y; i++) {
                    baSpace = baSpace + " ";

                }
            }


            PdfPTable line3 = new PdfPTable(1);
            line3.setWidthPercentage(100);
            line3.addCell(getCell("_____________________________________________________________________________", PdfPCell.ALIGN_LEFT));
            document.add(line3);
            document.add(new Paragraph("                                                  "));

            PdfPTable stx = new PdfPTable(1);
            stx.setWidthPercentage(97);

            stx.addCell(getCell("Sub Total RM " + DashboardActivity.subTotal.getText().toString() + "  ", PdfPCell.ALIGN_RIGHT));
            stx.addCell(getCell("  ", PdfPCell.ALIGN_LEFT));
            document.add(stx);
            PdfPTable gst = new PdfPTable(1);
            gst.setWidthPercentage(97);

            gst.addCell(getCell("GST RM " + gstSpace + DashboardActivity.gst.getText().toString() + "  ", PdfPCell.ALIGN_RIGHT));
            document.add(gst);


            PdfPTable gt = new PdfPTable(1);
            gt.setWidthPercentage(97);


            double disAmnt = 0;
            if (TextUtils.isEmpty(DashboardActivity.discount.getText().toString())) {
                disAmnt = 0;
            } else {
                disAmnt = Double.parseDouble(DashboardActivity.discount.getText().toString());
            }

            gt.addCell(getCell("  ", PdfPCell.ALIGN_LEFT));
            gt.addCell(getCell("Discount ( " + disAmnt + ") % " + gstSpace + "RM " + DashboardActivity.discountValue.getText().toString() + "  ", PdfPCell.ALIGN_RIGHT));

            gt.addCell(getCell("      ", PdfPCell.ALIGN_RIGHT));

            gt.addCell(getCell("Grand Total RM " + mgSpace + DashboardActivity.grandTotal.getText().toString() + "  ", PdfPCell.ALIGN_RIGHT));
            gt.addCell(getCell("____________________________________________________________________________", PdfPCell.ALIGN_LEFT));

            gt.addCell(getCell("  ", PdfPCell.ALIGN_RIGHT));
            System.out.println("Pay Mode ====" + DashboardActivity.paymentModeValue);
            if (DashboardActivity.paymentModeValue.contains("card") || DashboardActivity.paymentModeValue.contains("cheque")) {
            } else {
                if (TextUtils.isEmpty(DashboardActivity.fromCustomer.getText().toString())) {
                    gt.addCell(getCell("Received RM " + raSpace + String.format("%.2f", Double.parseDouble("0")) + "  ", PdfPCell.ALIGN_RIGHT));
                } else {
                    gt.addCell(getCell("Received RM " + raSpace + String.format("%.2f", Double.parseDouble(DashboardActivity.fromCustomer.getText().toString())) + "  ", PdfPCell.ALIGN_RIGHT));
                }


                gt.addCell(getCell("  ", PdfPCell.ALIGN_LEFT));


                gt.addCell(getCell("Balance RM " + baSpace + DashboardActivity.balance.getText().toString() + "  ", PdfPCell.ALIGN_RIGHT));
                gt.addCell(getCell("___________________________________________________________________________", PdfPCell.ALIGN_LEFT));

            }
            gt.addCell(getCell("  ", PdfPCell.ALIGN_LEFT));
            //  gt.addCell(getCell("Dealer Name = " + Store.getInstance().dealerName, PdfPCell.ALIGN_LEFT));

            document.add(gt);


            PdfPTable thank1 = new PdfPTable(1);
            thank1.setWidthPercentage(100);
            thank1.addCell(getCell(" ", PdfPCell.ALIGN_CENTER));
            thank1.addCell(getCell("***Thank You***", PdfPCell.ALIGN_CENTER));
            document.add(new Paragraph("Waiter Name :"+Store.getInstance().lastbill.getWaiter()));
            document.add(new Paragraph("_____________________________________________________________________________"));
            document.add(thank1);


            // close document
            document.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressLint("DefaultLocale")
    public static Boolean write_to_report(Context context, String fname, ArrayList<Bill> bills, HashMap<String, String> params) {
        try {

            Company company = new Company();


            BizUtils bizUtils = new BizUtils();

            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "reports");
            boolean success = true;


            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {
                // Do something on success
                Log.d("FOLDER CREATED", "TRUE");
            } else {
                // Do something else on failure
                Log.d("FOLDER CREATED", "FALSE");
            }


            folder.getAbsolutePath();
            Log.d("ABS PATH", folder.getAbsolutePath());
            //Create file path for Pdf
            String fpath = folder.getAbsolutePath() + "/" + fname + ".pdf";
            File file = new File(fpath);
            System.out.println("fpath = " + fpath);

            if (!file.exists()) {
                file.createNewFile();
            }
            // To customise the text of the pdf
            // we can use FontFamily
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN,
                    12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN,
                    12);
            // create an instance of itext document
            Document document = new Document();

            PdfWriter.getInstance(document,
                    new FileOutputStream(file.getAbsoluteFile()));
            document.open();

            try {
                // get receipt stream
                InputStream ims = context.getAssets().open("log_trans.png");
                Bitmap bmp = BitmapFactory.decodeStream(ims);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());


                PdfPTable logo = new PdfPTable(1);
                logo.setWidthPercentage(15);
                image.setAlignment(Image.ALIGN_CENTER);
                image.setBorder(Image.NO_BORDER);
                logo.addCell(image);
                document.add(logo);


            } catch (IOException ex) {
                ex.getStackTrace();
            }

            //using add method in document to insert a paragraph
            PdfPTable thank = new PdfPTable(1);
            thank.setWidthPercentage(100);
            thank.addCell(getCell(context.getString(R.string.app_name), PdfPCell.ALIGN_CENTER));
            document.add(thank);
            document.add(new Paragraph("_____________________________________________________________________________"));
            document.add(new Paragraph("                                                                             "));

            PdfPTable cn = new PdfPTable(1);
            cn.setWidthPercentage(100);

            cn.addCell(getCell( company.getCompanyName(), PdfPCell.ALIGN_CENTER));

            cn.addCell(getCell( company.getAddressLine1() ,PdfPCell.ALIGN_CENTER));
            cn.addCell(getCell(  company.getAddressLine2(),PdfPCell.ALIGN_CENTER));
            cn.addCell(getCell( company.getEMailId(),PdfPCell.ALIGN_CENTER));
            cn.addCell(getCell(company.getTelephoneNo(),PdfPCell.ALIGN_CENTER));
            cn.addCell(getCell( company.getGSTNo(),PdfPCell.ALIGN_CENTER));
            cn.addCell(getCell("_____________________________________________________________________________",PdfPCell.ALIGN_CENTER));
            document.add(cn);

            PdfPTable cnx = new PdfPTable(1);
            cnx .setWidthPercentage(100);

            cnx .addCell(getCell("   ", PdfPCell.ALIGN_LEFT));
            cnx .addCell(getCell("Report from datetime :"+params.get("fromdate"), PdfPCell.ALIGN_LEFT));
            cnx .addCell(getCell("Report to datetime :"+params.get("todate"), PdfPCell.ALIGN_LEFT));




            document.add(cnx);





            PdfPTable cn1 = new PdfPTable(1);
            cn1.setWidthPercentage(100);
            cn1.addCell(getCell("_____________________________________________________________________________", PdfPCell.ALIGN_LEFT));

            PdfPTable cn2 = new PdfPTable(1);
            cn2.setWidthPercentage(100);
            cn2.addCell(getCell("                   ", PdfPCell.ALIGN_LEFT));
            //  cn2.addCell(getCell(DashboardActivity.currentSaleType, PdfPCell.ALIGN_CENTER));

            document.add(cn2);



            PdfPTable cn3 = new PdfPTable(1);
            cn3.setWidthPercentage(100);
            cn3.addCell(getCell("Item Details", PdfPCell.ALIGN_CENTER));
            cn2.addCell(getCell("_____________________________________________________________________________", PdfPCell.ALIGN_LEFT));
            document.add(cn3);


            PdfPTable line = new PdfPTable(1);
            line.setWidthPercentage(100);
            line.addCell(getCell("_____________________________________________________________________________", PdfPCell.ALIGN_LEFT));
            document.add(line);


            PdfPTable table = new PdfPTable(10);
            table.setWidthPercentage(100);

            table.setHeadersInEvent(true);
            table.addCell(getCell("S.No", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("ID", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("Name", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("Qty", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("UOM", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("Price", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("Amount", PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell("GST", PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell("Discount", PdfPCell.ALIGN_RIGHT));
            table.addCell(getCell("Total", PdfPCell.ALIGN_RIGHT));


            document.add(table);

            PdfPTable linex = new PdfPTable(1);
            linex.setWidthPercentage(100);
            linex.addCell(getCell("_____________________________________________________________________________", PdfPCell.ALIGN_LEFT));
            document.add(linex);


            double total = 0;
            int c = 1;
            ArrayList<Item> productList = new ArrayList<>();
            for(int x=0;x<bills.size();x++) {
                productList = new ArrayList<>();
                productList.addAll(bills.get(x).getItemList());


                for (int i = 0; i < productList.size(); i++) {
                    PdfPTable table1 = new PdfPTable(10);
                    table1.setWidthPercentage(100);

                    table1.addCell(getCell(String.valueOf(c), PdfPCell.ALIGN_LEFT));
                    table1.addCell(getCell(String.valueOf(productList.get(i).getId()), PdfPCell.ALIGN_LEFT));
                    table1.addCell(getCell(productList.get(i).getName(), PdfPCell.ALIGN_LEFT));
                    table1.addCell(getCell(String.valueOf(productList.get(i).getQuantity()), PdfPCell.ALIGN_LEFT));
                    table1.addCell(getCell(String.valueOf(productList.get(i).getUom()), PdfPCell.ALIGN_LEFT));
                    table1.addCell(getCell(String.valueOf(String.format("%.2f", Float.parseFloat(productList.get(i).getPrice()))), PdfPCell.ALIGN_LEFT));
                    table1.addCell(getCell(String.valueOf(String.format("%.2f", productList.get(i).getCalculatedPrice())), PdfPCell.ALIGN_RIGHT));

                    double gst = Double.parseDouble(productList.get(i).getPrice()) * 0.06;
                    double discount =  Double.parseDouble(productList.get(i).getPrice()) *  bills.get(x).getDiscountPercentage();
                    double Gtotal = Double.parseDouble(productList.get(i).getPrice()) - discount;

                    println("---"+gst);

                    table1.addCell(getCell(String.valueOf(String.format("%.2f",gst)), PdfPCell.ALIGN_RIGHT));
                    table1.addCell(getCell(String.valueOf(String.format("%.2f",discount)), PdfPCell.ALIGN_RIGHT));
                    table1.addCell(getCell(String.valueOf(String.format("%.2f", Gtotal)), PdfPCell.ALIGN_RIGHT));

                    document.add(table1);

                    total = total +Gtotal;

                    c++;

                }
            }




            PdfPTable gt = new PdfPTable(1);
            gt.setWidthPercentage(97);

            gt.addCell(getCell("___________________________________________________________________________", PdfPCell.ALIGN_LEFT));
            gt.addCell(getCell("                                                                          ", PdfPCell.ALIGN_LEFT));
            gt.addCell(getCell("Grand Total RM " + total + "  ", PdfPCell.ALIGN_RIGHT));
            document.add(gt);




            document.add(new Paragraph("_____________________________________________________________________________"));
            PdfPTable thank1 = new PdfPTable(1);
            thank1.setWidthPercentage(100);
            thank1.addCell(getCell(" ", PdfPCell.ALIGN_CENTER));
            thank1.addCell(getCell("Total Items = "+(c-1), PdfPCell.ALIGN_RIGHT));
            thank1.addCell(getCell("***Thank You***", PdfPCell.ALIGN_CENTER));
            document.add(thank1);


            // close document
            document.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        }
    }





    public static PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public static void showMenu(final Context context) {



        ImageView home, bill, logout, reports, pdf,table,admin,kitchen,mealtiming;
        TextView homeLabel, billLabel, logoutLabel, reportsLabel, pdfLabel,tableLabel,adminLabel,kitchenTools,mealtimingText;
        final Dialog dialog = new Dialog(context);
        Calligrapher calligrapher = new Calligrapher(context);
        calligrapher.setFont((Activity) context, "fonts/Quicksand-BoldItalic.otf", true);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.show_menu);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        home = dialog.findViewById(R.id.home);
        bill = dialog.findViewById(R.id.bill);
        logout = dialog.findViewById(R.id.logout);
        reports = dialog.findViewById(R.id.reports);
        pdf = dialog.findViewById(R.id.pdf);
        table = dialog.findViewById(R.id.table);
        admin = dialog.findViewById(R.id.admin);
        kitchen = dialog.findViewById(R.id.kitchen);
        mealtiming = dialog.findViewById(R.id.meal_timing);


        homeLabel = dialog.findViewById(R.id.home_label);
        pdfLabel = dialog.findViewById(R.id.pdf_label);
        billLabel = dialog.findViewById(R.id.bills_label);
        logoutLabel = dialog.findViewById(R.id.logout_label);
        reportsLabel = dialog.findViewById(R.id.reports_label);
        tableLabel = dialog.findViewById(R.id.table_label);
        adminLabel =  dialog.findViewById(R.id.admin_label);
        kitchenTools = dialog.findViewById(R.id.kitchen_tools);
        mealtimingText = dialog.findViewById(R.id.meal_timimg_text);


        tableLabel.setText(String.valueOf(Store.getInstance().configuration.getTableLabel()+" View"));
        kitchenTools.setText(String.valueOf(Store.getInstance().configuration.getKotLabel()+" Status"));

        if(Store.getInstance().configuration.getConfName().toLowerCase().contains("car wash"))
        {
            table.setImageResource(R.drawable.garage);
            kitchen.setImageResource(R.drawable.wrench);
        }
        if(Store.getInstance().configuration.getConfName().toLowerCase().contains("parlour"))
        {
            table.setImageResource(R.drawable.woman);
            kitchen.setImageResource(R.drawable.hair_cut_tool);
        }



        if(Boolean.valueOf(Store.getInstance().getProperty(context).getProperty("isTableModel")))
        {
            tableLabel.setVisibility(View.VISIBLE);
            table.setVisibility(View.VISIBLE);


        }
        else
        {
            tableLabel.setVisibility(View.GONE);
            table.setVisibility(View.GONE);
        }

        if(Boolean.valueOf(Store.getInstance().getProperty(context).getProperty("isKot")))
        {
            kitchenTools.setVisibility(View.VISIBLE);
            kitchen.setVisibility(View.VISIBLE);


        }
        else
        {
            kitchenTools.setVisibility(View.GONE);
            kitchen.setVisibility(View.GONE);
        }


        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, BillActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, InvoiceListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReportsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);


            }
        });
        reportsLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ReportsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);


            }
        });
        billLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, BillActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        pdfLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, InvoiceListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        logoutLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);


            }
        });

        homeLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        table.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, TableActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        tableLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, TableActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
    }
});

        adminLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, AdminActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, AdminActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

        kitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, KOTViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        kitchenTools.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, KOTViewActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

        mealtiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MealTimingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });
        mealtimingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MealTimingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
            }
        });

            mealtiming.setVisibility(View.GONE);
            mealtimingText.setVisibility(View.GONE);


        int count = 0;
        for(int i=0;i<Store.getInstance().addedItemList.size();i++) {
            System.out.println("----Saved---" + Store.getInstance().addedItemList.get(i).isSaved());
            if (!(Store.getInstance().addedItemList.get(i).isSaved())) {
                 Toast.makeText(context, "Please save or remove newly added items", Toast.LENGTH_SHORT).show();

            } else {
                count++;
            }
        }
        if(count == Store.getInstance().addedItemList.size())
        {
            dialog.show();
        }



    }
    public  static  void addCustomActionBar(final Context context, String s)
    {


        ImageButton bills,menu;




        ( (AppCompatActivity)context ).getSupportActionBar().setTitle("Tables");
        ( (AppCompatActivity)context ).getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ( (AppCompatActivity)context ).getSupportActionBar().setDisplayShowCustomEnabled(true);
        ( (AppCompatActivity)context ).getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        View view = ( (AppCompatActivity)context ).getSupportActionBar().getCustomView();

        Calligrapher calligrapher = new Calligrapher(context);
        calligrapher.setFont( ( (AppCompatActivity)context ), "fonts/Quicksand-BoldItalic.otf", true);


        TextView title = view.findViewById(R.id.title);
        title.setText(String.valueOf(s));

         bills =  view.findViewById(R.id.bills);
        menu = (ImageButton)view.findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BizUtils.showMenu(context);

            }
        });
        bills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BizUtils.showBillListDialog(context);

            }
        });
    }

    public static void showBillListDialog(final Context context) {


        final Dialog billListDialog = new Dialog(context);
        billListDialog.setContentView(R.layout.bill_list);
        final ListView billListView = billListDialog.findViewById(R.id.listview);


        final HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("waiterId", String.valueOf(Store.getInstance().user.getId()));
        hm.put("type","openlist");


        Bill.OnTaskCompleted onTaskCompleted = new Bill.OnTaskCompleted() {
            @Override
            public void onTaskCompleted() {

                println("Loading Bill list--");
                BillListAdapter billListAdapter = new BillListAdapter(context, Store.getInstance().billList,billListDialog);
                billListView.setAdapter(billListAdapter);

                billListDialog.show();

            }
        };
        Bill.loadData(onTaskCompleted,hm);




    }


    class GetLastBill extends AsyncTask<Void, Void, Boolean> {
        HashMap<String, String> params;
        String url;
        String jsonResult;
        Long id;
        Context context;


        public GetLastBill(Context context, HashMap<String, String> params) {
            super();

            url = "bill/getLastBill";
            jsonResult = null;

            this.context = context;
            this.params = params;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            params.put("id", String.valueOf(id));
            System.out.println("waiter id " + Store.getInstance().user.getId());
            params.put("waiter_id", String.valueOf(Store.getInstance().user.getId()));


        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            jsonResult = HttpHandler.makeServiceCall(this.url, params);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (jsonResult != null) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();


                Type collectionType = new TypeToken<Bill>() {
                }.getType();

                Bill bill = gson.fromJson(jsonResult, collectionType);


                Store.getInstance().lastbill  = (Bill) bill;

                System.out.println("Bill json:"+jsonResult);


            }


        }
    }
    public void getTableList(Context context)
    {

         new TableList(context).execute();
    }
}

    class TableList extends AsyncTask<Void, Void, Boolean>
    {

        String url;
        String jsonResult;
        Long id;
        Context context;


        public TableList(Context context) {
            super();

            url = "bizTable/toList";
            jsonResult = null;
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();




        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            jsonResult = HttpHandler.makeServiceCall(this.url,null);
            return true;
        }
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(jsonResult!=null)
            {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();


                Type collectionType = new TypeToken<Collection<Table>>() {
                }.getType();

                Collection<Table> tables = gson.fromJson(jsonResult, collectionType);


                Store.getInstance().tableList = (ArrayList<Table>) tables;

                System.out.println("item res :"+jsonResult);








            }
        }








}
