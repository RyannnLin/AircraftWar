package dao;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class RankDaoImpl implements RankDao {
    private String path;
    private List<Rank> ranks = new LinkedList<Rank>();

    public RankDaoImpl(String path) {
        this.path = path;
    }

    //读取已有的排名信息
    @Override
    public void readFile() throws IOException {
        try {
            //查看文件是否存在，若不存在，则新建
            File file = new File(path);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            List<String> lines = Files.readAllLines(Paths.get(path));
            if (lines.size() == 0) {
                return;
            }
            for (String item : lines) {
                String[] temp = item.split("\t");
                String userName = temp[0];
                int score = Integer.parseInt(temp[1]);
                String time = temp[2];
                Rank rank = new Rank(userName, score, time);
                ranks.add(rank);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //将ranks列表写回文件(因要更新排名信息，所以覆盖原有数据)
    @Override
    public void writeFile() throws IOException {
        try {
            PrintWriter pw = new PrintWriter(path);
            for (Rank item : ranks) {
                pw.write(item.getUserName() + '\t');
                pw.write("" + item.getScore() + '\t');
                pw.write(item.getTime() + '\n');
            }
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //按得分由高到低插入本次游戏的得分数据
    @Override
    public void insertRank(Rank rank) {
        int mark = 0;
        if (ranks.size() == 0) {
            ranks.add(rank);
        } else {
            for (Rank item : ranks) {
                if (item.getScore() < rank.getScore()) {
                    break;
                }
                mark++;
            }
            ranks.add(mark, rank);
        }
    }

    @Override
    public void displayRank() {
        System.out.println("*******************************************");
        System.out.println("                 得分排行榜                 ");
        System.out.println("*******************************************");
        int ranking = 1;
        for (Rank item : ranks) {
            System.out.println("第" + ranking + "名: " + item.getUserName() + "," + item.getScore() + "," + item.getTime());
            ranking++;
        }
    }

    @Override
    public String getRanks() {
        String message = "";
        for (Rank item : ranks) {
            message = message + item.getUserName() + '\t' + item.getScore() + '\t' + item.getTime() + ',';
        }
        return message;
    }

    @Override
    public void deleteData(int row) {
        ranks.remove(row);
    }
}

