package edu.hitsz.aircraft.enemy.MobEnemy;

import edu.hitsz.ImageManager;
import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.enemy.AbstractEnemy;
import edu.hitsz.aircraft.enemy.EnemyFactory;
import edu.hitsz.game.EasyGame;
import edu.hitsz.game.MediumGame;

public class MobEnemyFactory implements EnemyFactory {
    private int speedY;
    private int hp;

    public MobEnemyFactory(int mode) {
        if (mode == EasyGame.EASYGAME) {
            speedY = 7;
            hp = 30;
        } else if (mode == MediumGame.MEDIUMGAME) {
            speedY = 8;
            hp = 30;
        } else {
            speedY = 8;
            hp = 30;
        }
    }

    @Override
    public AbstractEnemy creatEnemy() {
        return new MobEnemy(
                (int) (Math.random() * (MainActivity.screenWidth - ImageManager.MOB_ENEMY_IMAGE.getWidth())),
                (int) (Math.random() * MainActivity.screenHeight * 0.05),
                0,
                speedY,
                hp);
    }

    @Override
    public void IncreaseHp() {

    }
}
