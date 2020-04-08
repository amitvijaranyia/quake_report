package com.example.android.quakereport;

public class EarthQuake {
    String  magnitude;
    String location;
    String date;
    String time;
    String offset;
    String url;

    public EarthQuake(String magnitude, String location, String date, String time, String offset, String url) {
        this.magnitude = magnitude;
        this.location = location;
        this.date = date;
        this.time = time;
        this.offset = offset;
        this.url = url;
    }
}
