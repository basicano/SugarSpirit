package com.example.android.shopping2sep.Models;

public class CartSummeryModel {
    private String product_id;
    private String qtn;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQtn() {
        return qtn;
    }

    public void setQtn(String qtn) {
        this.qtn = qtn;
    }

    public CartSummeryModel(String product_id, String qtn) {
        this.product_id = product_id;
        this.qtn = qtn;
    }
}
