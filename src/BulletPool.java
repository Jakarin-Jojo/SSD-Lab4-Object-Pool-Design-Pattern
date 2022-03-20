import java.util.ArrayList;
import java.util.List;

public class BulletPool {
    private List<Bullet> bullets = new ArrayList<Bullet>();
    int size = 30;
    public BulletPool(){
        for (int i = 0; i < size ; i++){
            bullets.add(new Bullet(-999,-999 , 0 , 0));
        }
    }

    public Bullet requestBullet(int x, int y, int dx, int dy){
        try {
            Bullet bullet = bullets.remove(0);
            bullet.refreshState(x, y , dx ,dy);
            return bullet;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Not enough bullet");
            size += 1;
            Bullet bullet = new Bullet(-999, -999, 0, 0);
            bullets.add(bullet);
            return bullet;
        }
    }

    public void releaseBullet(Bullet bullet){
            bullets.add(bullet);
    }

    public void resetPool() {
        System.out.println(size);
        size = 30;
    }
}
