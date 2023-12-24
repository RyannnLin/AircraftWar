package edu.hitsz.scoreDAO;

import java.time.LocalDateTime;

/**
 * @author 林子睿
 */
public class Score {
    private String playerName;
    private int score;
    private LocalDateTime time;

    public Score(String playerName,int score, LocalDateTime time){
        this.playerName = playerName;
        this.score = score;
        this.time = time;
    }

    public String getPlayerName() {
        return playerName;
    }
    public int getScore(){
        return score;
    }
    public LocalDateTime getTime() {
        return time;
    }
}
