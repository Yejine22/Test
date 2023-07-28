package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ImageView mic;
    TextView inputTextView;
    TextView resultTextView;

    private static final int SPEECH_INPUT=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button =findViewById(R.id.StartButton);

        mic = findViewById(R.id.mic);
        inputTextView = findViewById(R.id.inputTextView); // 입력된 문장을 표시할 TextView
        resultTextView = findViewById(R.id.resultTextView);

        mic.setOnClickListener(new View.OnClickListener() {
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
        });
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent =new Intent(getApplicationContext(),AdjustActivity.class);
                startActivity(intent);
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String speechInput = Objects.requireNonNull(result).get(0);
                inputTextView.setText("입력된 문장: " + speechInput); // 입력된 문장을 TextView에 표시
                resultTextView.setText(""); // 이전 결과를 초기화

                // 키워드 추출
                KeywordExtractor keywordExtractor = new KeywordExtractor();
                KeywordInfo keywordInfo = keywordExtractor.extractKeywords(speechInput);

                // 키워드 결과 출력
                String keywordMessage = "키워드 추출 결과: \n";
                keywordMessage += "당장 할건지 여부: " + keywordInfo.isNow + "\n";
                if (!keywordInfo.isNow) {
                    keywordMessage += "시: " + keywordInfo.hour + "\n";
                    if (keywordInfo.minute != -1) {
                        keywordMessage += "분: " + keywordInfo.minute + "\n";
                    }
                }
                keywordMessage += "온도: " + keywordInfo.temperature + "도";

                // 결과를 TextView에 설정
                resultTextView.setText(keywordMessage);
            }
        }
    }
    class KeywordExtractor {
        public KeywordInfo extractKeywords(String input) {
            KeywordInfo keywordInfo = new KeywordInfo();

            // Use regular expressions to extract the keywords
            Pattern nowPattern = Pattern.compile("지금|현재|당장");
            Pattern hourPattern = Pattern.compile("(\\d+)시"); // 변경된 부분
            Pattern minutePattern = Pattern.compile("(\\d+)분"); // 변경된 부분
            Pattern temperaturePattern = Pattern.compile("(\\d+)도");

            Matcher nowMatcher = nowPattern.matcher(input);
            Matcher hourMatcher = hourPattern.matcher(input);
            Matcher minuteMatcher = minutePattern.matcher(input);
            Matcher temperatureMatcher = temperaturePattern.matcher(input);

            if (nowMatcher.find()) {
                keywordInfo.isNow = true;
            }
            else {
                keywordInfo.isNow = false;
            }

            if (hourMatcher.find()) {
                keywordInfo.hour = Integer.parseInt(hourMatcher.group(1));
            }

            if (minuteMatcher.find()) {    // 5분뒤에 물끓여
                keywordInfo.minute = Integer.parseInt(minuteMatcher.group(1));
            }
            else {
                keywordInfo.minute = 0;
            }

            if (temperatureMatcher.find()) {
                keywordInfo.temperature = Integer.parseInt(temperatureMatcher.group(1));
            }

            return keywordInfo;
        }
    }

    class KeywordInfo {
        boolean isNow = true;
        int hour = -1;
        int minute = -1;
        int temperature = 100;
    }
}