package server;

import dao.Rank;
import dao.RankDaoImpl;

import java.io.IOException;

public class RankOnline {
    // 弹窗，输入玩家姓名
    int difficulty;
    String name;
    String filename;
    int currentScore;

    public RankOnline(int difficulty, String name, int currentScore) {
        this.difficulty = difficulty;
        this.name = name;
        this.currentScore = currentScore;
        if (difficulty == 0) {
            filename = "src/score_EasyGame.txt";
        }
        if (difficulty == 1) {
            filename = "src/score_MediumGame.txt";
        }
        if (difficulty == 2) {
            filename = "src/score_HardGame.txt";
        }
    }


    public String rankBench() throws IOException {    // 创建Score对象
        Rank currentRank = new Rank(name, currentScore);
        RankDaoImpl rankDao = new RankDaoImpl(filename);
        rankDao.readFile();
        rankDao.insertRank(currentRank);
        String rankList = rankDao.getRanks();
        rankDao.displayRank();
        rankDao.writeFile();
        return rankList;
    }
}