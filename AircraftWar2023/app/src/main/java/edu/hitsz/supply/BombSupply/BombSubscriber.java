package edu.hitsz.supply.BombSupply;

/**
 * 炸弹道具bombSupply的订阅者接口
 */
public interface BombSubscriber {
    /**
     * 英雄机撞击炸弹道具时，炸弹生效，订阅者update状态
     */
    void update();
}
