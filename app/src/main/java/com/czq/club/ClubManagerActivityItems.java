package com.czq.club;

public class ClubManagerActivityItems {
    private String name;
    private String state;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public ClubManagerActivityItems(String name,String state){
        this.state=state;
        this.name=name;
    }
}
