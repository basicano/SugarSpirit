package com.example.android.shopping2sep.library;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    final String PREF_NAME = "shopping2sep";

    final String PREF_USER_ID = "user_id";
    final String PREF_USER_NAME = "name";
    final String PREF_USER_EMAIL = "email";
    final String PREF_USER_PASSWORD = "password";
    final String PREF_MOBILE = "mobile";
//    final String PREF_USER_TYPE = "user_type";
    final String PREF_IS_LOGGED_IN= "is_logged_in";
    final String PREF_IS_REM_ME = "is_remember_me";
//    final String PREF_CURRENCY = "currency_code";
    final String PREF_CART_QTN = "cart_count";
    final String PREF_ORDER_ID = "order_id";


    final String PREF_Address = "address";
    final String PREF_Zipcode = "zipcode";
    final String PREF_City = "city";
    final String PREF_Country = "country";
    final String PREF_City_id = "city_id";
    final String PREF_Country_id= "country_id";
//    final String PREF_languageId= "languageId";

    public String user_id = "";
    public String name = "";
    public String emailId = "";
    public String password = "";
    public String mobile_no = "";
    public int cart_count = 0;
//    public String user_type = "";
//    public String gender = "";
//    public String currency_code = "";
    public String address = "";
    public String  order_id = "" ;
    public String zipcode = "";
    public String city = "";
    public String country = "";
    public String city_id = "";
    public String country_id = "";
//    public String languageId = "";
//    public String loginBy = "";
    public boolean is_logged_in = false;
    public boolean is_remember_me = false;


    SharedPreferences p;
    SharedPreferences.Editor editor;
    static Prefs prefs = new Prefs();

    public Prefs(){    }

    public static Prefs getInstance() {
        return prefs;
    }

    public void loadPrefs(Context context) {
        p = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        emailId = p.getString(PREF_USER_EMAIL, emailId);
        password = p.getString(PREF_USER_PASSWORD, password);
        user_id = p.getString(PREF_USER_ID, user_id);
        name = p.getString(PREF_USER_NAME, name);
        mobile_no = p.getString(PREF_MOBILE, mobile_no);
        cart_count = p.getInt(PREF_CART_QTN, 0);
        order_id = p.getString(PREF_ORDER_ID, order_id);
//        user_type = p.getString(PREF_USER_TYPE, user_type);
//        gender = p.getString(PREF_GENDER, gender);
//        currency_code = p.getString(PREF_CURRENCY, currency_code);
        address = p.getString(PREF_Address, address);
        zipcode = p.getString(PREF_Zipcode, zipcode);
        city = p.getString(PREF_City, city);
        country = p.getString(PREF_Country, country);
        city_id = p.getString(PREF_City_id, city_id);
        country_id = p.getString(PREF_Country_id, country_id);
        is_logged_in = p.getBoolean(PREF_IS_LOGGED_IN, is_logged_in);
        is_remember_me = p.getBoolean(PREF_IS_REM_ME, is_remember_me);
//        languageId = p.getString(PREF_languageId, languageId);
//        loginBy = p.getString(PREF_loginBy, loginBy);

    }



    public boolean savePrefs(Context context) {
        p = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = p.edit();
        editor.putString(PREF_USER_EMAIL, emailId);
        editor.putString(PREF_USER_PASSWORD, password);
        editor.putString(PREF_USER_ID, user_id);
        editor.putString(PREF_USER_NAME, name);
        editor.putString(PREF_MOBILE, mobile_no);
        editor.putInt(PREF_CART_QTN, cart_count);
        editor.putString(PREF_ORDER_ID, order_id);
//        editor.putString(PREF_USER_TYPE, user_type);
//        editor.putString(PREF_GENDER, gender);
//        editor.putString(PREF_CURRENCY, currency_code);
        editor.putString(PREF_Address, address);
        editor.putString(PREF_Zipcode, zipcode);
        editor.putString(PREF_City, city);
        editor.putString(PREF_Country, country);
        editor.putString(PREF_City_id, city_id);
        editor.putString(PREF_Country_id, country_id);
//        editor.putString(PREF_languageId, languageId);
        editor.putBoolean(PREF_IS_LOGGED_IN, is_logged_in);
        editor.putBoolean(PREF_IS_REM_ME, is_remember_me);

//        editor.putString(PREF_loginBy, loginBy);

        return editor.commit();
    }

    public boolean removePrefs(Context ctx) {
        editor = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(PREF_USER_EMAIL,"" );
        editor.putString(PREF_USER_PASSWORD, "");
        editor.putString(PREF_USER_ID, "");
        editor.putString(PREF_USER_NAME, "");
        editor.putString(PREF_MOBILE, "");
        editor.putInt(PREF_CART_QTN, 0);
        editor.putString(PREF_ORDER_ID, "");
//        editor.putString(PREF_USER_TYPE, "");
//        editor.putString(PREF_GENDER, "");
//        editor.putString(PREF_CURRENCY, "");
        editor.putString(PREF_Address, "");
        editor.putString(PREF_Zipcode, "");
        editor.putString(PREF_City, "");
        editor.putString(PREF_Country, "");
        editor.putString(PREF_City_id, "");
        editor.putString(PREF_Country_id, "");
//        editor.putString(PREF_languageId, "");
//        editor.putString(PREF_loginBy, "");
        editor.putBoolean(PREF_IS_LOGGED_IN, false);
        editor.putBoolean(PREF_IS_REM_ME, false);
        editor.apply();

        return editor.commit();
    }
}
