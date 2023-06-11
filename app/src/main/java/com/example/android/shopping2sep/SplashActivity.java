package com.example.android.shopping2sep;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.android.shopping2sep.library.Common;
import com.example.android.shopping2sep.library.Prefs;

import java.util.Locale;


public class SplashActivity extends Activity {

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        getWindow().setBackgroundDrawable(null);

        Prefs.getInstance().loadPrefs(this);

        if(new Common().isInternetAvailable(this)){
//            SecureKeyStore.updateSecureKey(SplashScreen.this);

            context=this;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if(Prefs.getInstance().is_logged_in){
                        setContentView(R.layout.activity_main);
//                        Locale locale;
//                        if(Prefs.getInstance().languageId.equalsIgnoreCase("1")) {
//                            locale = new Locale("en");
//                        }else{
//                            locale = new Locale("ar");
//                        }
//                        Resources res = getResources();
//                        DisplayMetrics dm = res.getDisplayMetrics();
//                        Configuration conf = res.getConfiguration();
//                        conf.locale = locale;
//                        res.updateConfiguration( conf, dm );

                        Intent intent = new Intent(context, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Log.d("Splash", "Starting Login");
                        Intent intent = new Intent(context, SignUpIn.class);
                        startActivity(intent);
                        finish();
                    }

                }
            },3000);       //3000ms

        }else {
            Toast.makeText(this, "Please check internet connection !", Toast.LENGTH_SHORT).show();
        }

//        final Handler handler = new Handler();
//        Runnable r = new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        };
//        handler.postDelayed(r,3000);

    }
}
