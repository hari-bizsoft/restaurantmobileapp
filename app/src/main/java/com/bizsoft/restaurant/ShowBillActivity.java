package com.bizsoft.restaurant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.bizsoft.restaurant.adapters.AddedItemListAdapter;
import com.bizsoft.restaurant.adapters.BillItemListAdapter;
import com.bizsoft.restaurant.adapters.ItemListAdapter;
import com.bizsoft.restaurant.dataobjects.Bill;
import com.bizsoft.restaurant.dataobjects.Store;
import com.bizsoft.restaurant.service.BizUtils;

import me.anwarshahriar.calligrapher.Calligrapher;

public class ShowBillActivity extends AppCompatActivity {

    TextView billID,paymentMode;
    TextView subTotal,gst,grandTotal;
    TextView discount,frmCustomer,balance;
    ListView listview;
    TextView dated;
    TextView waiterName;
    private BillItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bill);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "fonts/Quicksand-BoldItalic.otf", true);

        getSupportActionBar().setTitle("Bill ID :"+Store.getInstance().currentShowBill.getId());
        billID = findViewById(R.id.bill_id);
        paymentMode = findViewById(R.id.payment_mode);
        subTotal = findViewById(R.id.sub_total);
        gst= findViewById(R.id.gst);
        grandTotal = findViewById(R.id.grand_total);
        discount = findViewById(R.id.discount);
        frmCustomer = findViewById(R.id.received_rm);
        balance = findViewById(R.id.balance_rm);
        dated = findViewById(R.id.date);
        waiterName = findViewById(R.id.waiter_name);

        BizUtils.addCustomActionBar(ShowBillActivity.this,"Bill View ");



        billID.setText(String.valueOf(Store.getInstance().currentShowBill.getId()));
        paymentMode.setText(String.valueOf(Store.getInstance().currentShowBill.getPaymentMode()));
        subTotal.setText(String.valueOf("RM "+String.format("%.2f",Store.getInstance().currentShowBill.getSubTotal())));
        gst.setText(String.valueOf("RM "+String.format("%.2f",Store.getInstance().currentShowBill.getGst())));
        grandTotal.setText(String.valueOf("RM "+String.format("%.2f",Store.getInstance().currentShowBill.getGrandTotal())));
        discount.setText(String.valueOf("RM "+String.format("%.2f",Store.getInstance().currentShowBill.getDiscountValue())));
        frmCustomer.setText(String.valueOf("RM "+String.format("%.2f",Store.getInstance().currentShowBill.getReceivedFromCustomer())));
        balance.setText(String.valueOf("RM "+String.format("%.2f",Store.getInstance().currentShowBill.getBalance())));
        dated.setText(String.valueOf(Store.getInstance().currentShowBill.getDatetime()));
        waiterName.setText(String.valueOf("Waiter Name: "+Store.getInstance().currentShowBill.getWaiter()));
        listview =findViewById(R.id.listview);


        for(int i=0;i<Store.getInstance().currentShowBill.getItemList().size();i++)
        {
            System.out.println("Test : "+Store.getInstance().currentShowBill.getItemList().get(i).getPrice());
            System.out.println("Q : "+Store.getInstance().currentShowBill.getItemList().get(i).getQuantity());
        }



        adapter = new BillItemListAdapter(ShowBillActivity.this,Store.getInstance().currentShowBill.getItemList());
        listview.setAdapter(adapter);


    }
}
