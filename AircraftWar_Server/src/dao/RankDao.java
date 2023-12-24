package dao;

import java.io.IOException;
import java.util.List;

public interface RankDao {
    void readFile() throws IOException;
    void writeFile() throws IOException;
    void displayRank();
    void insertRank(Rank rank);
    String getRanks();
    void deleteData(int row);
}

