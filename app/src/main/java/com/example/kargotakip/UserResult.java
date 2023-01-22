package com.example.kargotakip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserResult {
    @SerializedName("user_id")
    @Expose
    public Integer user_id;
    @SerializedName("user_mail")
    @Expose
    public String user_mail;

    public UserResult(Integer user_id, String user_mail) {
        this.user_id = user_id;
        this.user_mail = user_mail;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }
}

