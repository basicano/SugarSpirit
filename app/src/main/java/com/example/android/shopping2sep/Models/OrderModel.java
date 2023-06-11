package com.example.android.shopping2sep.Models;

public class OrderModel {
    String order_id;
    String no_of_items;
    String order_status;
    String dely_date;
    String billed_charge;

    public OrderModel(String order_id, String no_of_items, String order_status, String dely_date, String billed_charge) {
        this.order_id = order_id;
        this.no_of_items = no_of_items;
        this.order_status = order_status;
        this.dely_date = dely_date;
        this.billed_charge = billed_charge;
    }

    public String getDely_date() {
        return dely_date;
    }

    public void setDely_date(String dely_date) {
        this.dely_date = dely_date;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getNo_of_items() {
        return no_of_items;
    }

    public void setNo_of_items(String no_of_items) {
        this.no_of_items = no_of_items;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getBilled_charge() {
        return billed_charge;
    }

    public void setBilled_charge(String billed_charge) {
        this.billed_charge = billed_charge;
    }
}
