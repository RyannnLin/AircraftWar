package edu.hitsz.bullet;

import edu.hitsz.supply.BombSupply.BombSubscriber;

/**
 * @Author hitsz
 */
public class EnemyBullet extends AbstractBullet implements BombSubscriber {


    public EnemyBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

    @Override
    public void update() {
        vanish();
    }
}
