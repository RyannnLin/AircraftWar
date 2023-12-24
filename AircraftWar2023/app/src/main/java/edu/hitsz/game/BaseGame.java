package edu.hitsz.game;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.hitsz.ImageManager;
import edu.hitsz.R;
import edu.hitsz.activity.GameActivity;
import edu.hitsz.activity.MainActivity;
import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.aircraft.enemy.AbstractEnemy;
import edu.hitsz.aircraft.enemy.BossEnemy.BossEnemy;
import edu.hitsz.aircraft.enemy.EliEnemy.EliEnemyFactory;
import edu.hitsz.aircraft.enemy.EnemyFactory;
import edu.hitsz.aircraft.enemy.MobEnemy.MobEnemyFactory;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.AbstractBullet;
import edu.hitsz.scoreDAO.Score;
import edu.hitsz.scoreDAO.ScoreDAO;
import edu.hitsz.scoreDAO.ScoreDaoImpl;
import edu.hitsz.supply.AbstractSupply;
import edu.hitsz.supply.BombSupply.BombSupply;

/**
 * 游戏逻辑抽象基类，遵循模板模式，action() 为模板方法
 * 包括：游戏主面板绘制逻辑，游戏执行逻辑。
 * 子类需实现抽象方法，实现相应逻辑
 *
 * @author hitsz
 */
public abstract class BaseGame extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    public static final String TAG = "BaseGame";
    boolean mbLoop = false; //控制绘画线程的标志位
    private SurfaceHolder mSurfaceHolder;
    private Canvas canvas;  //绘图的画布
    private Paint mPaint;
    private Handler handler;
    private String username = "";
    private int rivalScore;//敌方分数


    public void setUsername(String username) {
        this.username = username;
    }

    public void setRivalScore(int score) {
        rivalScore = score;
    }

    protected Context con = getContext();


    /**
     * 音乐
     */
    public static boolean isMusicOn = true;

    private HashMap<Integer, Integer> soundID = new HashMap<Integer, Integer>();
    protected MediaPlayer bgmPlayer = MediaPlayer.create(getContext(), R.raw.bgm);
    private SoundPool soundPool;
    private HashMap<Integer, Integer> mSoundMap;
    private static final int BULLET_SHOOT = 1;
    private static final int BULLET_HIT = 2;
    private static final int BOMB_ACTION = 3;
    private static final int GET_SUPPLY = 4;
    private static final int GAME_OVER = 5;

    private void createSoundPool() {
        if (soundPool == null) {
            AudioAttributes audioAttributes =
                    new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
    }

    //点击屏幕位置
    float clickX = 0, clickY = 0;

    private int backGroundTop = 0;

    /**
     * 背景图片缓存，可随难度改变
     */
    protected Bitmap backGround;


    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 16;

    private final HeroAircraft heroAircraft;

    protected final List<AbstractEnemy> enemyAircrafts;

    private final List<AbstractBullet> heroBullets;
    private final List<AbstractBullet> enemyBullets;

    protected List<AbstractSupply> supplies;

    protected int enemyMaxNumber;

    private boolean gameOverFlag;

    public boolean getGameOverFlag() {
        return gameOverFlag;
    }

    public int score;
    public int bossScoreDuration = 0;
    private int time;

    protected static ScoreDAO sd = new ScoreDaoImpl();

    public static ScoreDAO getScoreDAO() {
        return sd;
    }

    /**
     * 周期（ms)
     * 控制英雄机射击周期，默认值设为简单模式
     */
    private int cycleDuration = 600;
    protected int shootCycleDuration = 400;
    private int cycleTime;


    public BaseGame(Context context, Handler handler) {
        super(context);
        this.handler = handler;
        mbLoop = true;
        mPaint = new Paint();  //设置画笔
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(this);
        this.setFocusable(true);
        ImageManager.initImage(context);

        //音乐部分
        bgmPlayer.setLooping(true);//设置背景音乐循环
        createSoundPool();
        mSoundMap = new HashMap<Integer, Integer>();
        mSoundMap.put(BULLET_SHOOT, soundPool.load(con, R.raw.bullet, 1));
        mSoundMap.put(BULLET_HIT, soundPool.load(con, R.raw.bullet_hit, 1));
        mSoundMap.put(BOMB_ACTION, soundPool.load(con, R.raw.bomb_explosion, 1));
        mSoundMap.put(GET_SUPPLY, soundPool.load(con, R.raw.get_supply, 1));
        mSoundMap.put(GAME_OVER, soundPool.load(con, R.raw.heaven, 1));

        // 初始化飞机
        heroAircraft = HeroAircraft.getHeroAircraft();
        heroAircraft.setHp(1000);
        enemyAircrafts = new CopyOnWriteArrayList<>();
        heroBullets = new CopyOnWriteArrayList<>();
        enemyBullets = new CopyOnWriteArrayList<>();

        //道具
        supplies = new CopyOnWriteArrayList<>();


        //相关变量的初始化
        gameOverFlag = false;
        score = 0;
        rivalScore = 0;
        time = 0;
        cycleTime = 0;


        heroController();
    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {
        //new Thread(new Runnable() {
        Runnable task = () -> {

            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);
                if (enemyAircrafts.size() < enemyMaxNumber) {
                    Log.d("BaseGame", "produceEnemy");
                    creatGameEnemy();
                }
                shootAction();
            }

            // 子弹移动
            bulletsMoveAction();
            // 飞机移动
            aircraftsMoveAction();
            //道具移动
            supplyMoveAction();

            // 撞击检测
            try {
                crashCheckAction();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 后处理
            postProcessAction();

            try {
                Thread.sleep(timeInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //}
        };
        new Thread(task).start();
    }

    public void setMusic(boolean b) {
        isMusicOn = b;
    }

    private void turnOnBgm() {
        if (isMusicOn) {
            bgmPlayer.start();
        }
    }

    protected void changeMusic(int music) {
        bgmPlayer.stop();
        bgmPlayer.reset();
        bgmPlayer.release();
        bgmPlayer = MediaPlayer.create(getContext(), music);
        bgmPlayer.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void heroController() {
        setOnTouchListener((view, motionEvent) -> {
            clickX = motionEvent.getX();
            clickY = motionEvent.getY();
            heroAircraft.setLocation(clickX, clickY);

            if (clickX < 0 || clickX > MainActivity.screenWidth || clickY < 0 || clickY > MainActivity.screenHeight) {
                // 防止超出边界
                return false;
            }
            return true;
        });
    }

    /**
     * 创建敌机（仅普通敌机和精英机）
     */
    protected void creatGameEnemy() {
        if (enemyAircrafts.size() < enemyMaxNumber) {
            EnemyFactory enemyFactory;
            if (Math.random() * 10 > 7) {
                enemyFactory = new EliEnemyFactory(1);
            } else {
                enemyFactory = new MobEnemyFactory(1);
            }
            enemyAircrafts.add(enemyFactory.creatEnemy());
        }
    }

    private void shootAction() {
        // 敌机射击
        for (AbstractAircraft aircraft : enemyAircrafts) {
            enemyBullets.addAll(aircraft.shoot());
        }
        // 英雄射击
        heroBullets.addAll(heroAircraft.shoot());
    }

    private boolean timeCountAndNewCycleJudge() {
        cycleDurationController();
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    protected void cycleDurationController() {
        if (time > 0 && time % 6000 == 0 && cycleDuration >= 150) {
            cycleDuration -= 25;
            shootCycleDuration -= 10;
            System.out.println("难度提升，当前周期:" + cycleDuration);
        }
    }

    private void bulletsMoveAction() {
        for (AbstractBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (AbstractBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void supplyMoveAction() {
        for (AbstractSupply supply : supplies) {
            supply.forward();
        }
    }

    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() throws InterruptedException {
        // 敌机子弹攻击英雄
        for (AbstractBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 英雄子弹攻击敌机
        for (AbstractBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractEnemy enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    //子弹命中音效
                    if (isMusicOn) {
                        soundPool.play(mSoundMap.get(BULLET_HIT), 1, 1, 0, 0, 1);
                    }

                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        //获得分数，产生道具补给
                        if (enemyAircraft.getClass().getName().equals(BossEnemy.class.getName())) {
                            score += 30;
                            bossScoreDuration += 30;
                        } else {
                            score += 10;
                            bossScoreDuration += 30;
                        }
                        if (MainActivity.ONLINE) {
                            Message msg = new Message();
                            msg.what = 2;
                            msg.obj = score;
                            handler.sendMessage(msg);
                        }
                        List<AbstractSupply> supply = enemyAircraft.getSupply();
                        supplies.addAll(supply);
                        //击落敌机，关闭boss背景音乐
                        if (enemyAircraft.getClass().getName().equals(BossEnemy.class.getName())) {
                            if (isMusicOn) {
                                changeMusic(R.raw.bgm);
                            }
                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }
        //我方获得道具，道具生效
        for (AbstractSupply supply : supplies) {
            if (heroAircraft.crash(supply)) {
                if (isMusicOn) {
                    soundPool.play(mSoundMap.get(4), 1, 1, 0, 0, 1);
                    if (supply.getClass().getName().equals(BombSupply.class.getName())) {
                        soundPool.play(mSoundMap.get(BOMB_ACTION), 0.6f, 0.6f, 0, 0, 1);
                    }
                }
                for (AbstractEnemy e : enemyAircrafts) {
                    supply.addSubscriber(e);
                    if (supply.getClass().getName().equals(BombSupply.class.getName())) {
                        if (e.getClass().getName().equals(BossEnemy.class.getName())) {
                            score += 30;
                            bossScoreDuration += 30;
                        } else {
                            score += 10;
                            bossScoreDuration += 10;
                        }
                    }
                }
                for (AbstractBullet e : enemyBullets) {
                    supply.addSubscriber(e);
                }
                supply.function();
                supply.vanish();
            }
        }
    }

    /**
     * 后处理：<p>
     * 1. 删除无效的子弹<p>
     * 2. 删除无效的敌机<p>
     * 3. 检查英雄机生存<p>
     * 4. 删除无效道具<p>
     * 5. 关闭音乐，播放死亡音效<p>
     * 6. 存档<p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        supplies.removeIf(AbstractFlyingObject::notValid);

        if (heroAircraft.notValid()) {
            gameOverFlag = true;
            mbLoop = false;

            bgmPlayer.stop();
            if (isMusicOn) {
                soundPool.play(mSoundMap.get(GAME_OVER), 0.3f, 0.3f, 0, 0, 1);
            }
            scoreSaving();
            Log.i(TAG, "heroAircraft is not Valid");
        }
    }

    protected void scoreSaving() {
        Score s = new Score(username, score, LocalDateTime.now());
        //本地数据
        sd.addScore(s);
        sd.listAllScores();
        //联机数据
        if (MainActivity.ONLINE) {
            /* TODO 联机数据处理 */
            Message msg = new Message();
            msg.obj = score;
            handler.sendMessage(msg);
        }
    }

    public void draw() {
        canvas = mSurfaceHolder.lockCanvas();
        if (mSurfaceHolder == null || canvas == null) {
            return;
        }

        //绘制背景，图片滚动
        canvas.drawBitmap(backGround, 0, this.backGroundTop - backGround.getHeight(), mPaint);
        canvas.drawBitmap(backGround, 0, this.backGroundTop, mPaint);
        backGroundTop += 1;
        if (backGroundTop == MainActivity.screenHeight)
            this.backGroundTop = 0;

        //先绘制子弹，后绘制飞机
        paintImageWithPositionRevised(enemyBullets); //敌机子弹


        paintImageWithPositionRevised(heroBullets);  //英雄机子弹


        paintImageWithPositionRevised(enemyAircrafts);//敌机

        paintImageWithPositionRevised(supplies);//敌机


        canvas.drawBitmap(ImageManager.HERO_IMAGE,
                heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2,
                mPaint);

        //画生命值
        paintScoreAndLife();

        mSurfaceHolder.unlockCanvasAndPost(canvas);

    }

    private void paintImageWithPositionRevised(List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            Bitmap image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            canvas.drawBitmap(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, mPaint);
        }
    }

    private void paintScoreAndLife() {
        int x = 10;
        int y = 40;

        mPaint.setColor(Color.RED);
        mPaint.setTextSize(50);
        canvas.drawText("SCORE:" + this.score, x, y, mPaint);
        y = y + 60;
        canvas.drawText("LIFE:" + this.heroAircraft.getHp(), x, y, mPaint);
        y = y + 60;
        if (MainActivity.ONLINE) {
            canvas.drawText(GameActivity.rivalName + ": " + GameActivity.rivalScore, x, y, mPaint);
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        new Thread(this).start();
        Log.i(TAG, "start surface view thread");
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        MainActivity.screenWidth = i1;
        MainActivity.screenHeight = i2;
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        mbLoop = false;
    }

    @Override
    public void run() {
        turnOnBgm();
        while (mbLoop) {   //游戏结束停止绘制
            synchronized (mSurfaceHolder) {
//                Message msg = new Message();
//                msg.what = 0x123;
//                msg.obj = score;
//                handler.sendMessage(msg);
                action();
                draw();
            }
        }
        Message message = Message.obtain();
        message.what = gameOverFlag ? 1 : 0;
        message.obj = score;
        handler.sendMessage(message);
    }
}
