package edu.hitsz.scoreDAO;

import java.util.List;

/**
 * @author 林子睿
 */
public interface ScoreDAO{
    void setFileName(int mode);
    /**
     * 添加得分记录到txt文件中
     * @param score 含玩家名称，分数，时间，难度
     */
    void addScore(Score score);

    /**
     * 从txt文件中读取分数，保存到List<scores>中
     */
    void readScore();

    /**
     * 列出所有记录
     */
    void listAllScores();
    List<Score> getAllScores();
    void deleteScore(Score score);


}
