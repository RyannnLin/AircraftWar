package edu.hitsz.supply.FireSupply;

import edu.hitsz.supply.AbstractSupply;
import edu.hitsz.supply.supplyFactory;

public class FireFactory implements supplyFactory {
    public AbstractSupply creatSupply(int locationX, int locationY, int speedX, int speedY){
        return new FireSupply(locationX, locationY, speedX, speedY);
    }
}