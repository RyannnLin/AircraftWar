package edu.hitsz.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.hitsz.ClientThread;
import edu.hitsz.R;
import edu.hitsz.game.BaseGame;
import edu.hitsz.game.EasyGame;
import edu.hitsz.game.MediumGame;
import edu.hitsz.scoreDAO.Score;

public class ScoreListActivity extends AppCompatActivity {
    private ListView listv;
    public static final int PORT = 9996;
    private Handler handler;
    public ClientThread scoreClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scorelist);
        listv = this.findViewById(R.id.ScoreListView);
        MyAdapter BAdapter = new MyAdapter(this);//得到一个MyAdapter对象
        listv.setAdapter(BAdapter);  //为ListView绑定Adapter
        setListTitle();

        //删除数据的弹窗
        //添加"Yes"按钮
        AlertDialog alertDialog2 = new AlertDialog.Builder(this)
                .setTitle("WARNING")
                .setMessage("是否删除这条记录")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("确定", (dialogInterface, i) -> {
                    Toast.makeText(ScoreListActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    //TODO 读取点击行的数据，进行删除操作
                })

                .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ScoreListActivity.this, "取消", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();

        // 为ListView的每一个item添加点击事件的监听器
        listv.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            setTitle("点击了ListView的第 " + (arg2 + 1) + " 条目");   //在标题栏中输出点击条目信息
            alertDialog2.show();
        });


        Button back = findViewById(R.id.back_btn);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(ScoreListActivity.this, MainActivity.class);
            startActivity(intent);
        });

        //服务器连接
        if(MainActivity.ONLINE){
            handler = new Handler(getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    Log.i("pairing result", msg.obj.toString());
                }
            };
            scoreClient = new ClientThread(PORT, handler);
            new Thread(scoreClient).start();
        }

    }

    /**
     * 设置标题
     */
    private void setListTitle() {
        if (GameActivity.getGameType() == EasyGame.EASYGAME) {
            setTitle("难度：EASY");
        } else if (GameActivity.getGameType() == MediumGame.MEDIUMGAME) {
            setTitle("难度：MEDIUM");
        } else {
            setTitle("难度：HARD");
        }
    }

    // 获得数据的方法
    private ArrayList<HashMap<String, String>> getDate() {
        List<Score> scores = BaseGame.getScoreDAO().getAllScores();
        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
        int rank = 1;
        HashMap<String, String> title = new HashMap<String, String>();
        title.put("rank", "排名");
        title.put("player", "玩家");
        title.put("score", "分数");
        title.put("time", "时间");
        listItem.add(title);
        // 为动态数组添加数据
        for (int i = 0; i < scores.size(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("rank", Integer.toString(rank++));
            map.put("player", scores.get(i).getPlayerName());
            map.put("score", Integer.toString(scores.get(i).getScore()));
            map.put("time", DateTimeFormatter.ofPattern("MM-dd HH:mm").format(scores.get(i).getTime()));
            listItem.add(map);
        }
        return listItem;
    }

    // 定义ViewHolder类，存放控件
    private final class ViewHolder {
        private TextView rank;
        private TextView player;
        private TextView score;
        private TextView time;
    }

    // 定义 BaseAdapter适配器的子类
    private class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;   //声明一个LayoutInfalter对象用来导入布局
        private final ArrayList<HashMap<String, String>> datas;  //声明HashMap类型的数组

        // 构造函数
        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
            this.datas = getDate();  //为HashMap类型的数组赋初值

        }

        @Override
        public int getCount() {
            return datas.size();//返回数组的长度
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 动态生成每个下拉项对应的View。每个下拉项View由LinearLayout中包含一个ImageView，
         * 两个内嵌的LinearLayout构成，其中一个包含两个TextView，另一个中包含一个按钮
         **/
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.scorelistitem, null);
                holder = new ViewHolder();
                // 得到各个控件的对象
                holder.rank = (TextView) convertView.findViewById(R.id.textView1);
                holder.player = (TextView) convertView.findViewById(R.id.textView2);
                holder.score = (TextView) convertView.findViewById(R.id.textView3);
                holder.time = (TextView) convertView.findViewById(R.id.textView4);
                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            HashMap<String, String> map = datas.get(position);

            //设置TextView显示的内容，即我们存放在动态数组中的数据
            holder.rank.setText(map.get("rank"));
            holder.player.setText(map.get("player"));
            holder.score.setText(map.get("score"));
            holder.time.setText(map.get("time"));
            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ScoreListActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
