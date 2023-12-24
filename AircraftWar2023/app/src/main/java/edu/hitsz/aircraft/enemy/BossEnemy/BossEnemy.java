package edu.hitsz.aircraft.enemy.BossEnemy;

import java.util.LinkedList;
import java.util.List;

import edu.hitsz.aircraft.ShootStrategy.Radiation;
import edu.hitsz.aircraft.enemy.AbstractEnemy;
import edu.hitsz.supply.AbstractSupply;
import edu.hitsz.supply.BombSupply.BombFactory;
import edu.hitsz.supply.FireSupply.FireFactory;
import edu.hitsz.supply.HpSupply.HpFactory;
import edu.hitsz.supply.supplyFactory;


/**
 * @author 林子睿
 * @version 2023.5.1 区分普通模式和困难模式的boss机移动
 */
public class BossEnemy extends AbstractEnemy {
    /**
     * 攻击方式
     */

    private static int Boss_supplyNum = 3;

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public List<AbstractSupply> getSupply() {
        supplyFactory factory;//创建工厂
        List<AbstractSupply> supplies = new LinkedList<>();
        for (int i = 0; i < Boss_supplyNum; i++) {
            //掉落道具横坐标的调整参数
            int argument = (int) (Math.random() * 100) - 100;
            if (Math.random() * 10 < 3) {
                //创建加血道具
                factory = new HpFactory();
            } else if (Math.random() * 10 < 7.5) {
                //创建火力道具
                factory = new FireFactory();
            } else {
                //创建炸弹道具
                factory = new BombFactory();
            }
            supplies.add(factory.creatSupply(locationX + argument,
                    locationY,
                    i - 1,
                    3));
        }
        //随机产生道具
        return supplies;
    }

    public void forward() {
        super.forward();
        if (locationY >= 150 || locationY <= 0) {
            // 纵向超出边界后反向
            speedY = -speedY;
        }
    }

    @Override
    public void setShootStrategy() {
        shootStrategy = new Radiation();
    }

    /**
     * 英雄机撞击炸弹道具时，炸弹生效，boss机血量减少60
     */
    @Override
    public void update() {
        if (hp > 60) {
            decreaseHp(60);
        } else {
            vanish();
        }
    }
}

