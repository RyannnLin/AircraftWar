package edu.hitsz.supply;

public interface supplyFactory {
    AbstractSupply creatSupply(int locationX, int locationY, int speedX, int speedY);
}
