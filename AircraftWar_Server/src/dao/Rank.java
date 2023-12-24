package dao;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Rank {

    private String userName;
    private int score;
    private String time;

    //新建排名所用的构造器
    public Rank(String userName,int score) {
        this.userName = userName;
        this.score = score;
        //得分时间为构造器被调用时的时间
        time = new SimpleDateFormat("MM-dd HH:mm").format(new Date());
    }

    //读取文件所用的构造器
    public Rank(String userName, int score, String time) {
        this.userName = userName;
        this.score = score;
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
