package com.example.android.shopping2sep;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.shopping2sep.Models.AddressModel;
import com.example.android.shopping2sep.adapters.AddressListAdapter;
import com.example.android.shopping2sep.library.LoadingDialog;
import com.example.android.shopping2sep.library.Prefs;
import com.example.android.shopping2sep.library.VolleyApiListener;
import com.example.android.shopping2sep.library.VolleyApiRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ManageAddressActivity extends AppCompatActivity implements VolleyApiListener {
    public static final String TAG = ManageAddressActivity.class.getSimpleName();
    private LoadingDialog loadingDialog;

    public static boolean addr_selected;
    public static AddressModel del_address;

    RecyclerView addr_rv;
    private ArrayList<AddressModel> addr_list;
    private LinearLayoutManager lLayout;

    CheckBox tnc_chkb;
    TextView read_tnc_tv, cart_count;
    ImageView cart_iv;

    AppCompatButton opt_new_addr;
    AppCompatButton  proceed_btn;

    AddressListAdapter adapter;

    private final String ty_add_dely_addr = "addDelyAddr", ty_get_dely_addr = "getDelyAddr", fn_mng_addr ="user_manage_addr.php";
    private String ty_add_ordr_addr = "addOrdrAddr" , fn_place_order  = "place_order.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mng_addr);
        Log.d(TAG, "onCreate()");
        loadingDialog = new LoadingDialog(ManageAddressActivity.this);
        initFragment();
    }

    private void initFragment() {
        Log.d(TAG, "initFragment()");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.logo_tb);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadingDialog = new LoadingDialog( this );

        cart_iv = (ImageView) findViewById( R.id.ic_cart );
        cart_count = (TextView) findViewById( R.id.cart_count );

        cart_iv.setVisibility( View.GONE );
        cart_count.setVisibility(View.GONE);

        tnc_chkb = (CheckBox) findViewById(R.id.tnc_chkb);
        tnc_chkb.setChecked(false);

        read_tnc_tv = (TextView) findViewById(R.id.tv_read_tnc) ;
        read_tnc_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManageAddressActivity.this,ViewTnC.class);
                startActivity(i);
            }
        });
        opt_new_addr = (AppCompatButton) findViewById(R.id.opt_new_addr);
        opt_new_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNewAddress();
            }
        });

        proceed_btn = (AppCompatButton) findViewById(R.id.proceed_btn);
        proceed_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addr_selected){
                    if (tnc_chkb.isChecked()){
                        add_ordr_dely_addr();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "User must agree to the tnc before proceeding", Toast.LENGTH_SHORT ).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Delivery address not selected", Toast.LENGTH_SHORT ).show();
                }

            }
        });


        lLayout = new LinearLayoutManager( ManageAddressActivity.this );
        lLayout.setOrientation(LinearLayoutManager.VERTICAL);
        addr_rv = (RecyclerView) findViewById(R.id.addr_rv);
        addr_rv.setLayoutManager(lLayout );
        addr_rv.setHasFixedSize( true );
//        addr_rv.setAdapter(adapter);
        getDeliveryAddr();
    }

    private void add_ordr_dely_addr() {
        Log.d(TAG, "getDelveryAddr()");
        loadingDialog.show();
        HashMap<String, String> params = new HashMap<>();
        Prefs.getInstance().loadPrefs(this);
        params.put("email", Prefs.getInstance().emailId);
        params.put("order_id", Prefs.getInstance().order_id);
        params.put("dely_addr_id", del_address.getSlno());
        params.put("action", ty_add_ordr_addr);
        new VolleyApiRequest(ManageAddressActivity.this, ManageAddressActivity.this, ty_add_ordr_addr,
                fn_place_order , params);
    }


    private void getNewAddress() {
        Log.d(TAG, "getNewAddress()");
        final AlertDialog.Builder builder = new AlertDialog.Builder(ManageAddressActivity.this);
        LayoutInflater inflater = LayoutInflater.from(ManageAddressActivity.this);
        View view = inflater.inflate(R.layout.alert_add_address, null);
        builder.setView(view);

        TextInputLayout txt_rcvr_name = (TextInputLayout) view.findViewById( R.id.txt_rcvr_name );
        TextInputLayout txt_mobile_no = (TextInputLayout) view.findViewById( R.id.txt_mobile_no );
        TextInputLayout txt_address = (TextInputLayout) view.findViewById( R.id.txt_address );
        TextInputLayout txt_pin_code = (TextInputLayout) view.findViewById( R.id.txt_pin_code );

        TextInputEditText input_rcvr_name = view.findViewById(R.id.input_rcvr_name);
        TextInputEditText input_mobile_no = view.findViewById(R.id.input_mobile_no);
        TextInputEditText input_address = view.findViewById(R.id.input_address);
        TextInputEditText input_pin_code = view.findViewById(R.id.input_pin_code);

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.add_addr_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = true;
                if (input_rcvr_name.getText().toString().trim() == null || input_rcvr_name.getText().toString().trim().length() == 0) {
                    txt_rcvr_name.setError( "Please Enter Receiver's name " );
                    result = false;
                } else {
//                    if (txt_rcvr_name.isErrorEnabled())
                    txt_rcvr_name.setError( null );
                }

                if (input_mobile_no.getText().toString().trim() == null || input_mobile_no.getText().toString().trim().length() == 0) {
                    txt_mobile_no.setError( "Please enter Receiver's mobile number" );
                    result = false;
                } else {
                    txt_mobile_no.setError( null );
                }

                if (input_address.getText().toString().trim() == null || input_address.getText().toString().trim().length() == 0) {
                    txt_address.setError( "Please enter the delivery address" );
                    result = false;
                }  else {
                    txt_address.setError( null );
                }

                if (input_pin_code.getText().toString().trim() == null || input_pin_code.getText().toString().trim().length() == 0) {
                    txt_pin_code.setError( "Please enter Pin Code");
                    result = false;
                }  else {
                    txt_pin_code.setError( null );
                }

                if(result){
                    loadingDialog.show();
//                    TODO connect to online resource api to calculate city and state
//                    TODO connect to GPS to find delivery location of user
                    HashMap<String, String > params = new HashMap<>();
                    params.put("email",  Prefs.getInstance().emailId);
                    params.put("rcvr_name", input_rcvr_name.getText().toString());
                    params.put("mob_no", input_mobile_no.getText().toString());
                    params.put("address", input_address.getText().toString());
                    params.put("pin_code", input_pin_code.getText().toString());
                    params.put("action", ty_add_dely_addr);
                    new VolleyApiRequest(ManageAddressActivity.this, ManageAddressActivity.this, ty_add_dely_addr, fn_mng_addr, params);
                }
                dialog.dismiss();
            }
        });
    }

    public void getDeliveryAddr() {
        Log.d(TAG, "getDeliveryAddr()");
        loadingDialog.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("email", Prefs.getInstance().emailId);
        params.put("action", ty_get_dely_addr);
        new VolleyApiRequest(ManageAddressActivity.this, ManageAddressActivity.this, ty_get_dely_addr,
                fn_mng_addr , params);
    }


    @Override
    public void onSuccessJson(String response, String type) {
        Log.e(TAG, "onSuccessJson() response: "+response+ " type: "+type);
        loadingDialog.hide();

        if(type.equalsIgnoreCase(ty_get_dely_addr)){
            try {
                JSONObject obj = new JSONObject( response );
                if (obj.getString( "status" ).equals( "success" )) {

                    JSONObject jsonObject = new JSONObject( response );
                    Log.d(TAG, "jsonObject : "+jsonObject.toString());

                    addr_list = new Gson().fromJson(jsonObject.getJSONArray("dely_addrs").toString(), new TypeToken<ArrayList<AddressModel>>(){}.getType());
                    Log.d(TAG, "addr_list ArrayList : "+addr_list.toString());

                    display_dely_addr();

                } else {
                    if (obj.has( "error_msg" )) {
                        Log.d(TAG, obj.getString("error_msg"));
                        Toast.makeText(ManageAddressActivity.this, obj.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(type.equalsIgnoreCase(ty_add_dely_addr)){
            try {
                JSONObject obj = new JSONObject( response );
                if (obj.getString( "status" ).equals( "success" )) {

                    getDeliveryAddr();
                    Toast.makeText(ManageAddressActivity.this, "Address added to the above list, Please select the delivery address from the list",Toast.LENGTH_LONG).show();

                } else {
                    if (obj.has( "error_msg" )) {
                        Log.d(TAG, obj.getString("error_msg"));
                        Toast.makeText(ManageAddressActivity.this, obj.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (type.equalsIgnoreCase(ty_add_ordr_addr)){
            try {
                JSONObject obj = new JSONObject( response );
                if (obj.getString( "status" ).equals( "success" )) {
                    Intent intent = new Intent(ManageAddressActivity.this, InvoiceActivity.class);
//                    Intent intent = new Intent(ManageAddressActivity.this, PaymentModeActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                else {
                    if (obj.has( "error_msg" )) {
                        Log.d(TAG, obj.getString("error_msg"));
                        Toast.makeText(ManageAddressActivity.this, obj.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void display_dely_addr() {
        Log.d(TAG, "display_dely_addr() ");
        adapter = new AddressListAdapter( ManageAddressActivity.this, addr_list );
        addr_rv.setAdapter( adapter );
    }

    @Override
    public void onFailureJson(int responseCode, String responseMessage) {
        loadingDialog.hide();
        Log.e(TAG, "onFailureJson()" + responseMessage);
    }
}
