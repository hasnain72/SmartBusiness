package com.example.zafar.sbusiness.Models;

/**
 * Created by Zafar on 11/13/2017.
 */

public class User {
    public String user_id;
    public String user_type;
    public String location;
    public String email;
    public String name;
    public String mobile_no;
    public String credit_card;
    public String wallet;
    public String account_number;
    public String status;
    public String created_at;
    public String password;
    public String token;
    public String auth;

    public User(){}
    public User(String user_id, String user_type, String location, String email, String name, String mobile_no, String credit_card, String wallet, String account_number, String status, String created_at, String password, String token, String auth) {
        this.user_id = user_id;
        this.user_type = user_type;
        this.location = location;
        this.email = email;
        this.name = name;
        this.mobile_no = mobile_no;
        this.credit_card = credit_card;
        this.wallet = wallet;
        this.account_number = account_number;
        this.status = status;
        this.created_at = created_at;
        this.password = password;
        this.token = token;
        this.auth = auth;

    }

//    Getters

    public String getUser_id() {
        return user_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getLocation() {
        return location;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public String getCredit_card() {
        return credit_card;
    }

    public String getWallet() {
        return wallet;
    }

    public String getAccount_number() {
        return account_number;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getPassword() {
        return password;
    }

    public String getAuth() {
        return auth;
    }

    public String getToken() {
        return token;
    }

//    Setters


    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public void setCredit_card(String credit_card) {
        this.credit_card = credit_card;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }
}
