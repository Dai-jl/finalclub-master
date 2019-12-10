package com.czq.club;

public class Passage {
    private Club2 club;
    private String time;

    public Club2 getClub() {
        return club;
    }

    public void setClub(Club2 club) {
        this.club = club;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public Passage(Club2 club,String time,String title){
        this.club = club;
        this.time = time;
        this.title = title;
    }
}
