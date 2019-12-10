package com.czq.club;

import android.graphics.drawable.Drawable;

public class BeanMyclub_task {
    private Drawable myclub_logo;
    private String myclub_name;
    private String time;
    private String task_content;

    public Drawable getMyclub_logo() {
        return myclub_logo;
    }

    public void setMyclub_logo(Drawable myclub_logo) {
        this.myclub_logo = myclub_logo;
    }

    public String getMyclub_name() {
        return myclub_name;
    }

    public String getTime() {
        return time;
    }

    public void setMyclub_name(String myclub_name) {
        this.myclub_name = myclub_name;
    }

    public String getTask_content() {
        return task_content;
    }

    public void setTask_content(String task_content) {
        this.task_content = task_content;
    }

    public void setTime(String time) {
        this.time = time;
    }
//    public BeanMyclub_task(int myclub_logo,String myclub_name,String time,String task_content){
//        this.myclub_logo=myclub_logo;
//        this.myclub_name=myclub_name;
//        this.task_content=task_content;
//        this.time=time;
//    }
}
