package com.example.android.shopping2sep;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class SignUpIn extends AppCompatActivity {
    public static final String TAG = SignUpIn.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_up_holder);
        initFragment();
    }

    private void initFragment() {
        Fragment fragment;
        fragment = new SignInFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction().setReorderingAllowed(true);
        ft.replace(R.id.fragment_frame,fragment);
        ft.commit();
    }


}
