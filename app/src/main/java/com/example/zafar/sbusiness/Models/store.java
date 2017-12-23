package com.example.zafar.sbusiness.Models;

/**
 * Created by Zafar on 11/12/2017.
 */

public class store {
    String store_id;
    String business_user_id;
    String cat_id;
    String title;
    String location;
    String created_at;

    public store(String store_id, String business_user_id, String cat_id, String title, String location, String created_at) {
        this.store_id = store_id;
        this.business_user_id = business_user_id;
        this.cat_id = cat_id;
        this.title = title;
        this.location = location;
        this.created_at = created_at;
    }

    public store(){}

//    Getters
    public String getStore_id() {
        return store_id;
    }

    public String getBusiness_user_id() {
        return business_user_id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getCreated_at() {
        return created_at;
    }

//    Setters
    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public void setBusiness_user_id(String business_user_id) {
        this.business_user_id = business_user_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}


