package edu.hitsz.supply.BombSupply;

import java.util.ArrayList;
import java.util.List;

import edu.hitsz.supply.AbstractSupply;


public class BombSupply extends AbstractSupply {
    private List<BombSubscriber> bombBombSubscribers = new ArrayList<>();

    public void addSubscriber(BombSubscriber object) {
        bombBombSubscribers.add(object);
    }

    public BombSupply(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }


    @Override
    public void function() {
//        if(StartMenu.game.getIsMusicOn()){
//            new MusicThread(MusicThread.BOMB_EXPLOSION_SOUND).start();
//        }
        for(BombSubscriber subscriber: bombBombSubscribers){
            subscriber.update();
        }
        System.out.println("BombSupply Active!");
    }
}
