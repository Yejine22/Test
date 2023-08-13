package com.example.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;
//8.11 수정

public class MemoryActivity extends AppCompatActivity {
    private ListView listView;
    private RecordAdapter recordAdapter;
    private List<MemoryItem> recordList;
    private DatabaseReference historyRef;

    private DocumentReference docRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory);

        // ListView와 어댑터 초기화
        listView = findViewById(R.id.memorylist);
        recordList = new ArrayList<>();
        recordAdapter = new RecordAdapter(this, R.layout.memory_item, recordList);
        listView.setAdapter(recordAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

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
                        //8.11 추가 머가문제야ㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏㅏ
                        //record.setKey(recordSnapshot.getKey());
                        if(record.getTemperature()!=-1){
                            if(record.getHour()==-1) {
                                if (record.getMinute() == -1) {
                                    record.setMinute(record.setPresentMinute());
                                }
                                record.setHour(record.setPresentHour());
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
        //8.11 추가, 사용기록 삭제 버튼 추가
        Button deleteButton = (Button)findViewById(R.id.Delete) ;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int count, checked ;
                count = recordAdapter.getCount() ;
                //Log.d("MemoryActivity", "Delete button clicked"); // 디버깅 로그 추가

                if (count > 0) {
                    // 현재 선택된 아이템의 position 획득.
                    checked = listView.getCheckedItemPosition();
                    Log.d("MemoryActivity", "Deletion completed"+count);
                    Log.d("MemoryActivity", "Deletion completed"+checked);

                    if (checked > -1 && checked < count) {

                        Log.d("MemoryActivity", "Deletion completed"+count);
                        MemoryItem selectedItem = recordList.get(checked);
                        String selectedKey = selectedItem.getKey(); // 만약 MemoryItem 클래스에 키 관련 필드가 있다면 이에 맞게 수정

                        // Firebase에서 삭제
                        historyRef.child(selectedKey).removeValue(); // 선택한 항목의 키를 사용하여 Firebase에서 삭제

                        // 아이템 삭제
                        recordList.remove(checked) ;

                        // listview 선택 초기화.
                        listView.clearChoices();

                        // listview 갱신.
                        recordAdapter.notifyDataSetChanged();
                    }
                }
                //Log.d("MemoryActivity", "Deletion completed"); // 디버깅 로그 추가
            }
        }); //8.11 추가, 사용기록 삭제 버튼 추가
    }



}
