package edu.hitsz.scoreDAO;


import static android.content.Context.MODE_PRIVATE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.hitsz.game.EasyGame;
import edu.hitsz.game.MediumGame;

/**
 * @author 林子睿
 */
public class ScoreDaoImpl implements ScoreDAO {

//    File file = new File("data/data/edu.hitsz/","easyScore.txt");
    public ScoreDaoImpl(){

    }
    private List<Score> scores = new ArrayList<>();

    private String scoreFileName;

    public void setFileName(int mode) {
        if (mode == EasyGame.EASYGAME) {
            scoreFileName = "data/data/edu.hitsz/easyScore.txt";
        } else if (mode == MediumGame.MEDIUMGAME) {
            scoreFileName = "data/data/edu.hitsz/commonScore.txt";
        } else {
            scoreFileName = "data/data/edu.hitsz/hardScore.txt";
        }
    }
    @Override
    public void addScore(Score score) {
        try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(scoreFileName, true), StandardCharsets.UTF_8), true)) {
            out.println(score.getPlayerName() + "|" + score.getScore() + "|" + score.getTime());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readScore() {
        try (Scanner in = new Scanner(new FileInputStream(scoreFileName),"UTF-8")) {
            while (in.hasNextLine()) {
                String[] tokens = in.nextLine().split("\\|");
                String name = tokens[0];
                int score = Integer.parseInt(tokens[1]);
                LocalDateTime time = LocalDateTime.parse(tokens[2]);
                scores.add(new Score(name, score, time));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        scores.sort((s1, s2) -> {
            // TODO Auto-generated method stub
            return s2.getScore() - s1.getScore();
        });
    }


    public List<Score> getAllScores() {
        scores.clear();
        readScore();
        return scores;
    }

    @Override
    public void listAllScores() {
        if (scores.isEmpty()) {
            this.readScore();
        }
        if (scores.isEmpty()) {
            System.out.println("No score record, start a game now!");
        } else {
            scores.forEach(Score -> System.out.println(
                    Score.getPlayerName() + "," + Score.getScore() + "," + DateTimeFormatter.ofPattern("MM-dd HH:mm").format(Score.getTime())));
        }
    }

    /**
     * 根据score删除数据
     * @param score 要删除的数据
     */
    @Override
    public void deleteScore(Score score) {
        try {
            File inputFile = new File(scoreFileName);
            File tempFile = new File("temp.txt");
            RandomAccessFile raf = new RandomAccessFile(inputFile, "rw");
            RandomAccessFile tempRaf = new RandomAccessFile(tempFile, "rw");
            scores.remove(score);
            for(Score s:scores){
                try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(tempFile, true), StandardCharsets.UTF_8), true)) {
                    out.println(s.getPlayerName() + "|" + s.getScore() + "|" + s.getTime());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            raf.close();
            tempRaf.close();
            inputFile.delete();
            tempFile.renameTo(inputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Delete data successful!");
    }
}
