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

public class HardGame extends BaseGame{
    public static int HARDGAME = 2;
    public HardGame(Context context, Handler handler) {
        super(context,handler);
        super.enemyMaxNumber = 7;
        this.backGround = ImageManager.BACKGROUND3_IMAGE;
    }

    @Override
    protected void creatGameEnemy() {
        //创建普通敌机和精英敌机
        if (enemyAircrafts.size() < enemyMaxNumber) {
            EnemyFactory enemyFactory;
            if (Math.random() * 10 > 7) {
                enemyFactory = new EliEnemyFactory(HARDGAME);
            } else {
                enemyFactory = new MobEnemyFactory(HARDGAME);
            }
            enemyAircrafts.add(enemyFactory.creatEnemy());
        }
        //创建boss机
        if (score >= 500 && score % 500 < 30) {
            boolean flag = true;
            for (AbstractEnemy enemy : enemyAircrafts) {
                if (BossEnemy.class.getName().equals(enemy.getClass().getName())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                EnemyFactory boss;
                boss = new BossFactory(HARDGAME);
                enemyAircrafts.add(boss.creatEnemy());
                if (isMusicOn) {
                    changeMusic(R.raw.bgm_boss);
                }
                boss.IncreaseHp();
            }
        }
    }

    @Override
    protected void scoreSaving() {
        sd.setFileName(HARDGAME);
        super.scoreSaving();
    }
}
