package com.example.android.shopping2sep.Models;

import android.util.Log;

public class AddressModel {
    public static final String TAG = AddressModel.class.getSimpleName();
    private String slno;
    private String rcvr_name;
    private String mob_no;
    private String address;
    private String pin_code;


    public AddressModel(String slno, String rcvr_name, String mob_no, String address, String pin_code) {
        Log.d(TAG, "slno : " + slno);
        this.slno = slno;
        this.rcvr_name = rcvr_name;
        this.mob_no = mob_no;
        this.address = address;
        this.pin_code = pin_code;
    }


    public String getSlno() {
        return slno;
    }

    public void setSlno(String slno) {
        this.slno = slno;
    }

    public String getRcvr_name() {
        return rcvr_name;
    }

    public void setRcvr_name(String rcvr_name) {
        this.rcvr_name = rcvr_name;
    }

    public String getMob_no() {
        return mob_no;
    }

    public void setMob_no(String mob_no) {
        this.mob_no = mob_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getFullAddress(){
        return rcvr_name+"\n"+address+"\n"+pin_code+"\n"+mob_no;
    }
}
