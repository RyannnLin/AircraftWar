package edu.hitsz.aircraft.enemy;

import java.util.List;

import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.supply.AbstractSupply;
import edu.hitsz.supply.BombSupply.BombSubscriber;

/**
 * @author 林子睿
 * @version 2023.3.22
 * 修改getSupply()的返回类型为List<AbstractSupply>，使Boss机能生成3个道具
 */
public abstract class AbstractEnemy extends AbstractAircraft implements BombSubscriber {
    protected int mode;


    public void setMode(int mode) {
        this.mode = mode;
    }
    public void setHp(int hp){
        this.hp = hp;
    }

    public AbstractEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= MainActivity.screenHeight) {
            vanish();
        }
    }

    /**
     * 敌机产生道具
     * @return List<AbstractSupply> 产生的道具
     */
    public abstract List<AbstractSupply> getSupply();


}
