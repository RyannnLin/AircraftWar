package edu.hitsz.aircraft.ShootStrategy;

import java.util.List;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.AbstractBullet;

/**
 * 射击实现
 * @author 林子睿
 */
public interface ShootStrategy {
    /**
     * 设置shoot中的参数
     * @param aircraft 飞机
     */
    void setParam(AbstractAircraft aircraft);

    /**
     * 创建子弹
     * @param aircraft 飞机
     * @return bullet 子弹
     */
    AbstractBullet createBullet(AbstractAircraft aircraft);
    /**
     * 射击方法
     * @param aircraft 飞机
     * @return bullet
     */
    List<AbstractBullet> shoot(AbstractAircraft aircraft);
}
