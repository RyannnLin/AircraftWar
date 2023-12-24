package edu.hitsz.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.hitsz.ClientThread;
import edu.hitsz.R;

//TODO 处理“记住我”
public class RegisterActivity extends AppCompatActivity {
    public static final int PORT = 9998;//本地端口号

    public static final String SP_INFOS = "SPData_Files"; //符号常量——配置文件名
    public static final String USERID = "UserID";          //符号常量——键值对的帐号键名
    public static final String PASSWORD = "PassWord";     //符号常量——键值对的密码键名
    private static EditText etUid;                  //接收用户id组件
    private static EditText etPwd;                  //接收用户密码组件
    private static CheckBox cb;                     //"记住我"复选框组件
    private static String uidstr;                   //用户帐号
    private static String pwdstr;                   //用户密码

    public ClientThread registerClient;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Button regBtn = findViewById(R.id.register_btn);

//        checkIfRemember();             //从SharedPreferences中读取用户的帐号和密码

        //连接服务器
        handler = new Handler(getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.i("register result", msg.obj.toString());
            }
        };
        registerClient = new ClientThread(PORT, handler);
        new Thread(registerClient).start();

        //注册
        regBtn.setOnClickListener(v -> {
            etUid = (EditText) findViewById(R.id.rgt_editTextUsername);       //获得帐号EditText
            etPwd = (EditText) findViewById(R.id.rgt_editTextPassword);       //获得密码EditText
            uidstr = etUid.getText().toString().trim();         //获得输入的帐号
            pwdstr = etPwd.getText().toString().trim();         //获得输入的密码


            //将账号密码传输到服务器
            if (!uidstr.isEmpty() && !pwdstr.isEmpty()) {
                Message msg = new Message();
                msg.obj = (String) (uidstr + " " + pwdstr);
                registerClient.toserverHandler.sendMessage(msg);
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                rememberMe(uidstr, pwdstr);     //将用户的帐号与密码存入SharedPreferences
                Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(RegisterActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
            }

        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        uidstr = etUid.getText().toString().trim();         //获得输入的帐号
        pwdstr = etPwd.getText().toString().trim();         //获得输入的密码
        rememberMe(uidstr, pwdstr);     //将用户的帐号与密码存入SharedPreferences

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //方法：从SharedPreferences中读取用户的帐号和密码
    public void checkIfRemember() {
        SharedPreferences sp = getSharedPreferences(SP_INFOS, MODE_PRIVATE); //获得SharedPreferences
        uidstr = sp.getString(USERID, null);              //取键值对中的帐号值
        pwdstr = sp.getString(PASSWORD, null);            //取键值对中的密码值
        if (!uidstr.isEmpty() && !pwdstr.isEmpty()) {
            etUid.setText(uidstr);                        //给EditText控件赋帐号
            etPwd.setText(pwdstr);                        //给EditText控件赋密码
        }
    }

    //方法：将用户的id和密码存入SharedPreferences
    public void rememberMe(String uid, String pwd) {
        SharedPreferences sp = getSharedPreferences(SP_INFOS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();        //获得Editor
        editor.putString(USERID, uid);                        //将用户的帐号存入Preferences
        editor.putString(PASSWORD, pwd);                    //将密码存入Preferences
        editor.commit();
        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
        Toast.makeText(RegisterActivity.this, "用户名" + uidstr, Toast.LENGTH_SHORT).show();
    }
}
