package edu.hitsz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.HashMap;
import java.util.Map;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.enemy.BossEnemy.BossEnemy;
import edu.hitsz.aircraft.enemy.EliEnemy.EliEnemy;
import edu.hitsz.aircraft.enemy.MobEnemy.MobEnemy;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.supply.BombSupply.BombSupply;
import edu.hitsz.supply.FireSupply.FireSupply;
import edu.hitsz.supply.HpSupply.HpSupply;

public class ImageManager {
    /**
     * 类名-图片 映射，存储各基类的图片 <br>
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    private static final Map<String, Bitmap> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public static Bitmap BACKGROUND1_IMAGE;
    public static Bitmap BACKGROUND2_IMAGE;
    public static Bitmap BACKGROUND3_IMAGE;
    public static Bitmap HERO_IMAGE;
    public static Bitmap HERO_BULLET_IMAGE;
    public static Bitmap ENEMY_BULLET_IMAGE;
    public static Bitmap MOB_ENEMY_IMAGE;
    public static Bitmap ELITE_ENEMY_IMAGE;
    public static Bitmap BOSS_ENEMY_IMAGE;
    public static Bitmap FIRE_SUPPLY_IMAGE;
    public static Bitmap HP_SUPPLY_IMAGE;
    public static Bitmap BOMB_SUPPLY_IMAGE;

    public static Bitmap BACKGROUND1_bg;
    public static void initImage(Context context){
        //背景
        ImageManager.BACKGROUND1_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg);
        ImageManager.BACKGROUND2_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg2);
        ImageManager.BACKGROUND3_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.bg3);
        //飞机
        ImageManager.HERO_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.hero);
        ImageManager.MOB_ENEMY_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.mob);
        ImageManager.ELITE_ENEMY_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.elite);
        ImageManager.BOSS_ENEMY_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.boss);
        CLASSNAME_IMAGE_MAP.put(HeroAircraft.class.getName(), HERO_IMAGE);
        CLASSNAME_IMAGE_MAP.put(MobEnemy.class.getName(), MOB_ENEMY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(EliEnemy.class.getName(), ELITE_ENEMY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(BossEnemy.class.getName(),BOSS_ENEMY_IMAGE);
        //子弹
        ImageManager.HERO_BULLET_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_hero);
        ImageManager.ENEMY_BULLET_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet_enemy);
        CLASSNAME_IMAGE_MAP.put(HeroBullet.class.getName(), HERO_BULLET_IMAGE);
        CLASSNAME_IMAGE_MAP.put(EnemyBullet.class.getName(), ENEMY_BULLET_IMAGE);
        //道具
        ImageManager.FIRE_SUPPLY_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.prop_bullet);
        ImageManager.HP_SUPPLY_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.prop_blood);
        ImageManager.BOMB_SUPPLY_IMAGE = BitmapFactory.decodeResource(context.getResources(), R.drawable.prop_bomb);
        CLASSNAME_IMAGE_MAP.put(FireSupply.class.getName(),FIRE_SUPPLY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(HpSupply.class.getName(),HP_SUPPLY_IMAGE);
        CLASSNAME_IMAGE_MAP.put(BombSupply.class.getName(),BOMB_SUPPLY_IMAGE);




    }

    public static Bitmap get(String className){
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public static Bitmap get(Object obj){
        if (obj == null){
            return null;
        }
        return get(obj.getClass().getName());
    }
}
