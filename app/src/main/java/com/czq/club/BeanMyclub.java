package com.czq.club;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class BeanMyclub implements Serializable {
    private String myclub_name;     //社团名称
    private Drawable myclub_logo;   //社团logo
    private String describtion;     //社团简介
    private int member_count;       //社团人数
    private String things;          //社团事迹
    private int cid;                //社团id

    private String sNo,sName,isload;
    private byte[] in;//活动之间的参数传递

    public String getsName() {
        return sName;
    }

    public void setIn(byte[] in) {
        this.in = in;
    }

    public String getIsload() {
        return isload;
    }

    public byte[] getIn() {
        return in;
    }

    public void setIsload(String isload) {
        this.isload = isload;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsNo() {
        return sNo;
    }

    public void setsNo(String sNo) {
        this.sNo = sNo;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public Drawable getMyclub_logo() {
        return myclub_logo;
    }

    public void setMyclub_logo(Drawable myclub_logo) {
        this.myclub_logo = myclub_logo;
    }

    public void setMyclub_name(String myclub_name) {
        this.myclub_name = myclub_name;
    }

    public int getMember_count() {
        return member_count;
    }

    public String getDescribtion() {
        return describtion;
    }

    public String getThings() {
        return things;
    }

    public void setDescribtion(String describtion) {
        this.describtion = describtion;
    }

    public void setMember_count(int member_count) {
        this.member_count = member_count;
    }

    public void setThings(String things) {
        this.things = things;
    }

    public String getMyclub_name() {
        return myclub_name;
    }
//    public BeanMyclub(String myclub_name,int myclub_logo,String describtion,int member_count,String things){
//        this.myclub_logo=myclub_logo;
//        this.myclub_name=myclub_name;
//        this.describtion=describtion;
//        this.member_count=member_count;
//        this.things=things;
//    }

}
