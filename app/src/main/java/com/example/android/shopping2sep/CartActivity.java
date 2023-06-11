package com.example.android.shopping2sep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.shopping2sep.Models.CartProductModel;
import com.example.android.shopping2sep.adapters.CartListAdapter;
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

public class CartActivity extends AppCompatActivity implements VolleyApiListener {
    public static final String TAG = CartActivity.class.getSimpleName();
    LoadingDialog loadingDialog;

    RecyclerView cart_rv;
    TextView subTot_tv, del_ch_tv, tot_bill_tv;

    TextView cart_count;
    ImageView cart_iv;

    private String fn_cart_dets = "user_cart_dets", ty_cart_dets = "userCartDets";

    private ArrayList<CartProductModel> cart_dets_ls;
    private LinearLayoutManager lLayout;
    Button checkout_btn;
    private String ty_add_order = "addOrder" , fn_place_order = "place_order.php";
    CartListAdapter adapter;

    public CartActivity(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.logo_tb);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingDialog = new LoadingDialog( this );

//        cart_iv = (ImageView) findViewById( R.id.ic_cart );
//        cart_count = (TextView) findViewById( R.id.cart_count );
//
//        cart_iv.setVisibility( View.GONE );
//        cart_count.setVisibility(View.GONE);

        subTot_tv = (TextView) findViewById(R.id.subtot_ca_tv);
        del_ch_tv = (TextView) findViewById(R.id.delch_ca_tv);
        tot_bill_tv = (TextView ) findViewById(R.id.totAmt_ca_tv);
        checkout_btn = (Button) findViewById(R.id.checkout_cart_btn);
        checkout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                checkout_btn.setClickable(false);
                add_order();
            }
        });

        lLayout = new LinearLayoutManager( CartActivity.this );
        lLayout.setOrientation(LinearLayoutManager.VERTICAL);
//        list.setAdapter( adapter );

        cart_rv = (RecyclerView) findViewById(R.id.cart_rv);
        cart_rv.setLayoutManager(lLayout );
        cart_rv.setHasFixedSize( true );
        getCartDetails();

    }

    private void add_order() {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", Prefs.getInstance().emailId);
        params.put("action", ty_add_order);
        new VolleyApiRequest(CartActivity.this, CartActivity.this, ty_add_order,
                fn_place_order, params);
    }

    private void getCartDetails() {
        Log.d(TAG, "getCartDetails()");
            loadingDialog.show();
            HashMap<String, String> params = new HashMap<>();
            Prefs.getInstance().loadPrefs(this);
            params.put("email", Prefs.getInstance().emailId);
            params.put("action", ty_cart_dets);
            new VolleyApiRequest(CartActivity.this, CartActivity.this, ty_cart_dets,
                    fn_cart_dets, params);
    }

    @Override
    public void onSuccessJson(String response, String type) {
        Log.e(TAG, "onSuccessJson() response: "+response+ " type: "+type);
        loadingDialog.hide();

        if(type.equalsIgnoreCase(ty_cart_dets)){
            try {
                JSONObject obj = new JSONObject( response );
                if (obj.getString( "status" ).equals( "success" )) {

                    JSONObject jsonObject = new JSONObject( response );
                    Log.d(TAG, "jsonObject : "+jsonObject.toString());

                    cart_dets_ls = new Gson().fromJson(jsonObject.getJSONArray("cart_details").toString(), new TypeToken<ArrayList<CartProductModel>>(){}.getType());
                    Log.d(TAG, "productDetails ArrayList : "+cart_dets_ls.toString());
                    subTot_tv.setText( "Rs "+jsonObject.getDouble("sub_tot")+"");
                    display_cart_dets();
                } else {
                    if (obj.has( "error_msg" )) {
                        Log.d(TAG, obj.getString("error_msg"));
                        Toast.makeText(CartActivity.this, obj.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(type.equalsIgnoreCase(ty_add_order)){
            checkout_btn.setClickable(true);
            try {
                JSONObject obj = new JSONObject(response);
                if (obj.getString("status").equals("success")) {
                    Log.d(TAG, "order_id : "+ obj.getString("order_id"));
                    Prefs.getInstance().order_id = obj.getString("order_id");
                    Prefs.getInstance().savePrefs(getApplicationContext());
                    Intent intent = new Intent(CartActivity.this, ManageAddressActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    if (obj.has( "error_msg" )) {
                        Log.d(TAG, obj.getString("error_msg"));
                        Toast.makeText(CartActivity.this, obj.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void display_cart_dets() {
        Log.d(TAG, "display_cart_dets() ");
        adapter = new CartListAdapter( CartActivity.this, cart_dets_ls );
        cart_rv.setAdapter( adapter );
    }

    @Override
    public void onFailureJson(int responseCode, String responseMessage) {
        checkout_btn.setClickable(true);
        loadingDialog.hide();
        Log.e(TAG, "onFailureJson()" + responseMessage);
    }
}
