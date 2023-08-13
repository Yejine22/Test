package com.example.test;

import java.text.SimpleDateFormat;
import java.util.Date;
public class MemoryItem {
    private int hour;
    private int minute;
    private int temperature;
    private String now;
    private Boolean Stop;
    //8.11추가 파이어베이스 기록 삭제를 위한 key 추가
    private String key;

    public MemoryItem() {
        // Default constructor required for Firebase
    }
    public MemoryItem(int temperature, int hour, int minute, Boolean Stop) {
        this.hour = hour;
        this.minute = minute;
        this.temperature = temperature;
        this.Stop=Stop;
        this.now=setNow();
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
    public Boolean getStop() { return Stop; }
    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setStop(Boolean Stop) {
        this.Stop=Stop;
    }
    //8.11 추가
    public void setKey(String key) {
        this.key = key;
    }
    public String getKey() {
        return key;
    }
    //8.11 추가
    public String getString() {
        return "온도: " + this.temperature + "℃, 끓일 시간: " + this.hour + "시 " + this.minute + "분";
    }

    public String setNow(){
        long now=System.currentTimeMillis();
        Date date=new Date(now);
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String getTime=dateFormat.format(date);
        this.now=getTime;
        return getTime;
    }

    public int setPresentHour(){
        long now=System.currentTimeMillis();
        Date date=new Date(now);
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH");
        String Hour=dateFormat.format(date);
        return Integer.parseInt(Hour);
    }

    public int setPresentMinute(){
        long now=System.currentTimeMillis();
        Date date=new Date(now);
        SimpleDateFormat dateFormat=new SimpleDateFormat("mm");
        String Minute=dateFormat.format(date);
        return Integer.parseInt(Minute);
    }

    public String getNow() {
        return now;
    }

}
