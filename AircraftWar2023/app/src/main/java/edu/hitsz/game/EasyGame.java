package edu.hitsz.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;

import edu.hitsz.ImageManager;
import edu.hitsz.aircraft.enemy.EliEnemy.EliEnemyFactory;
import edu.hitsz.aircraft.enemy.EnemyFactory;
import edu.hitsz.aircraft.enemy.MobEnemy.MobEnemyFactory;
import edu.hitsz.scoreDAO.ScoreDaoImpl;


public class EasyGame extends BaseGame{
    public final static int EASYGAME = 0;
    public EasyGame(Context context, Handler handler) {
        super(context,handler);
        super.enemyMaxNumber = 5;
        this.backGround = ImageManager.BACKGROUND1_IMAGE;
    }

    @Override
    protected void cycleDurationController() {

    }

    @Override
    protected void creatGameEnemy() {
        //创建普通敌机和精英敌机
        if (enemyAircrafts.size() < enemyMaxNumber) {
            EnemyFactory enemyFactory;
            if (Math.random() * 10 > 8) {
                enemyFactory = new EliEnemyFactory(EASYGAME);
            } else {
                enemyFactory = new MobEnemyFactory(EASYGAME);
            }
            enemyAircrafts.add(enemyFactory.creatEnemy());
        }
    }

    @Override
    protected void scoreSaving() {
        sd.setFileName(EASYGAME);
        super.scoreSaving();
    }
}
