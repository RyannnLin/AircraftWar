package edu.hitsz.aircraft.enemy.EliEnemy;

import static edu.hitsz.activity.MainActivity.screenWidth;

import edu.hitsz.ImageManager;
import edu.hitsz.aircraft.enemy.AbstractEnemy;
import edu.hitsz.aircraft.enemy.EnemyFactory;
import edu.hitsz.game.EasyGame;
import edu.hitsz.game.MediumGame;

public class EliEnemyFactory implements EnemyFactory {
    private int speedY;
    private int hp;
    public EliEnemyFactory(int mode) {
        if (mode == EasyGame.EASYGAME) {
            speedY = 2;
            hp = 30;
        } else if (mode == MediumGame.MEDIUMGAME) {
            speedY = 3;
            hp = 50;
        } else {
            speedY = 4;
            hp = 50;
        }
    }
    public AbstractEnemy creatEnemy() {
        AbstractEnemy enemy = new EliEnemy((int) (Math.random() * (screenWidth - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * screenWidth * 0.05),
                0,
                speedY,
                hp);
        enemy.setMode(EasyGame.EASYGAME);
        return enemy;
    }

    /**
     * 血量增加控制
     */
    @Override
    public void IncreaseHp() {

    }
}

