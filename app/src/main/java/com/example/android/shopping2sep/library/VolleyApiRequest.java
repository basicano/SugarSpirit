package com.example.android.shopping2sep.library;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyApiRequest {
    public static final String TAG = VolleyApiListener.class.getSimpleName();
    private final String  type;                                                                     // to determine what type of request is made for eg: fetch_home_data, update_product_qtn
    private final Activity act;                                                                     // what activity has requested api call
    private final VolleyApiListener volleyApiListener;                                              // listener class 
//    private String local_netURL = "http://192.168.0.101/shopping_2sep/";
    private String online_netURL = "http://muser.weblink4you.com/shopping_android/";
    private String networkURL ="";
    private JSONObject jsonObject = null;
    Map<String, String> params = new HashMap<>();

    public VolleyApiRequest(Activity act, VolleyApiListener volleyApiListener, String api_URL, Map<String, String> params) {
        this.type = "online_resource_api";
        this.act = act;
        this.volleyApiListener = volleyApiListener;
        this.networkURL = api_URL;
        this.params = params;
    }

    public VolleyApiRequest(Activity activity, VolleyApiListener volleyApiListener, String type, String file_name, Map<String, String>  params) {
        this.act = activity;
        this.volleyApiListener = volleyApiListener;
        this.type = type;
//        this.networkURL = local_netURL +file_name;
        this.networkURL = online_netURL +file_name;
        Log.d(  TAG,"URL  = "+networkURL);
        this.params = params;
        Log.d(  TAG,"params = "+params.toString());
        if(new Common().isInternetAvailable(activity)){
            requestCall();
        }
        else{
            Log.e(TAG, "Internet is not enabled");
            Toast.makeText(activity, "Please make sure that you have active internet", Toast.LENGTH_SHORT).show();
        }
    }

    void requestCall() {
        RequestQueue queue = Volley.newRequestQueue(act);
        StringRequest postRequest = new StringRequest(Request.Method.POST, networkURL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        volleyApiListener.onSuccessJson(response, type);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            NetworkResponse response = error.networkResponse;
                            if (response != null) {
                                int code = response.statusCode;

                                String errorMsg = new String(response.data);
                                Log.e(TAG, "error_response : " + errorMsg);
                                try {
                                    jsonObject = new JSONObject(errorMsg);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                String msg = jsonObject.optString("message");
                                volleyApiListener.onFailureJson(code, msg);
                            } else {
                                String errorMsg = error.getMessage();
                                volleyApiListener.onFailureJson(0, errorMsg);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d(TAG, "sent params");
                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);

    }
}
