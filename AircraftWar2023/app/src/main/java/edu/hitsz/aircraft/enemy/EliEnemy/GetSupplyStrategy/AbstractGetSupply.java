package edu.hitsz.aircraft.enemy.EliEnemy.GetSupplyStrategy;

import edu.hitsz.supply.AbstractSupply;

import java.util.List;

public abstract class AbstractGetSupply {
    protected int locationX;
    protected int locationY;
    protected double probability = -1;

    public AbstractGetSupply(int locationX, int locationY) {
        this.locationX = locationX;
        this.locationY = locationY;
    }

    public void setProbability(double x) {
        probability = x;
    }

    public abstract List<AbstractSupply> getSupply();
}
