package com.example.android.shopping2sep;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.example.android.shopping2sep.library.Prefs;
import com.example.android.shopping2sep.library.VolleyApiListener;
import com.example.android.shopping2sep.library.VolleyApiRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CartFunctionality implements VolleyApiListener {
    public final String TAG = CartFunctionality.class.getSimpleName();
    private Activity activity;
    String user_email;
    private String fn_cart_func= "cart_functions", ty_add = "add", ty_update = "update", ty_remove = "remove" ;

    public CartFunctionality(Activity activity){
        user_email = Prefs.getInstance().emailId;
        this.activity = activity;
    }

    public void updateQnty(String product_id, String  qtn) {
        HashMap<String, String> params = new HashMap<>();

        params.put("email", user_email);
        params.put("product_id", product_id);
        params.put("qtn", qtn);
        params.put("action", ty_update);
        new VolleyApiRequest(activity, this, ty_update, fn_cart_func, params);
    }

    public void addToCart(String product_id, String qtn) {
        HashMap<String, String> params = new HashMap<>();

        params.put("email", user_email);
        params.put("product_id", product_id);
        params.put("qtn", qtn);
        params.put("action", ty_add);
        new VolleyApiRequest(activity, this, ty_add, fn_cart_func, params);
    }

    public void removeFromCart(String product_id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", user_email);
        params.put("product_id", product_id);
        params.put("action", ty_remove);
        params.put("qtn", "0");
        new VolleyApiRequest(activity, this, ty_remove, fn_cart_func, params);
    }

    @Override
    public void onSuccessJson(String response, String type) {
        try{
            JSONObject obj = new JSONObject(response);
            if (obj.getString("status").equals("success")) {
//                Log.d(TAG, response);
                Prefs.getInstance().loadPrefs(activity);
                Prefs.getInstance().cart_count = obj.getInt("cart_count");
            }
            else {
                String error = obj.getString("error_msg");
                Log.d(TAG, error);
                Toast.makeText(activity, error, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFailureJson(int responseCode, String responseMessage) {
        Log.e(TAG, "onFailureJson()" + responseMessage);
    }
}
