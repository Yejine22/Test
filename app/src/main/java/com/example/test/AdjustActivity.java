package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdjustActivity extends AppCompatActivity {

    private Boolean Stop=false;
    private TimePicker timePicker;
    private SeekBar seekBar;
    private ArrayList<MemoryItem> memoriesList;
    private ArrayAdapter<Object> memoriesAdapter;
    private Switch lockSwitch;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust);

        timePicker = findViewById(R.id.timePicker);

        timePicker.setIs24HourView(true);

        Button button =findViewById(R.id.MemoryButton);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent =new Intent(AdjustActivity.this, MemoryActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(getApplicationContext(),"user의 사용기록을 확인합니다.",Toast.LENGTH_LONG).show();
            }
        });

        tv = findViewById(R.id.textView);

        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

               tv.setText(String.format("온도는 %d도 입니다.", seekBar.getProgress()));
            }
        });

        //끓여줘 버튼
        Button boil =findViewById(R.id.boilingbutton);
        MemoryItem m=new MemoryItem();
        boil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                int temperature = seekBar.getProgress();
                m.getNow();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference historyRef = database.getReference("memory");

                String recordId = historyRef.push().getKey();
                historyRef.child(recordId).setValue(new MemoryItem(temperature, hour, minute, false));
            }
            //버튼 누르면 전기포트 끓이는 설정 연동 코드 추가
        });

        //Lock 스위치
        lockSwitch =findViewById(R.id.LockSwitch);
        lockSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // "Lock" 스위치가 활성화된 경우
                // 버튼들을 비활성화
                boil.setEnabled(false);
//                button2.setEnabled(false);
            } else {
                // "Lock" 스위치가 비활성화된 경우
                // 버튼들을 활성화
                boil.setEnabled(true);
//                button2.setEnabled(true);
            }
        });

        //Stop 버튼

        Button stop =findViewById(R.id.EmergencyStopButton);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Stop=true;

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference historyRef = database.getReference("memory");

                String recordId = historyRef.push().getKey();
                historyRef.child(recordId).setValue(new MemoryItem(-1,-1,-1, Stop));
            }
            //버튼 누르면 전기포트 끓이는 설정 연동 코드 추가
        });
    }

}