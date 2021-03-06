import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

public class Game extends Observable {
    private BulletPool bulletPool;

    private int width = 600;
    private int height = 600;
    private long resetTime = new Date().getTime();

    private List<Bullet> bullets;
    private Thread mainLoop;
    private boolean alive;

    public Game() {
        bulletPool = new BulletPool();

        alive = true;
        bullets = new ArrayList<Bullet>();
        mainLoop = new Thread() {
            @Override
            public void run() {
                while(alive) {
                    tick();
                    setChanged();
                    notifyObservers();
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mainLoop.start();
    }

    public void tick() {
        long currentTime = new Date().getTime();
        int millisToSec = 1000;
        if (currentTime - resetTime >= millisToSec * 30) {
            resetTime = new Date().getTime();
        }
        else if (bullets.size() < 30) {
            bulletPool.resetPool();
            resetTime = new Date().getTime();
        }
        moveBullets();
        cleanupBullets();
    }

    private void moveBullets() {
        for(Bullet bullet : bullets) {
            bullet.move();
        }
    }

    private void cleanupBullets() {
        List<Bullet> toRemove = new ArrayList<Bullet>();
        for(Bullet bullet : bullets) {
            if(bullet.getX() <= 0 ||
                    bullet.getX() >= width ||
                    bullet.getY() <= 0 ||
                    bullet.getY() >= height) {
                toRemove.add(bullet);
            }
        }
        for(Bullet bullet : toRemove) {
            bullets.remove(bullet);
            bulletPool.releaseBullet(bullet);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void burstBullets(int x, int y) {
        bullets.add(bulletPool.requestBullet(x, y, 1, 0));
        bullets.add(bulletPool.requestBullet(x, y, 0, 1));
        bullets.add(bulletPool.requestBullet(x, y, -1, 0));
        bullets.add(bulletPool.requestBullet(x, y, 0, -1));
        bullets.add(bulletPool.requestBullet(x, y, 1, 1));
        bullets.add(bulletPool.requestBullet(x, y, 1, -1));
        bullets.add(bulletPool.requestBullet(x, y, -1, 1));
        bullets.add(bulletPool.requestBullet(x, y, -1, -1));
    }

}
