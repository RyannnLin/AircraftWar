package edu.hitsz.aircraft.enemy.EliEnemy.GetSupplyStrategy;

import edu.hitsz.supply.AbstractSupply;
import edu.hitsz.supply.BombSupply.BombFactory;
import edu.hitsz.supply.FireSupply.FireFactory;
import edu.hitsz.supply.HpSupply.HpFactory;
import edu.hitsz.supply.supplyFactory;

import java.util.LinkedList;
import java.util.List;

public class HardGetSupply extends AbstractGetSupply{
    public HardGetSupply(int locationX, int locationY) {
        super(locationX, locationY);
    }

    @Override
    public List<AbstractSupply> getSupply() {
        supplyFactory factory;//创建工厂
        if (probability == -1) {
            setProbability(Math.random() * 10);
        }
        //设置概率
        if (probability < 2) {
            //创建加血道具
            factory = new HpFactory();
        } else if (probability < 4) {
            //创建火力道具
            factory = new FireFactory();
        } else if (probability < 5) {
            //创建炸弹道具
            factory = new BombFactory();
        } else {
            factory = null;
        }
        //随机产生道具
        List<AbstractSupply> supplies = new LinkedList<>();
        if (factory != null) {
            supplies.add(factory.creatSupply(
                    locationX,
                    locationY,
                    0,
                    3));
        }
        return supplies;
    }
}
