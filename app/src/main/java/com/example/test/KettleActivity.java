package com.example.test;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class KettleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kettle);

//        Button button =findViewById(R.id.MemoryButton);
//
//        button.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent =new Intent(getApplicationContext(), MemoryActivity.class);
//                startActivity(intent);
//                Toast.makeText(getApplicationContext(),"user의 사용기록을 확인합니다.",Toast.LENGTH_LONG).show();
//            }
//        });
    }
}