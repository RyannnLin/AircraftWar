package edu.hitsz.supply.FireSupply;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.supply.AbstractSupply;
import edu.hitsz.supply.BombSupply.BombSubscriber;

/**
 * @author 林子睿
 */
public class FireSupply extends AbstractSupply {

    public FireSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    public void function() {
        System.out.println("FireSupply Active!");
        Runnable r =()->{
            HeroAircraft.getHeroAircraft().setFireEnhance(true);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            HeroAircraft.getHeroAircraft().setFireEnhance(false);
        };
        new Thread(r).start();
    }

    @Override
    public void addSubscriber(BombSubscriber object) {

    }
}
