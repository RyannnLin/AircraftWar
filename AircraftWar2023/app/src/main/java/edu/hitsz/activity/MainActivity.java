package edu.hitsz.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.hitsz.R;
import edu.hitsz.game.BaseGame;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public volatile static boolean ONLINE = false;//判断是否为联机游戏

    public static int screenWidth;
    public static int screenHeight;
    private static String username = "";


    private int gameType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button medium_btn = findViewById(R.id.medium_btn);
        Button easy_btn = findViewById(R.id.easy_btn);
        Button hard_btn = findViewById(R.id.hard_btn);

        //音乐开关
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch music = findViewById(R.id.musicSetting);
        //排行榜
        Button scorelist_btn = findViewById(R.id.scorelist_btn);

        getScreenHW();
        if (getIntent() != null) {
            username = getIntent().getStringExtra("username");
        }

        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        medium_btn.setOnClickListener(view -> {
            gameType = 1;
            intent.putExtra("gameType", gameType);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        easy_btn.setOnClickListener(view -> {
            gameType = 0;
            intent.putExtra("gameType", gameType);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        hard_btn.setOnClickListener(view -> {
            gameType = 2;
            intent.putExtra("gameType", gameType);
            intent.putExtra("username", username);
            startActivity(intent);
        });
        //音乐开关
        music.setOnCheckedChangeListener(new CheckListener());
        //排行榜
        scorelist_btn.setOnClickListener(view -> {
            Intent scoreIntent = new Intent(MainActivity.this, ScoreListActivity.class);
            BaseGame.getScoreDAO().setFileName(gameType);
            startActivity(scoreIntent);
        });
    }

    public void getScreenHW() {
        //定义DisplayMetrics 对象
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        //窗口的宽度
        screenWidth = dm.widthPixels;
        //窗口高度
        screenHeight = dm.heightPixels;

        Log.i(TAG, "screenWidth : " + screenWidth + " screenHeight : " + screenHeight);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private class CheckListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            String state;
            if (isChecked) {
                BaseGame.isMusicOn = true;
                state = "开";
            } else {
                BaseGame.isMusicOn = false;
                state = "关";
            }
            String showText = "音乐" + state;
            Toast.makeText(MainActivity.this, showText, Toast.LENGTH_LONG).show();
        }

    }
}