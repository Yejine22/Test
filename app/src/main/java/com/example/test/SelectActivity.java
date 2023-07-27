package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SelectActivity extends AppCompatActivity {

    private ArrayList<String> items;
    private ArrayAdapter adapter;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        items = new ArrayList<String>() ;
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items) ;

        listview = (ListView) findViewById(R.id.kettlelist) ;
        listview.setAdapter(adapter) ;


        Button addButton = (Button)findViewById(R.id.Add) ;
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Intent intent =new Intent(getApplicationContext(), KettleActivity.class);
//                startActivity(intent);
//                Toast.makeText(getApplicationContext(),"새 포트를 추가합니다.",Toast.LENGTH_LONG).show();
                int count;
                count = adapter.getCount();

                // 아이템 추가.
                items.add("LIST" + Integer.toString(count + 1));

                // listview 갱신
                adapter.notifyDataSetChanged();
            }
        }) ;

        Button deleteButton = (Button)findViewById(R.id.Delete) ;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int count, checked ;
                count = adapter.getCount() ;

                if (count > 0) {
                    // 현재 선택된 아이템의 position 획득.
                    checked = listview.getCheckedItemPosition();

                    if (checked > -1 && checked < count) {
                        // 아이템 삭제
                        items.remove(checked) ;

                        // listview 선택 초기화.
                        listview.clearChoices();

                        // listview 갱신.
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }) ;

        Button selectButton = (Button)findViewById(R.id.button5) ;
        selectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int count, checked ;
                count = adapter.getCount() ;

                if (count > 0) {
                    // 현재 선택된 아이템의 position 획득.
                    checked = listview.getCheckedItemPosition();

                    if (checked > -1 && checked < count) {

                        Intent intent =new Intent(getApplicationContext(), AdjustActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"전기포트가 선택되었습니다.",Toast.LENGTH_LONG).show();

                        // listview 선택 초기화.
                        listview.clearChoices();

                        // listview 갱신.
                        //adapter.notifyDataSetChanged();
                    }
                }
            }
        }) ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {


                String strText = (String) parent.getItemAtPosition(position) ;

                // TODO : use strText
            }
        }) ;
        //다른 화면 다녀와도 리스트 유지
        if (savedInstanceState != null) {
            ArrayList<String> restoredItems = savedInstanceState.getStringArrayList("items");
            if (restoredItems != null) {
                items.addAll(restoredItems);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("items", items);

    }

}