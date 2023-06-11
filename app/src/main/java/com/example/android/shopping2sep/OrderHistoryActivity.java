package com.example.android.shopping2sep;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.shopping2sep.Models.CartProductModel;
import com.example.android.shopping2sep.Models.OrderModel;
import com.example.android.shopping2sep.adapters.CartListAdapter;
import com.example.android.shopping2sep.adapters.OrderAdapter;
import com.example.android.shopping2sep.library.LoadingDialog;
import com.example.android.shopping2sep.library.Prefs;
import com.example.android.shopping2sep.library.VolleyApiListener;
import com.example.android.shopping2sep.library.VolleyApiRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderHistoryActivity extends AppCompatActivity implements VolleyApiListener {
    public static final String TAG = OrderHistoryActivity.class.getSimpleName();

    private LoadingDialog loadingDialog;

    private String ty_get_ordr="getUserOrdrHist", fn_get_ordr = "get_user_ordr_hist.php";
    private OrderAdapter adapter;
    private ArrayList<OrderModel> order_mdl_list;

    RecyclerView ordr_hist_rv;

    public OrderHistoryActivity() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.logo_tb);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingDialog = new LoadingDialog( this );

        getOrderHistoryItems();
    }

    private void getOrderHistoryItems() {
        Log.d(TAG, "getCartDetails()");
        loadingDialog.show();
        HashMap<String, String> params = new HashMap<>();
        Prefs.getInstance().loadPrefs(this);
        params.put("email", Prefs.getInstance().emailId);
        params.put("action", ty_get_ordr);
        new VolleyApiRequest(OrderHistoryActivity.this, OrderHistoryActivity.this, ty_get_ordr,
                fn_get_ordr, params);
    }

    @Override
    public void onSuccessJson(String response, String type) {
        Log.e(TAG, "onSuccessJson() response: "+response+ " type: "+type);
        loadingDialog.hide();

        if(type.equalsIgnoreCase(ty_get_ordr)){
            try {
                JSONObject obj = new JSONObject( response );
                if (obj.getString( "status" ).equals( "success" )) {

                    JSONObject jsonObject = new JSONObject( response );
                    Log.d(TAG, "jsonObject : "+jsonObject.toString());

                    order_mdl_list = new Gson().fromJson(jsonObject.getJSONArray("cart_details").toString(), new TypeToken<ArrayList<CartProductModel>>(){}.getType());
                    Log.d(TAG, "productDetails ArrayList : "+ order_mdl_list.toString());
                    displayOrderHistory();
                } else {
                    if (obj.has( "error_msg" )) {
                        Log.d(TAG, obj.getString("error_msg"));
                        Toast.makeText(OrderHistoryActivity.this, obj.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void displayOrderHistory() {
        Log.d(TAG, "displayOrderHistory() ");
        adapter = new OrderAdapter( OrderHistoryActivity.this, order_mdl_list );
        ordr_hist_rv.setAdapter( adapter );
    }

    @Override
    public void onFailureJson(int responseCode, String responseMessage) {
        loadingDialog.hide();
        Log.e(TAG, "onFailureJson()" + responseMessage);
    }
}