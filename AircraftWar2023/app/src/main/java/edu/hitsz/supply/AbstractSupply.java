package edu.hitsz.supply;



import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.supply.BombSupply.BombSubscriber;

/**
 * 所有掉落道具的父类
 * @author 林子睿
 */

public abstract class AbstractSupply extends AbstractFlyingObject {

    public AbstractSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }
    /**
     * 实现道具的功能
     */
    public abstract void function();
    public abstract void addSubscriber(BombSubscriber object);
}
