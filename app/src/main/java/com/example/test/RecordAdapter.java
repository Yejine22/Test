package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class RecordAdapter extends ArrayAdapter<MemoryItem> {
    private int resourceLayout;
    private Context mContext;

    public RecordAdapter(Context context, int resource, List<MemoryItem> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        MemoryItem record = getItem(position);
        if (record != null) {
            TextView nowTextView=v.findViewById(R.id.textViewNow);
            TextView timeTextView = v.findViewById(R.id.textViewTime);
            TextView temperatureTextView = v.findViewById(R.id.textViewTemperature);
            if(nowTextView != null){
                String now= record.getNow();
                nowTextView.setText(now);
            }
            if(timeTextView != null){
                String time= "시간: " + record.getHour() + "시 " + record.getMinute() + "분";
                timeTextView.setText(time);
            }
            if (temperatureTextView != null) {
                // 사용기록 데이터를 TextView에 표시
                String temp="온도: "+record.getTemperature()+"℃";
                temperatureTextView.setText(temp);
            }
        }
        return v;
    }
}

   /* @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        MemoryItem record = getItem(position);
        if (record != null) {
            TextView temperatureTextView = v.findViewById(R.id.memorylist);
            if (temperatureTextView != null) {
                // 사용기록 데이터를 TextView에 표시
                String historyText = "온도: " + record.getTemperature() + "℃, 끓일 시간: " + record.getHour() + "시 " + record.getMinute() + "분";
                temperatureTextView.setText(historyText);
            }
        }
        return v;
    }
*/