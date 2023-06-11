package com.example.android.shopping2sep.Models;

public class CartProductModel {

//    public static final String baseURL = "http://localhost/shopping_2sep/product_col1_img/";
    public static final String baseURL = "http://muser.weblink4you.com/shopping_android/product_col1_img/";    // online

    private String product_id;
    private String product_img;
    private String product_name;
//    private String mrp = "";
    private String qtn;
//    private String sel_price ="";
//    private String weight ="";

    public CartProductModel(String product_id, String qtn, String product_img,  String product_name  /*,  String sel_price, String product_name, String productImg, String weight, String mrp*/) {
        this.product_id = product_id;
        this.product_img = product_img;
        this.qtn = qtn;
//        this.sel_price = sel_price;
        this.product_name = product_name;
//        this.weight = weight;
//        this.mrp = mrp;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String id) {
        this.product_img = product_img;
    }

    public String getQtn() {
        return qtn;
    }

    public void setQtn(String qtn) {
        this.qtn = qtn;
    }

//    public String getSel_price() {
//        return sel_price;
//    }
//
//    public void setSel_price(String sel_price) {
//        this.sel_price = sel_price;
//    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

//    public String getWeight() {
//        return weight;
//    }
//
//    public void setWeight(String weight) {
//        this.weight = weight;
//    }

//    public String getMrp() {
//        return mrp;
//    }
//
//    public void setMrp(String mrp) {
//        this.mrp = mrp;
//    }

    public String  get_full_img_path(){
        return baseURL + product_img;
    }
}

