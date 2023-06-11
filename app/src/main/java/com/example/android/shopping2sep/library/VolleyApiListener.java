package com.example.android.shopping2sep.library;

public interface VolleyApiListener {
    public void onSuccessJson(String response, String type);
    public void onFailureJson(int responseCode, String responseMessage);
}
