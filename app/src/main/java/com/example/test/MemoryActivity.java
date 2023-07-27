package com.example.test;

//import android.os.Bundle;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MemoryActivity extends AppCompatActivity {
//    private ListView listView;
//    private List<String> historyList;
//    private ArrayAdapter<String> adapter;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_memory);
//
//        // ListView와 어댑터 초기화
//        listView = findViewById(R.id.memorylist);
//        historyList = new ArrayList<>();
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, historyList);
//        listView.setAdapter(adapter);
//
//        // 파이어베이스 데이터베이스 인스턴스 가져오기
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference historyRef = database.getReference("사용기록");
//
//        // "사용기록" 노드에 대한 이벤트 리스너 등록
//        historyRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // 사용기록 데이터가 변경될 때마다 호출되는 콜백 메서드
//                // dataSnapshot을 이용하여 사용기록 데이터를 읽어와서 ListView에 표시하는 작업을 수행합니다.
//                historyList.clear(); // 기존 데이터 초기화
//
//                for (DataSnapshot recordSnapshot : dataSnapshot.getChildren()) {
//                    MemoryItem record = recordSnapshot.getValue(MemoryItem.class);
//                    if (record != null) {
//                        String historyText = "온도: " + record.getTemperature() + "℃, 끓일 시간: " + record.getHour() + "시 " + record.getMinute() + "분";
//                        historyList.add(historyText);
//                    }
//                }
//
//                // ListView 갱신
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // 데이터베이스 읽기를 취소할 때 호출되는 콜백 메서드
//                // 오류 처리 등을 수행합니다.
////                Toast.makeText(getApplicationContext(),"error발생",Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//}

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

import java.util.ArrayList;
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
        historyRef = database.getReference("사용기록");

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
//                        String historyText = "온도: " + record.getTemperature() + "℃, 끓일 시간: " + record.getHour() + "시 " + record.getMinute() + "분";
                        recordList.add(record);
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
}



//public class MemoryActivity extends AppCompatActivity {
//
//    private ArrayList<MemoryItem> memoriesList;
//    private ArrayAdapter<MemoryItem> memoriesAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_memory);
//
//        ListView memorylist = findViewById(R.id.memorylist);
//        memoriesList = new ArrayList<>();
//        memoriesAdapter = new ArrayAdapter<>(this, R.layout.memory_item, memoriesList);
//        memorylist.setAdapter(memoriesAdapter);
//
//        // MainActivity에서 전달받은 데이터를 가져와 사용기록에 추가
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            MemoryItem recordItem = (MemoryItem) extras.getSerializable("memory");
//            if (recordItem != null) {
//                memoriesList.add(recordItem);
//                memoriesAdapter.notifyDataSetChanged();
//
//            }
//        }
//    }
//}