package com.example.androidproject;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.DefaultCompany.com.unity.template.mobile2D.R;

public class optionPage extends Activity {

    SeekBar sbSound;
    TextView text1, text2;
    RadioButton rbChoi, rbKim1, rbKim2;
    RadioGroup radioGroup;
    AudioManager audioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.optionpage); // XML 파일을 Java 코드에 연결합니다.

        // XML 파일에 정의된 뷰를 가져옵니다.
        sbSound = findViewById(R.id.sbSound);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);
        rbChoi = findViewById(R.id.rbChoi);
        rbKim1 = findViewById(R.id.rbKim1);
        rbKim2 = findViewById(R.id.rbKim2);
        Button btnBack = findViewById(R.id.btnBack);
        radioGroup = findViewById(R.id.radioGroup);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        // SeekBar 설정
        sbSound.setMax(maxVolume);
        sbSound.setProgress(currentVolume);

        // SeekBar의 이벤트 리스너 설정
        sbSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 사용자가 SeekBar를 조작하기 시작할 때의 동작
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 사용자가 SeekBar를 조작하고 멈췄을 때의 동작
            }
        });

        // 라디오 버튼 그룹의 이벤트 리스너 설정
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbChoi:

                        break;
                    case R.id.rbKim1:

                        break;
                    case R.id.rbKim2:

                        break;
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // 서브 액티비티 종료
            }
        });
    }
}
