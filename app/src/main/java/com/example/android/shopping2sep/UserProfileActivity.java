package com.example.android.shopping2sep;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.shopping2sep.library.LoadingDialog;
import com.example.android.shopping2sep.library.Prefs;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {
    public final String TAG = UserProfileActivity.class.getSimpleName();
    private LoadingDialog loadingDialog;

    TextView u_name_tv, u_email_tv, u_mobile_tv, u_address_tv;

    TextView edit_info, mng_addr, view_pv_order, contact_us, logout;


    public UserProfileActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        loadingDialog = new LoadingDialog(UserProfileActivity.this);

        Prefs.getInstance().loadPrefs(UserProfileActivity.this);

        u_name_tv = (TextView) findViewById(R.id.user_name);
        u_name_tv.setText(Prefs.getInstance().name);
        u_email_tv = (TextView) findViewById(R.id.user_email);
        u_email_tv.setText(Prefs.getInstance().emailId);
        u_mobile_tv = (TextView) findViewById(R.id.user_mobile);
        u_mobile_tv.setText(Prefs.getInstance().mobile_no);
        u_address_tv = (TextView) findViewById(R.id.user_address);
        u_address_tv.setText(Prefs.getInstance().address);

        edit_info = (TextView) findViewById(R.id.edit_info);
        edit_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mng_addr = (TextView) findViewById(R.id.mng_addr);
        mng_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view_pv_order = (TextView) findViewById(R.id.view_pv_ordrs);
        view_pv_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, OrderHistoryActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        contact_us= (TextView) findViewById(R.id.contact_us);
        contact_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        logout= (TextView) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                action confirmation alert dialog
                Prefs.getInstance().is_logged_in  = false;
                Prefs.getInstance().savePrefs(getApplicationContext());
                Intent intent = new Intent(UserProfileActivity.this, SignUpIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    public void onClick(View v) {
        Log.d(TAG, "onNavigationItemSelected() --> "+ v.toString());

        int id = v.getId();
        switch (id){
            case R.id.edit_info: break;

            case R.id.mng_addr: break;

            case R.id.view_pv_ordrs: break;

            case R.id.contact_us: break;

            case R.id.logout:
                Prefs.getInstance().is_logged_in = false;
                Prefs.getInstance().savePrefs(UserProfileActivity.this);
                Intent intent = new Intent(UserProfileActivity.this, SignUpIn.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
