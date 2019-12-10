package com.czq.club;

import android.graphics.drawable.Drawable;

public class club_manager_member_items {
    Drawable imageid;
    String name;
    String number;
    String phone;
    String branch;

    public void setImageid(Drawable imageid) {
        this.imageid = imageid;
    }

    public Drawable getImageid() {
        return imageid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getBranch() {
        return branch;
    }

    public String getNumber() {
        return number;
    }

    public String getPhone() {
        return phone;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
//    public club_manager_member_items(int imageid, String name, String number, String phone, String branch){
//        this.branch=branch;
//        this.imageid=imageid;
//        this.name=name;
//        this.number=number;
//        this.phone=phone;
//    }
}
