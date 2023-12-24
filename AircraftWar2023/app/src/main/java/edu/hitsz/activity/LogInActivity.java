package edu.hitsz.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.hitsz.ClientThread;
import edu.hitsz.R;

public class LogInActivity extends AppCompatActivity {
    public static final String SP_INFOS = "SPData_Files"; //符号常量——配置文件名
    public static final int PORT = 9997;//本地端口号

    private static EditText usernameText;
    private static EditText passwordText;
    private static String username;
    private static String password;

    private Handler handler;
    public ClientThread loginClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sp = getSharedPreferences(SP_INFOS, MODE_PRIVATE); //获得SharedPreferences
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        //按钮
        Button loginButton = findViewById(R.id.login_btn);
        Button registerButton = findViewById(R.id.to_register_btn);
        Button offlineButton = findViewById(R.id.offline_btn);


        //服务器连接
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.i("pairing result", msg.obj.toString());
            }
        };
        loginClient = new ClientThread(PORT, handler);
        new Thread(loginClient).start();

        /**
         * 登录按钮
         */
        loginButton.setOnClickListener(view -> {
            //文本框
            usernameText = (EditText) findViewById(R.id.editTextUsername);
            passwordText = (EditText) findViewById(R.id.editTextPassword);
            //用户名和密码文本
            username = usernameText.getText().toString().trim();
            password = passwordText.getText().toString().trim();


            //判断用户名是否正确
            if (!username.isEmpty() && !password.isEmpty()) {
                boolean ok = isLoginValid(username, password);
                if (ok) {
                    Toast.makeText(LogInActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    usernameText.setText("");
                    passwordText.setText("");
                    MainActivity.ONLINE=true;
                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    Toast.makeText(LogInActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * 注册按钮
         */
        registerButton.setOnClickListener(view -> {
            Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        /**
         * 单机模式按键
         */
        offlineButton.setOnClickListener(view -> {
            Toast.makeText(LogInActivity.this, "单机模式", Toast.LENGTH_SHORT).show();
            MainActivity.ONLINE = false;
            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
            intent.putExtra("username", "localPlayer");
            startActivity(intent);
        });
    }

    /**
     * 登录是否成功
     *
     * @param userName 用户名
     * @param password 密码
     * @return 用户名和密码是否与数据库中的数据匹配
     */
    private boolean isLoginValid(String userName, String password) {
        // TODO
        Log.i(userName, password);
        Message msg = new Message();
        msg.obj = (String) (userName + " " + password);
        loginClient.toserverHandler.sendMessage(msg);
        while (loginClient.msgFromServer.isEmpty()) {
        }
        Log.i("result", loginClient.msgFromServer);
        return loginClient.msgFromServer.trim().equals("success");
    }
}

