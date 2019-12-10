package com.czq.club;

import android.graphics.drawable.Drawable;

public class BeanMyclub_passage {
    private Drawable myclub_logo;
    private String myclub_name;
    private String time;
    private Drawable passage_photo;
    private String passage_sumary;
    private String passage_url;

    public String getPassage_url(){return passage_url;}

    public void setPassage_url(String passage_url) {
        this.passage_url = passage_url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPassage_sumary() {
        return passage_sumary;
    }

    public void setPassage_sumary(String passage_sumary) {
        this.passage_sumary = passage_sumary;
    }

    public Drawable getPassage_photo() {
        return passage_photo;
    }

    public void setPassage_photo(Drawable passage_photo) {
        this.passage_photo = passage_photo;
    }

    public String getMyclub_name() {
        return myclub_name;
    }

    public void setMyclub_name(String myclub_name) {
        this.myclub_name = myclub_name;
    }

    public Drawable getMyclub_logo() {
        return myclub_logo;
    }

    public void setMyclub_logo(Drawable myclub_logo) {
        this.myclub_logo = myclub_logo;
    }
//    public BeanMyclub_passage(Drawable myclub_logo,String myclub_name,String time,Drawable passage_photo,String passage_sumary){
//        this.myclub_logo=myclub_logo;
//        this.myclub_name=myclub_name;
//        this.passage_photo=passage_photo;
//        this.passage_sumary=passage_sumary;
//        this.time=time;
//    }


}
