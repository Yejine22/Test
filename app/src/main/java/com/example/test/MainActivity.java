package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ImageView mic;
    TextView inputTextView;
    TextView resultTextView;

    private static final int SPEECH_INPUT = 1;
    private boolean isAutoListening = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.StartButton);

        mic = findViewById(R.id.mic);
        inputTextView = findViewById(R.id.inputTextView); // 입력된 문장을 표시할 TextView
        resultTextView = findViewById(R.id.resultTextView);

        /*mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR"); // 한국어로 설정
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "음성을 입력하세요");

                try {
                    startActivityForResult(intent, SPEECH_INPUT);
                }
                catch (Exception e) {
                    Toast.makeText(MainActivity.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startListening();
            }
        });

        if (isAutoListening) {
            startListening();
        }

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent =new Intent(getApplicationContext(),AdjustActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startListening() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR"); // 한국어로 설정
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "음성을 입력하세요");

        try {
            startActivityForResult(intent, SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String speechInput = Objects.requireNonNull(result).get(0);
                inputTextView.setText("입력된 문장: " + speechInput);
                resultTextView.setText("");

                KeywordExtractor keywordExtractor = new KeywordExtractor();
                KeywordInfo keywordInfo = keywordExtractor.extractKeywords(speechInput);

                //파이어베이스에 데이터 업로드
                MemoryItem m=new MemoryItem();
                int hour = keywordInfo.hour;
                int minute = keywordInfo.minute;
                int temperature = keywordInfo.temperature;
                m.getNow();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference historyRef = database.getReference("memory");

                String recordId = historyRef.push().getKey();
                historyRef.child(recordId).setValue(new MemoryItem(temperature, hour, minute,false));

                String keywordMessage = "키워드 추출 결과: \n";
                keywordMessage += "당장 할건지 여부: " + keywordInfo.isNow + "\n";
                keywordMessage += "시: " + (keywordInfo.hour != -1 ? keywordInfo.hour : 0) + "\n";
                keywordMessage += "분: " + (keywordInfo.minute != -1 ? keywordInfo.minute : 0) + "\n";
                keywordMessage += "온도: " + (keywordInfo.temperature != -1 ? keywordInfo.temperature : 100) + "도\n";
                keywordMessage += "끓여달라고 말했는지 여부: " + keywordInfo.isBoil;
                resultTextView.setText(keywordMessage);

                // 음성인식 다시 시작하는 조건
                if ((((keywordInfo.hour==-1 && keywordInfo.minute==-1
                        && keywordInfo.isNow && keywordInfo.temperature == -1))
                        || !speechInput.contains("안녕"))
                        || !speechInput.contains("끓여 줘"))
                {
                    startListening();
                }
            }
        }
    }

    class KeywordExtractor {
        public KeywordInfo extractKeywords(String input) {
            KeywordInfo keywordInfo = new KeywordInfo();

            Pattern nowPattern = Pattern.compile("지금|현재|당장");
            Pattern hourPattern = Pattern.compile("(\\d+)시");
            Pattern minutePattern = Pattern.compile("(\\d+)분");
            Pattern temperaturePattern = Pattern.compile("(\\d+)도");
            Pattern boilPattern = Pattern.compile("끓여 줘");

            Matcher nowMatcher = nowPattern.matcher(input);
            Matcher hourMatcher = hourPattern.matcher(input);
            Matcher minuteMatcher = minutePattern.matcher(input);
            Matcher temperatureMatcher = temperaturePattern.matcher(input);
            Matcher boilMatcher = boilPattern.matcher(input);

            // 만약 키워드가 추출되지 않았을 경우에는 기본값
            if (hourMatcher.find()) {
                keywordInfo.hour = Integer.parseInt(hourMatcher.group(1));
            }

            if (minuteMatcher.find()) {
                keywordInfo.minute = Integer.parseInt(minuteMatcher.group(1));
            }

            if (temperatureMatcher.find()) {
                keywordInfo.temperature = Integer.parseInt(temperatureMatcher.group(1));
            }

            // 시(hour)와 분(minute)이 모두 추출되지 않았다? => 당장할건지 여부(isNow)가 true
            if (keywordInfo.hour==-1 && keywordInfo.minute==-1) {
                keywordInfo.isNow = true;
            }
            // 또는 지금, 당장, 현재 세 키워드가 들어가도 당장 할건지 여부는 true
            else if (nowMatcher.find()) {
                keywordInfo.isNow = true;
            }

            //"끓여줘"라는 키워드가 추출되면 isBoil이 true
            if (boilMatcher.find()) {
                keywordInfo.isBoil = true;
            }

            return keywordInfo;
        }
    }


    class KeywordInfo {
        boolean isNow = false;
        boolean isBoil = false;
        int hour = -1;
        int minute = -1;
        int temperature = -1;
    }
}

