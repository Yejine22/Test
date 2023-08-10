package com.example.test;

import android.os.Bundle;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MemoryActivity extends AppCompatActivity {
    private ListView listView;
    private RecordAdapter recordAdapter;
    private List<MemoryItem> recordList;
    private DatabaseReference historyRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        // ListView와 어댑터 초기화
        listView = findViewById(R.id.memorylist);
        recordList = new ArrayList<>();
        recordAdapter = new RecordAdapter(this, R.layout.memory_item, recordList);
        listView.setAdapter(recordAdapter);

        // 파이어베이스 데이터베이스 인스턴스 가져오기
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        historyRef = database.getReference("memory");

        // "사용기록" 노드에 대한 이벤트 리스너 등록
        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 사용기록 데이터가 변경될 때마다 호출되는 콜백 메서드
                // dataSnapshot을 이용하여 사용기록 데이터를 읽어와서 ListView에 표시하는 작업을 수행합니다.
                recordList.clear(); // 기존 데이터 초기화

                for (DataSnapshot recordSnapshot : dataSnapshot.getChildren()) {
                    MemoryItem record = recordSnapshot.getValue(MemoryItem.class);
                    if (record != null) {
                        if(record.getTemperature()!=-1){
                            if(record.getHour()==-1) {
                                if (record.getMinute() == -1) {
                                    record.setMinute(record.getPresentMinute());
                                }
                                record.setHour(record.getPresentHour());
                            }
                            recordList.add(record);
                        }else{
                            record.setTemperature(100);
                            recordList.add(record);
                        }
                    }
                }

                // ListView 갱신
                recordAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터베이스 읽기를 취소할 때 호출되는 콜백 메서드
                // 오류 처리 등을 수행합니다.
            }
        });
    }

    public int getPresentHour(){
        long now=System.currentTimeMillis();
        Date date=new Date(now);
        SimpleDateFormat dateFormat=new SimpleDateFormat("HH");
        String Hour=dateFormat.format(date);
        return Integer.parseInt(Hour);
    }

    public int getPresentMinute(){
        long now=System.currentTimeMillis();
        Date date=new Date(now);
        SimpleDateFormat dateFormat=new SimpleDateFormat("mm");
        String Minute=dateFormat.format(date);
        return Integer.parseInt(Minute);
    }
}
