package com.example.android.shopping2sep.Models;

public class ProductDetailModel {

//    public static final String baseURL = "http://localhost/shopping_2sep/product_col1_img/";
    public static final String baseURL = "http://muser.weblink4you.com/shopping_android/product_col1_img/";    // online



    private String id;
    private String name;
//    private String parent_id;
    private String img;
    private String des;
//    private String image_path;
//    private String total_subcategories;

    ProductDetailModel(){}

    public ProductDetailModel(String id, String name, /*String parent_id,*/ String img,
                              String des/*, String image_path, String total_subcategories*/) {
        this.setId(id);
        this.setName(name);
//        this.parent_id = parent_id;
        this.setImg(img);
        this.setDes(des);
//        this.setImage_path(image_path);
//        this.setTotal_subcategories(total_subcategories);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String get_full_img_path() {
        return baseURL+img;
    }
//
//    public void setImage_path(String image_path) {
//        this.image_path = image_path;
//    }
//
//    public String getTotal_subcategories() {
//        return total_subcategories;
//    }
//
//    public void setTotal_subcategories(String total_subcategories) {
//        this.total_subcategories = total_subcategories;
//    }
}
