package edu.hitsz.supply.HpSupply;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.supply.AbstractSupply;
import edu.hitsz.supply.BombSupply.BombSubscriber;

/**
 * @author 林子睿
 */
public class HpSupply extends AbstractSupply {


    public HpSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);

    }

    public void function() {
        HeroAircraft.getHeroAircraft().decreaseHp(-30);
    }

    @Override
    public void addSubscriber(BombSubscriber object) {

    }
}

