package edu.hitsz.aircraft.ShootStrategy;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.enemy.BossEnemy.BossEnemy;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

/**
 * @author 林子睿
 * 实现英雄机在火力道具下的散射和BOSS敌机的散射
 */
public class Radiation implements ShootStrategy{
    public static String HERO = HeroAircraft.class.getName();
    public static String BOSS = BossEnemy.class.getName();

    /**
     * 子弹一次发射数量
     */
    protected int shootNum = 3;

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
            speedY = aircraft.getSpeedY() + direction * 5;
        } else if (BOSS.equals(aircraft.getClass().getName())) {
            power = 10;
            direction = 1;
            speedY = Math.max(aircraft.getSpeedY()+ direction * 3, direction * 2) ;
        }
        x = aircraft.getLocationX();
        y = aircraft.getLocationY() + direction * 2;
    }

    public AbstractBullet createBullet(AbstractAircraft aircraft) {
        AbstractBullet bullet;
        if (HERO.equals(aircraft.getClass().getName())) {
            bullet = new HeroBullet(x, y, speedX, speedY, power);
        } else if (BOSS.equals(aircraft.getClass().getName())) {
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
        /**
         * 创建不同方向的三个子弹
         * */
        for (int i = 0; i < shootNum; i++) {
            speedX = (i - 1) * 2;
            res.add(createBullet(aircraft));
        }
        return res;
    }
}
