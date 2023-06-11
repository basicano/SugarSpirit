package com.example.android.shopping2sep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.shopping2sep.Models.ProductDetailModel;
import com.example.android.shopping2sep.adapters.Col1ListAdapter;
import com.example.android.shopping2sep.library.LoadingDialog;
import com.example.android.shopping2sep.library.Prefs;
import com.example.android.shopping2sep.library.VolleyApiListener;
import com.example.android.shopping2sep.library.VolleyApiRequest;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, VolleyApiListener {
    final static String TAG = MainActivity.class.getSimpleName();

    LoadingDialog loadingDialog;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    RecyclerView rv_col1;
    private LinearLayoutManager lLayout;
    ArrayList<ProductDetailModel> product_details_ls;
    HashMap<String , String> cart_summary;

    private ImageView cart_iv;
    public static TextView cart_count;
    SearchView search_sv;

//    private ArrayList <Model_GroupDrawable> ImagesArray = new ArrayList <>();


    final String fn_get_col1 = "get_home_data.php", ty_col1 = "data_col1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingDialog = new LoadingDialog(MainActivity.this);

        cart_summary = new HashMap<String, String>();

        toolbar = (Toolbar) findViewById( R.id.toolbar );
        toolbar.setTitle("");
        toolbar.setLogo( R.mipmap.logo_tb );
        setSupportActionBar( toolbar );
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled( true );

        drawerLayout = (DrawerLayout) findViewById( R.id.drawer_layout );
        navigationView = (NavigationView) findViewById( R.id.navigation_view );
        navigationView.setNavigationItemSelectedListener( this );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle( this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer );
//        toggle.
        drawerLayout.addDrawerListener( toggle );
        toggle.syncState();

        search_sv = (SearchView) findViewById(R.id.search_sv);

        cart_iv = (ImageView) findViewById( R.id.ic_cart );
        cart_count = (TextView) findViewById( R.id.cart_count );

        cart_iv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MainActivity.this, CartActivity.class );
                startActivity( intent );
            }
        } );

        lLayout = new LinearLayoutManager( MainActivity.this );
//        lLayout = new GridLayoutManager( MainActivity.this, 2 );
        rv_col1 = (RecyclerView) findViewById( R.id.rv_collection1 );
        rv_col1.setLayoutManager(lLayout );
        rv_col1.setHasFixedSize( true );

        get_home_sc_products();
    }

    private void get_home_sc_products() {
        Log.d(TAG, "get_home_sc_products()");
        loadingDialog.show();
        Prefs.getInstance().loadPrefs(this);
        HashMap<String,String> params = new HashMap<>();
        params.put("action", ty_col1);
        params.put("email", Prefs.getInstance().emailId);
        new VolleyApiRequest(MainActivity.this, MainActivity.this, ty_col1,
                fn_get_col1,params );
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onNavigationItemSelected() --> "+ item.toString());

        int id = item.getItemId();
        switch (id) {
            case R.id.action_home:
                Intent main = new Intent(MainActivity.this, MainActivity.class);
                startActivity(main);
                break;
            case R.id.action_logout:

                break;
            case R.id.action_share:
//                Intent search = new Intent(MainActivity.this, Share.class);
//                startActivity(share);
                break;
            case R.id.action_profile:
                Intent profile = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(profile);
                break;
        }
        drawerLayout.closeDrawer( GravityCompat.START );
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen( GravityCompat.START )) {
            drawerLayout.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onSuccessJson(String response, String type) {
        Log.e(TAG, "onSuccessJson() response: "+response+ " type: "+type);
        loadingDialog.hide();

        if(type.equalsIgnoreCase(ty_col1)){
            try {
//                ImagesArray.clear();

                JSONObject jsonObj = new JSONObject( response );
                if (jsonObj.getString( "status" ).equals( "success" )) {

                    Log.d(TAG, "jsonObject : "+jsonObj.toString());

                    Prefs.getInstance().loadPrefs(MainActivity.this);
                    Prefs.getInstance().cart_count = jsonObj.getInt("cart_count");
                    Prefs.getInstance().savePrefs(MainActivity.this);
                    cart_count.setText(String.format("%d", Prefs.getInstance().cart_count));
                    product_details_ls = new Gson().fromJson(jsonObj.getJSONArray("product_details").toString(), new TypeToken<ArrayList<ProductDetailModel>>(){}.getType());
                    Log.d(TAG, "product_details_ls ArrayList : "+product_details_ls.toString());
                    try {
                        for (int i = 0; i < jsonObj.getJSONArray("cart_summary").length(); i++) {
                            JSONObject objects = jsonObj.getJSONArray("cart_summary").getJSONObject(i);
                            Iterator key = objects.keys();
//                            Log.d(TAG, key.toString());
                            while (key.hasNext()) {
                                String k = key.next().toString();
                                Log.d(TAG, "Key : " + k + ", value : " + objects.getString(k));
                                cart_summary.put(k, objects.get(k).toString());
                                Log.d(TAG, "cart_summary : " + cart_summary.toString());

                            }
                        }
                    }
                    catch (Exception e){
                        Log.d(TAG, e.toString());
                    }
                    finally {
                        Prefs.getInstance().loadPrefs(MainActivity.this);
//                        Prefs.getInstance().cart_count = cart_summary.size();
                        Prefs.getInstance().savePrefs(MainActivity.this);
                    }

                    display_collection1();


//                        ImagesArray.add( new Group_Drawable(
//                                jsonObject1.getString( "banner_id" ),
//                                banner_parh + jsonObject1.getString( "banner_image" ),
//                                no_image_path ) );
                } else {
                    if (jsonObj.has( "error_msg" )) {
                        Log.d(TAG,jsonObj.getString("error_msg"));
                        Toast.makeText(MainActivity.this,jsonObj.getString("error_msg"), Toast.LENGTH_SHORT).show();
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        else{
//
//        }
    }

    private void display_collection1(){
        Log.d(TAG, "display_collection1() ");
        Col1ListAdapter adapter = new Col1ListAdapter( MainActivity.this, product_details_ls, cart_summary );
        rv_col1.setAdapter( adapter );

    }

    @Override
    public void onFailureJson(int responseCode, String responseMessage) {
        loadingDialog.hide();
        Log.e(TAG, "onFailureJson()" + responseMessage);
    }

}