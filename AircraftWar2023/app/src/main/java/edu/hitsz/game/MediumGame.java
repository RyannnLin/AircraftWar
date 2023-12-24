package edu.hitsz.game;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;

import edu.hitsz.ImageManager;
import edu.hitsz.R;
import edu.hitsz.aircraft.enemy.AbstractEnemy;
import edu.hitsz.aircraft.enemy.BossEnemy.BossEnemy;
import edu.hitsz.aircraft.enemy.BossEnemy.BossFactory;
import edu.hitsz.aircraft.enemy.EliEnemy.EliEnemyFactory;
import edu.hitsz.aircraft.enemy.EnemyFactory;
import edu.hitsz.aircraft.enemy.MobEnemy.MobEnemyFactory;

public class MediumGame extends BaseGame{
    public static int MEDIUMGAME = 1;
    public MediumGame(Context context, Handler handler) {
        super(context,handler);
        super.enemyMaxNumber = 5;
        this.backGround = ImageManager.BACKGROUND2_IMAGE;
    }

    @Override
    protected void creatGameEnemy() {
        if (enemyAircrafts.size() < enemyMaxNumber) {
            EnemyFactory enemyFactory;
            if (Math.random() * 10 > 7) {
                enemyFactory = new EliEnemyFactory(MEDIUMGAME);
            } else {
                enemyFactory = new MobEnemyFactory(MEDIUMGAME);
            }
            enemyAircrafts.add(enemyFactory.creatEnemy());
        }
        //创建boss机
        if (score >= 600 && score % 600 < 30) {
            boolean flag = true;
            for (AbstractEnemy enemy : enemyAircrafts) {
                if (BossEnemy.class.getName().equals(enemy.getClass().getName())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                EnemyFactory boss;
                boss = new BossFactory(MEDIUMGAME);
                enemyAircrafts.add(boss.creatEnemy());
                if (isMusicOn) {
                    changeMusic(R.raw.bgm_boss);
                }
            }
        }
    }

    @Override
    protected void scoreSaving() {
        sd.setFileName(MEDIUMGAME);
        super.scoreSaving();
    }
}
