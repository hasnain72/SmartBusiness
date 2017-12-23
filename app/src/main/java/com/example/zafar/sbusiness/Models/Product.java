package com.example.zafar.sbusiness.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zafar on 10/21/2017.
 */

public class Product implements Parcelable {
    String product_id;
    String store_id;
    String cat_id;
    String product_name;
    String description;
    String price;
    String image;
    String allow_OSP;
    String inventory;
    String qty;
    String status;

    public Product(){}

    public Product(String product_id , String store_id,String cat_id,String product_name,String description,String price,String image, String allow_OSP , String inventory ,String qty , String status){
        this.product_id = product_id;
        this.store_id = store_id;
        this.cat_id = cat_id;
        this.product_name = product_name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.allow_OSP = allow_OSP;
        this.inventory = inventory;
        this.qty = qty;
        this.status = status;
    }

//    Getters

    protected Product(Parcel in) {
        product_id = in.readString();
        store_id = in.readString();
        cat_id = in.readString();
        product_name = in.readString();
        description = in.readString();
        price = in.readString();
        image = in.readString();
        allow_OSP = in.readString();
        inventory = in.readString();
        qty = in.readString();
        status = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getProduct_id() {
        return product_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getAllow_OSP() {return allow_OSP;}

    public String getInventory() {return inventory;}

    public String getQty() {return qty;}

    public String getStatus() {return status;}

    //    Setters

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAllow_OSP(String allow_OSP) {this.allow_OSP = allow_OSP;}

    public void setInventory(String inventory) {this.inventory = inventory;}

    public void setQty(String qty) {this.qty = qty;}

    public void setStatus(String status) {this.status = status;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(product_id);
        parcel.writeString(store_id);
        parcel.writeString(cat_id);
        parcel.writeString(product_name);
        parcel.writeString(description);
        parcel.writeString(price);
        parcel.writeString(image);
        parcel.writeString(allow_OSP);
        parcel.writeString(inventory);
        parcel.writeString(qty);
        parcel.writeString(status);
    }
}
