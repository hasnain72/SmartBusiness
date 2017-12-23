package com.example.zafar.sbusiness.Models;

/**
 * Created by Zafar on 10/20/2017.
 */

public class PwithC {
    public String id;
    public String store_id;
    public String cat_id;



    public PwithC(){}

    public PwithC(String id, String store_id, String cat_id) {
        this.id = id;
        this.store_id = store_id;
        this.cat_id = cat_id;
    }
//    Getters

    public String getId() {
        return id;
    }

    public String getStore_id() {
        return store_id;
    }

    public String getCat_id() {
        return cat_id;
    }


//    Setters


    public void setId(String id) {
        this.id = id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }
}

