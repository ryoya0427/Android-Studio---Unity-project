package com.example.androidproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.DefaultCompany.com.unity.template.mobile2D.R;

public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // activity_main.xml에 버튼이 있다고 가정

        Button startGameBtn = findViewById(R.id.newGameBtn);
        Button continueBtn = findViewById(R.id.continueBtn);
        Button optionBtn = findViewById(R.id.optionBtn);
        mediaPlayer=mediaPlayer.create(this, R.raw.titlemusic_shieldoffear);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, com.unity3d.player.UnityPlayerActivity.class);
                startActivity(intent);
            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, optionPage.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        // 액티비티가 중지될 때 음악도 중지
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 액티비티가 다시 활성화되면 음악 재생 재개
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 액티비티가 종료될 때 MediaPlayer 자원 해제
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
