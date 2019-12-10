package com.czq.club;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;

public class Club implements Serializable {
    private byte[] imageId;
    private String name;
    private int count;
    private String things;
    private String level;
    private String introduction;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getThings() {
        return things;
    }

    public void setThings(String things) {
        this.things = things;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Club(){}

    public Bitmap ToBitMap() {
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(this.imageId, 0,
                    this.imageId.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public Club(byte[] imageId,String name){
        this.imageId = imageId;
        this.name = name;
    }
    public byte[] getImageId() {
        return imageId;
    }

    public void setImageId(byte[] imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}