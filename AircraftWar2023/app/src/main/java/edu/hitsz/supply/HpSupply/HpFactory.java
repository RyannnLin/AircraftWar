package edu.hitsz.supply.HpSupply;

import edu.hitsz.supply.AbstractSupply;
import edu.hitsz.supply.supplyFactory;

public class HpFactory implements supplyFactory {

    @Override
    public AbstractSupply creatSupply(int locationX, int locationY, int speedX, int speedY) {
        return new HpSupply(locationX, locationY, speedX, speedY);
    }
}
