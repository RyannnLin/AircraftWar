package edu.hitsz.aircraft.enemy;

public interface EnemyFactory {
    AbstractEnemy creatEnemy();
    void IncreaseHp();
}
