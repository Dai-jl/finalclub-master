package com.czq.club;

public class Club2 {
    int cId;
    int imageId;

    public Club2(int imageid,String name){
        this.imageId = imageid;
        this.name = name;
    }
    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;
}
