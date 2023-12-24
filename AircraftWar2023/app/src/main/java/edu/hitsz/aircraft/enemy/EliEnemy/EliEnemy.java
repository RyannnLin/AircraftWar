package edu.hitsz.aircraft.enemy.EliEnemy;

import java.util.List;

import edu.hitsz.aircraft.ShootStrategy.Straight;
import edu.hitsz.aircraft.enemy.AbstractEnemy;
import edu.hitsz.aircraft.enemy.EliEnemy.GetSupplyStrategy.AbstractGetSupply;
import edu.hitsz.aircraft.enemy.EliEnemy.GetSupplyStrategy.CommonGetSupply;
import edu.hitsz.aircraft.enemy.EliEnemy.GetSupplyStrategy.EasyGetSupply;
import edu.hitsz.aircraft.enemy.EliEnemy.GetSupplyStrategy.HardGetSupply;
import edu.hitsz.game.EasyGame;
import edu.hitsz.game.MediumGame;
import edu.hitsz.supply.AbstractSupply;

/**
 * @author 林子睿
 * @version 2023.5.1
 */
public class EliEnemy extends AbstractEnemy {

    public EliEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    /**
     * 通过策略模式实现不同模式下道具的生成
     * @return supplies:道具集合
     */
    @Override
    public List<AbstractSupply> getSupply() {
        AbstractGetSupply gs;
        if (mode == EasyGame.EASYGAME) {
            gs = new EasyGetSupply(locationX, locationY);
        } else if (mode == MediumGame.MEDIUMGAME) {
            gs = new CommonGetSupply(locationX, locationY);
        } else {
            gs = new HardGetSupply(locationX, locationY);
        }
        return gs.getSupply();
    }
    @Override
    public void setShootStrategy() {
        shootStrategy = new Straight();
    }

    /**
     * 英雄机撞击炸弹道具时，炸弹生效，直接坠毁，不掉落道具
     */
    @Override
    public void update() {
        vanish();
    }
}
