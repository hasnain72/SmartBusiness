package com.example.zafar.sbusiness.Models;

/**
 * Created by Zafar on 10/20/2017.
 */

public class Category {
    public String cat_id;
    public String cat_name;
    public String parent_id;
    public String cat_desc;
    public String cat_image;

    public Category(String cat_id , String cat_name , String parent_id , String cat_desc , String cat_image){
        this.cat_id =  cat_id;
        this.cat_desc = cat_desc;
        this.cat_name = cat_name;
        this.parent_id = parent_id;
        this.cat_image = cat_image;
    }

    public Category(){}

//    Getters
    public Category(String cat_id , String cat_name){
        this.cat_id =  cat_id;
        this.cat_name = cat_name;
    }

    public String getParent_id() {
        return parent_id;
    }

    public String getCat_name() {
        return cat_name;
    }

    public String getCat_id() {
        return cat_id;
    }

    public String getCat_desc() {
        return cat_desc;
    }

    public String getCat_image() {
        return cat_image;
    }


//    Setters

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public void setCat_desc(String cat_desc) {
        this.cat_desc = cat_desc;
    }

    public void setCat_image(String cat_image) {
        this.cat_image = cat_image;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }
}
