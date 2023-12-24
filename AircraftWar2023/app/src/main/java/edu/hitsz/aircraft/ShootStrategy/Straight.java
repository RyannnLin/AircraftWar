package edu.hitsz.aircraft.ShootStrategy;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.activity.GameActivity;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.enemy.EliEnemy.EliEnemy;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.game.EasyGame;
import edu.hitsz.game.MediumGame;

/**
 * @author 林子睿
 */
public class Straight implements ShootStrategy {
    public static String HERO = HeroAircraft.class.getName();
    public static String ELITE = EliEnemy.class.getName();

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 1;

    /**
     * 子弹伤害
     */
    private int power;

    /**
     * 子弹射击方向 (向上发射：1，向下发射：-1)
     */
    private int direction;

    /**
     * 子弹横坐标
     */
    private int x;

    /**
     * 子弹纵坐标
     */
    private int y;

    /**
     * 子弹横向速度
     */
    private int speedX;

    /**
     * 子弹纵向速度
     */
    private int speedY;

    public void setParam(AbstractAircraft aircraft) {
        if (HERO.equals(aircraft.getClass().getName())) {
            power = 30;
            direction = -1;
        } else if (ELITE.equals(aircraft.getClass().getName())) {
            if (GameActivity.getGameType() == EasyGame.EASYGAME || GameActivity.getGameType() == MediumGame.MEDIUMGAME) {
                power = 10;
            } else {
                power = 15;
            }
            direction = 1;
        }
        x = aircraft.getLocationX();
        y = aircraft.getLocationY() + direction * 2;
        speedX = 0;
        speedY = aircraft.getSpeedY() + direction * 5;
    }

    public AbstractBullet createBullet(AbstractAircraft aircraft) {
        AbstractBullet bullet;
        if (HERO.equals(aircraft.getClass().getName())) {
            bullet = new HeroBullet(x, y, speedX, speedY, power);
        } else if (ELITE.equals(aircraft.getClass().getName())) {
            bullet = new EnemyBullet(x, y, speedX, speedY, power);
        } else {
            bullet = null;
        }
        return bullet;
    }

    @Override
    public List<AbstractBullet> shoot(AbstractAircraft aircraft) {
        setParam(aircraft);
        List<AbstractBullet> res = new LinkedList<>();
        for (int i = 0; i < shootNum; i++) {
            // 子弹发射位置相对飞机位置向前偏移
            res.add(createBullet(aircraft));
        }
        return res;
    }
}
