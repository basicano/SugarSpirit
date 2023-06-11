package com.example.android.shopping2sep;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.shopping2sep.library.LoadingDialog;
import com.example.android.shopping2sep.library.Prefs;
import com.example.android.shopping2sep.library.VolleyApiListener;
import com.example.android.shopping2sep.library.VolleyApiRequest;

import java.util.HashMap;

public class PaymentModeActivity extends AppCompatActivity implements VolleyApiListener {
    public static final String TAG = PaymentModeActivity.class.getSimpleName();

    RadioButton online_mode_rb;
    private LoadingDialog loadingDialog;
    private String ty_online_pay="payu_api", fn_online_pay="payu_api_connect.php";
    private String ty_add_ordr_pay_mode="add_ordr_pay_mode", fn_add_ordr_pay_mode="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_mode);

        Log.d(TAG, "onCreate()");
        loadingDialog = new LoadingDialog(PaymentModeActivity.this);

        online_mode_rb = (RadioButton) findViewById(R.id.online_mode);
        online_mode_rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrdrPayMode();
                callPayUApi();
            }
        });
    }

    private void addOrdrPayMode() {
        Log.d(TAG, "addOrdrPayMode()");
        loadingDialog.show();
        HashMap<String, String> params = new HashMap<>();
        Prefs.getInstance().loadPrefs(this);
        params.put("order_id", Prefs.getInstance().order_id);
        params.put("action", ty_add_ordr_pay_mode);
        new VolleyApiRequest(PaymentModeActivity.this, PaymentModeActivity.this, ty_add_ordr_pay_mode,
                fn_add_ordr_pay_mode , params);
    }

    private void callPayUApi() {
        Log.d(TAG, "callPayUApi()");
        loadingDialog.show();
//        getCityState
        HashMap<String, String> params = new HashMap<>();
        Prefs.getInstance().loadPrefs(this);
        params.put("order_id", Prefs.getInstance().order_id);
        params.put("action", ty_online_pay);
        new VolleyApiRequest(PaymentModeActivity.this, PaymentModeActivity.this, ty_online_pay,
                fn_online_pay , params);
    }

    @Override
    public void onSuccessJson(String response, String type) {
        if (type.equalsIgnoreCase(ty_online_pay)){

        }

    }

    @Override
    public void onFailureJson(int responseCode, String responseMessage) {

    }
}
