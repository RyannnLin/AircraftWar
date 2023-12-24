package edu.hitsz.aircraft.enemy.MobEnemy;


import java.util.LinkedList;
import java.util.List;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.enemy.AbstractEnemy;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.supply.AbstractSupply;


/**
 * 普通敌机
 * 不可射击
 *
 * @author hitsz
 */
public class MobEnemy extends AbstractEnemy {

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void setShootStrategy() {
        shootStrategy = null;
    }


    @Override
    public List<AbstractSupply> getSupply() {
        return new LinkedList<>();
    }

    @Override
    public List<AbstractBullet> shoot() {
        return new LinkedList<>();
    }

    @Override
    public void update() {
        vanish();
    }
}
