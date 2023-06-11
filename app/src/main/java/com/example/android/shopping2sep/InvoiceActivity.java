package com.example.android.shopping2sep;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.android.shopping2sep.library.Prefs;

public class InvoiceActivity extends AppCompatActivity  {
    private static final String TAG = InvoiceActivity.class.getSimpleName();
    TextView email_tv, order_dets_tv, tck_ordr_tv, order_id_tv;
    LinearLayout order_dets_ll;
    ImageView order_dets_iv;
    AppCompatButton cont_shop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        Prefs.getInstance().loadPrefs(this);
        email_tv = (TextView) findViewById(R.id.inv_email);
        email_tv.setText(Prefs.getInstance().emailId);

        order_id_tv = (TextView) findViewById(R.id.inv_order_id);
        email_tv.setText(Prefs.getInstance().order_id);

        order_dets_ll = (LinearLayout) findViewById(R.id.ordr_dets_ll);
        order_dets_tv = (TextView) findViewById(R.id.ordr_dets_tv);
        order_dets_iv = (ImageView) findViewById(R.id.ordr_dets_ic);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OnClickListener() for viewing Order details");
//                TODO View Order Summary/Details
            }
        };
        order_dets_ll.setOnClickListener(clickListener);
        order_dets_tv.setOnClickListener(clickListener);
        order_dets_iv.setOnClickListener(clickListener);

        tck_ordr_tv = (TextView) findViewById(R.id.inv_tck_ordr_tv);
        tck_ordr_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OnClickListener() for viewing Order details");
//                TODO Track Order
            }
        });

        cont_shop = (AppCompatButton) findViewById(R.id.inv_go2home);
        cont_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InvoiceActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }
}
