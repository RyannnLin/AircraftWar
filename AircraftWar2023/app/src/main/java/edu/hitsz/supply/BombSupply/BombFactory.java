package edu.hitsz.supply.BombSupply;

import edu.hitsz.supply.AbstractSupply;
import edu.hitsz.supply.supplyFactory;

public class BombFactory implements supplyFactory {
    public AbstractSupply creatSupply(int locationX, int locationY, int speedX, int speedY){
        return new BombSupply(locationX, locationY, speedX, speedY);
    }
}
