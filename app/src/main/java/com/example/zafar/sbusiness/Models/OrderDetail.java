package com.example.zafar.sbusiness.Models;

/**
 * Created by Zafar on 10/20/2017.
 */

public class OrderDetail {
    public String order_id;
    public String product_id;
    public String prodcut_quantity;
    public String product_name;
    public String price;

    public OrderDetail(String order_id , String product_id , String product_quantity , String product_name , String price){
        this.order_id =  order_id;
        this.product_id = product_id;
        this.prodcut_quantity = product_quantity;
        this.product_name = product_name;
        this.price = price;
    }

    public OrderDetail(){}
//    Getters

    public String getOrder_id() {
        return order_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProdcut_quantity() {
        return prodcut_quantity;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getPrice() {
        return price;
    }


//    Setters


    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setProdcut_quantity(String prodcut_quantity) {
        this.prodcut_quantity = prodcut_quantity;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
