package com.example.test;

import java.text.SimpleDateFormat;
import java.util.Date;
public class MemoryItem {
    private int hour;
    private int minute;
    private int temperature;
    private long time;
    private String now;

    public MemoryItem() {
        // Default constructor required for Firebase
    }
    public MemoryItem(int temperature, int hour, int minute, long time) {
        this.hour = hour;
        this.minute = minute;
        this.temperature = temperature;
        this.time = time;
    }

    public int getHour() {
        return hour;
    }
    public int getMinute() {
        return minute;
    }

    public int getTemperature() {
        return temperature;
    }
    public long getTime() { return time; }
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setTime(long timestamp) {
        this.time = timestamp;
    }

    public String getString() {
        return "온도: " + this.temperature + "℃, 끓일 시간: " + this.hour + "시 " + this.minute + "분";
    }

    public String getNow(){
        long now=System.currentTimeMillis();
        Date date=new Date(now);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String getTime=dateFormat.format(date);
        this.now=getTime;
        return getTime;
    }
}
