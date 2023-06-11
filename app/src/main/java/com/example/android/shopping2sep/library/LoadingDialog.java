package com.example.android.shopping2sep.library;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog {
    ProgressDialog progressDialog;

    public LoadingDialog(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
    }

    public void show(){
        if(!progressDialog.isShowing()){
            progressDialog.show();
        }
    }
    public void hide(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }
}
