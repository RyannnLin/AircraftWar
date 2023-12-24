package edu.hitsz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import edu.hitsz.ClientThread;
import edu.hitsz.game.BaseGame;
import edu.hitsz.game.EasyGame;
import edu.hitsz.game.HardGame;
import edu.hitsz.game.MediumGame;

public class GameActivity extends AppCompatActivity {
    private static final String TAG = "GameActivity";
    public static final int PORT = 9999;


    private Handler handler;
    public ClientThread gameClient;

    //游戏参数
    public static int getGameType() {
        return gameType;
    }

    private static int gameType = 0;
    private static String username = "";
    public volatile static int rivalScore;
    public volatile static String rivalName;
    BaseGame basGameView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rivalName = "No rival!";
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d(TAG, "handleMessage");
                Message msgToServer = new Message();
                msgToServer.obj = username + "," + basGameView.score;
                if (msg.what == 1) {
                    Toast.makeText(GameActivity.this, "GameOver", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GameActivity.this, ScoreListActivity.class);
                    startActivity(intent);
                    if (MainActivity.ONLINE) {
//                        msgToServer.what = 1;
                        gameClient.toserverHandler.sendMessage(msgToServer);
                    }
                } else if (msg.what == 0x123) {
                    Log.i("from server", msg.obj.toString());
                    String[] serverData = msg.obj.toString().split(",");
                    if (serverData.length == 2 && !Objects.equals(serverData[0], username)) {
                        rivalName = serverData[0];
                        rivalScore = Integer.parseInt(serverData[1]);
                    }
                    Log.i("rivalScore", String.valueOf(rivalScore));
                } else if (msg.what == 2) {
                    gameClient.toserverHandler.sendMessage(msgToServer);
                }
            }
        };
        //启动线程
        if (MainActivity.ONLINE) {
            gameClient = new ClientThread(PORT, handler);
            new Thread(gameClient).start();
        }
        //读取MainActivity传输的数据
        if (getIntent() != null) {
            gameType = getIntent().getIntExtra("gameType", 3);
            username = getIntent().getStringExtra("username");
        }
        //初始化游戏，设置游戏难度和用户名
        if (gameType == 1) {
            basGameView = new MediumGame(this, handler);
            BaseGame.getScoreDAO().setFileName(EasyGame.EASYGAME);
        } else if (gameType == 2) {
            basGameView = new HardGame(this, handler);
            BaseGame.getScoreDAO().setFileName(MediumGame.MEDIUMGAME);
        } else {
            basGameView = new EasyGame(this, handler);
            BaseGame.getScoreDAO().setFileName(HardGame.HARDGAME);
        }
        basGameView.setUsername(username);
        setContentView(basGameView);
    }

    @Override
    public void onBackPressed() {
        basGameView = null;
        super.onBackPressed();
    }
}