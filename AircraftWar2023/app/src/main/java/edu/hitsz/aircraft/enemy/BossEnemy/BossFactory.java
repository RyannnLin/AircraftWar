package edu.hitsz.aircraft.enemy.BossEnemy;

import static edu.hitsz.activity.MainActivity.screenHeight;
import static edu.hitsz.activity.MainActivity.screenWidth;

import edu.hitsz.aircraft.enemy.AbstractEnemy;
import edu.hitsz.aircraft.enemy.EnemyFactory;
import edu.hitsz.ImageManager;
import edu.hitsz.game.MediumGame;

/**
 * @author 林子睿
 * @version 2023.5.1 区分普通模式和困难模式的boss机移动
 */
public class BossFactory implements EnemyFactory {
    private int hp = 200;
    private int hpIncrease = 0;
    public void IncreaseHp(){
        hpIncrease++;
    }
    private int mode;
    private int speedY;

    public BossFactory(int mode) {
        this.mode = mode;
        if (mode == MediumGame.MEDIUMGAME) {
            speedY = 0;
        } else {
            hp = 300 + hpIncrease * 100;
            speedY = 2;
        }
    }

    public AbstractEnemy creatEnemy() {
        AbstractEnemy boss = new BossEnemy((int) (Math.random() * (screenWidth - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * screenHeight * 0.05),
                3,
                speedY,
                hp);
        boss.setMode(mode);
        return boss;
    }
}
