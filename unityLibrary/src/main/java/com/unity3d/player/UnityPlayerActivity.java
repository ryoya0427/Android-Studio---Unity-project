package com.unity3d.player;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup; // ViewGroup 클래스 import 추가
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;

public class UnityPlayerActivity extends Activity implements IUnityPlayerLifecycleEvents {
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
    // 현재 무기 번호를 저장할 변수
    private int repairClickCount = 0; // 총 수리 클릭 카운트
    private static final int REQUIRED_REPAIR_CLICKS = 10; // 수리 완료에 필요한 클릭 수
    private int currentWeaponNumber = 1;
    private DrawerLayout slidingDrawer;
    private ImageView gunIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        mUnityPlayer = new UnityPlayer(this, this);

        // 부모 뷰가 있는 경우 기존 부모에서 제거
        if (mUnityPlayer != null) {
            Log.d("UnityPlayerActivity", "UnityPlayer initialized successfully");
        }

        FrameLayout layout = new FrameLayout(this);
        layout.addView(mUnityPlayer);
        setContentView(layout);
        mUnityPlayer.requestFocus();

        // 버튼 추가
        createButtons(layout);
        // 슬라이딩 드로어 추가
        createSlidingDrawer(layout); // 호출 추가
        if (mUnityPlayer == null) {
            Log.e("UnityPlayerActivity", "UnityPlayer is null. Event ignored.");
            return;
        }
    }
    private void createSlidingDrawer(FrameLayout layout) {
        slidingDrawer = new DrawerLayout(this);
        slidingDrawer.setLayoutParams(new DrawerLayout.LayoutParams(
                DrawerLayout.LayoutParams.MATCH_PARENT,
                DrawerLayout.LayoutParams.MATCH_PARENT
        ));
        Log.d("UnityPlayerActivity", "Drawer added to layout successfully.");

        // 드로어 내부에 총기 아이콘 추가
        gunIcon = new ImageView(this);
        gunIcon.setImageResource(R.drawable.gunicon); // 예시 아이콘 (교체 가능)
        gunIcon.setPadding(50, 50, 50, 50);

        // 드로어 컨테이너 레이아웃
        FrameLayout drawerContent = new FrameLayout(this);
        drawerContent.addView(gunIcon, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
        ));

        // 드로어 설정
        DrawerLayout.LayoutParams drawerParams = new DrawerLayout.LayoutParams(
                DrawerLayout.LayoutParams.MATCH_PARENT,
                DrawerLayout.LayoutParams.WRAP_CONTENT
        );
        drawerParams.gravity = Gravity.RIGHT; // 하단 드로어 위치
        slidingDrawer.addView(drawerContent, drawerParams);

        // 레이아웃에 드로어 추가
        layout.addView(slidingDrawer);

        // 총기 아이콘 클릭 이벤트
        gunIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repairClickCount++;
                if (repairClickCount >= REQUIRED_REPAIR_CLICKS) {
                    // Unity로 메시지 전송 (총기 수리 완료)
                    UnityPlayer.UnitySendMessage("GameManager", "OnRepairCompleted", "");
                    repairClickCount = 0; // 클릭 카운트 초기화
                    Toast.makeText(UnityPlayerActivity.this, "총기 수리가 완료되었습니다!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UnityPlayerActivity.this, "총기 수리 진행 중: " + repairClickCount + "/" + REQUIRED_REPAIR_CLICKS, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void createButtons(FrameLayout layout) {
        // 상단 좌측 버튼 ("무기 스왑")
        Button btnWeaponSwap = new Button(this);
        btnWeaponSwap.setText("무기 스왑");
        btnWeaponSwap.setBackgroundColor(0xFFB392F0); // 보라색 배경
        FrameLayout.LayoutParams btnWeaponSwapParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        btnWeaponSwapParams.gravity = Gravity.START | Gravity.TOP;
        btnWeaponSwapParams.setMargins(20, 20, 0, 0);
        layout.addView(btnWeaponSwap, btnWeaponSwapParams);

        // 하단 좌측 버튼 ("<-")
        Button btnControlleft = new Button(this);
        btnControlleft.setText("<-");
        btnControlleft.setBackgroundColor(0xFFB392F0); // 보라색 배경
        FrameLayout.LayoutParams btnControlleftParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        btnControlleftParams.gravity = Gravity.START | Gravity.BOTTOM;
        //btnControlleftParams.setMargins(20, 0, 0, 20);
        btnControlleftParams.setMargins(100, 0, 0, 100);
        layout.addView(btnControlleft, btnControlleftParams);

        // 하단 좌측 버튼 ("->")
        Button btnControlright = new Button(this);
        btnControlright.setText("->");
        btnControlright.setBackgroundColor(0xFFB392F0); // 보라색 배경
        FrameLayout.LayoutParams btnControlrightParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        btnControlrightParams.gravity = Gravity.START | Gravity.BOTTOM;
        btnControlrightParams.setMargins(20, 0, 0, 20);
        btnControlrightParams.setMargins(300, 0, 0, 100);
        layout.addView(btnControlright, btnControlrightParams);

        // 하단 좌측 버튼 ("점프")
        Button btnControljump = new Button(this);
        btnControljump.setText("점프");
        btnControljump.setBackgroundColor(0xFFB392F0); // 보라색 배경
        FrameLayout.LayoutParams btnControljumpParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        btnControljumpParams.gravity = Gravity.START | Gravity.BOTTOM;
        btnControljumpParams.setMargins(20, 0, 0, 20);
        btnControljumpParams.setMargins(0, 0, 0, 100);
        layout.addView(btnControljump, btnControljumpParams);

        // 하단 우측 버튼 ("구르기")
        Button btnDown = new Button(this);
        btnDown.setText("구르기");
        btnDown.setBackgroundColor(0xFFB392F0); // 보라색 배경
        FrameLayout.LayoutParams btnDownParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        btnDownParams.gravity = Gravity.END | Gravity.BOTTOM;
        btnDownParams.setMargins(0, 0, 20, 20);
        layout.addView(btnDown, btnDownParams);

        // 버튼 클릭 리스너 설정
        btnWeaponSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentWeaponNumber++;

                // 무기 번호가 4를 넘어가면 다시 1로 설정
                if (currentWeaponNumber > 4){
                    currentWeaponNumber = 1;
                }

                // 숫자에 맞는 KeyEvent 생성 (1, 2, 3, 4)
                int keyCode;
                switch (currentWeaponNumber) {
                    case 1:
                        keyCode = KeyEvent.KEYCODE_1;
                        break;
                    case 2:
                        keyCode = KeyEvent.KEYCODE_2;
                        break;
                    case 3:
                        keyCode = KeyEvent.KEYCODE_3;
                        break;
                    case 4:
                        keyCode = KeyEvent.KEYCODE_4;
                        break;
                    default:
                        keyCode = KeyEvent.KEYCODE_1; // 기본값으로 1 설정
                }

                // KeyEvent 생성 및 Unity로 전달
                KeyEvent keyDownEvent = new KeyEvent(KeyEvent.ACTION_DOWN, keyCode);
                KeyEvent keyUpEvent = new KeyEvent(KeyEvent.ACTION_UP, keyCode);
                mUnityPlayer.injectEvent(keyDownEvent);
                mUnityPlayer.injectEvent(keyUpEvent);

                // 현재 무기 번호를 토스트로 표시
                Toast.makeText(UnityPlayerActivity.this, "무기 변경: " + currentWeaponNumber, Toast.LENGTH_SHORT).show();
            }
        });
        btnControlleft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 버튼이 눌렸을 때
                        KeyEvent keyDownEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_A);
                        mUnityPlayer.injectEvent(keyDownEvent);
                        Toast.makeText(UnityPlayerActivity.this, "왼쪽 이동 시작 (A).", Toast.LENGTH_SHORT).show();
                        return true;

                    case MotionEvent.ACTION_UP:
                        // 버튼에서 손을 뗐을 때
                        KeyEvent keyUpEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_A);
                        mUnityPlayer.injectEvent(keyUpEvent);
                        Toast.makeText(UnityPlayerActivity.this, "왼쪽 이동 종료 (A).", Toast.LENGTH_SHORT).show();
                        return true;

                    default:
                        return false;
                }
            }
        });
        btnControlright.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 버튼이 눌렸을 때
                        KeyEvent keyDownEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_D);
                        mUnityPlayer.injectEvent(keyDownEvent);
                        Toast.makeText(UnityPlayerActivity.this, "오른쪽 이동 시작 (D).", Toast.LENGTH_SHORT).show();
                        return true;

                    case MotionEvent.ACTION_UP:
                        // 버튼에서 손을 뗐을 때
                        KeyEvent keyUpEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_D);
                        mUnityPlayer.injectEvent(keyUpEvent);
                        Toast.makeText(UnityPlayerActivity.this, "오른쪽 이동 종료 (D) .", Toast.LENGTH_SHORT).show();
                        return true;

                    default:
                        return false;
                }
            }
        });

        btnControljump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyEvent keyDownEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SPACE);
                KeyEvent keyUpEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_SPACE);
                // UnityPlayer에게 이벤트 전달 (키 눌림과 뗌 모두 전달)
                mUnityPlayer.injectEvent(keyDownEvent);
                mUnityPlayer.injectEvent(keyUpEvent);
                Toast.makeText(UnityPlayerActivity.this, "Jump 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyEvent keyDownEvent = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_CTRL_LEFT);
                KeyEvent keyUpEvent = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_CTRL_LEFT);
                mUnityPlayer.injectEvent(keyDownEvent);
                mUnityPlayer.injectEvent(keyUpEvent);

                // 토스트 메시지로 표시
                Toast.makeText(UnityPlayerActivity.this, "구르기 버튼 클릭됨", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // UnityPlayer가 언로드될 때 호출
    @Override
    public void onUnityPlayerUnloaded() {
        moveTaskToBack(true);
    }

    // UnityPlayer가 종료될 때 호출
    @Override
    public void onUnityPlayerQuitted() {
        // 종료 시 특별히 처리할 내용이 없다면 비워둘 수 있습니다.
    }

    @Override public void onDestroy() { mUnityPlayer.destroy(); super.onDestroy(); }
    @Override protected void onStop() { super.onStop(); mUnityPlayer.onStop(); }
    @Override protected void onStart() { super.onStart(); mUnityPlayer.onStart(); }
    @Override protected void onPause() { super.onPause(); mUnityPlayer.onPause(); }
    @Override protected void onResume() { super.onResume(); mUnityPlayer.onResume(); }
    @Override public void onLowMemory() { super.onLowMemory(); mUnityPlayer.lowMemory(); }
    @Override public void onTrimMemory(int level) { super.onTrimMemory(level); if (level == TRIM_MEMORY_RUNNING_CRITICAL) mUnityPlayer.lowMemory(); }
    @Override public void onConfigurationChanged(Configuration newConfig) { super.onConfigurationChanged(newConfig); mUnityPlayer.configurationChanged(newConfig); }
    @Override public void onWindowFocusChanged(boolean hasFocus) { super.onWindowFocusChanged(hasFocus); mUnityPlayer.windowFocusChanged(hasFocus); }
    @Override public boolean dispatchKeyEvent(KeyEvent event) { if (event.getAction() == KeyEvent.ACTION_MULTIPLE) return mUnityPlayer.injectEvent(event); return super.dispatchKeyEvent(event); }
    @Override public boolean onKeyUp(int keyCode, KeyEvent event) { return mUnityPlayer.onKeyUp(keyCode, event); }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event) { return mUnityPlayer.onKeyDown(keyCode, event); }
    @Override public boolean onTouchEvent(MotionEvent event) { return mUnityPlayer.onTouchEvent(event); }
    @Override public boolean onGenericMotionEvent(MotionEvent event) { return mUnityPlayer.onGenericMotionEvent(event); }
}
